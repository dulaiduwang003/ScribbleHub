package com.cn.app.superbot.utils;

import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.constants.AiConstants;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.exception.InsufficientAmountException;
import com.cn.app.superbot.service.UserOperateService;
import com.cn.app.superbot.structure.StrategyStructure;
import com.cn.app.superbot.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * The type Chat utils.
 */
@Component
@RequiredArgsConstructor
public class ChatUtils {


    /**
     * The SuperUser service.
     */
    private final UserOperateService userOperateService;



    /**
     * The Web client.
     */
    private static final WebClient.Builder webClient = WebClient.builder();


    /**
     * Inspect.
     *
     * @param userId the user id
     * @param limit  the limit
     */
    public void checkDeduct(final long userId, final long limit) {
        final UserInfoVo userInfo = userOperateService.getUserInfo(userId);
        //inspection frequency
        if (userInfo == null || userInfo.getFrequency() < limit) {
            throw new InsufficientAmountException(MsgConstants.LIMIT_NOT_ERR, 400);
        }
        //number of deductions
        userOperateService.deductions(userId, limit);
    }

    public void compensate(final long userId, final long limit) {
        userOperateService.increase(userId, limit);
    }


    /**
     * Eliminate chat gpt dto.
     *
     * @param dto the dto
     * @return the chat gpt dto
     */
    public ChatGPTDto eliminate(final ChatGPTDto dto) {
        final Long memory = ChatComment.operateStructure.getMemory();
        List<ChatGPTDto.Messages> collect = dto.getMessages();
        if (collect.size() > memory) {
            collect = collect.stream()
                    .skip(collect.size() - memory)
                    .collect(Collectors.toList());
        }
        for (int i = 0; i < collect.size() - 1; i++) {
            ChatGPTDto.Messages m = collect.get(i);
            String message = m.getContent();
            if (message.length() > 50) {
                final String s = message.substring(0, 20) + ".....";
                m.setContent(s);
            }
        }
        return dto.setMessages(collect);
    }


    /**
     * Web client build string.
     *
     * @param url       the url
     * @param parameter the parameter
     * @return the string
     */
    public String webClientBuild(final String url, final Object parameter) {
        return webClient.build()
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(parameter))
                .retrieve()
                .bodyToMono(String.class)
                //define  60 second timeout
                .timeout(Duration.ofSeconds(60))
                .onErrorMap(TimeoutException.class, e -> new CustomException(MsgConstants.GPT_API_TIMEOUT, 400))
                .block();
    }

    /**
     * Is similar boolean.
     *
     * @param str1 the str 1
     * @param str2 the str 2
     * @return the boolean
     */
    public boolean isSimilar(final String str1, final String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        int maxLen = Math.max(len1, len2);
        double similarity = 1 - (double) dp[len1][len2] / maxLen;
        return similarity > 0.9;
    }

    /**
     * Chat dialogue web client builder.
     *
     * @param config the config
     * @return the web client builder
     */
    public WebClient.Builder chatWebClient(final StrategyStructure config) {
        switch (config.choose) {
            //directMode

            case DIRECT -> webClient
                    .baseUrl(config.getOfficial().baseUrl)
                    .defaultHeader(AiConstants.AUTHORIZATION, AiConstants.BEARER + config.official.key);
            //proxyMode
            case AGENT ->
                    webClient
                    .baseUrl(config.getOfficial().baseUrl)
                    .defaultHeader(AiConstants.AUTHORIZATION, AiConstants.BEARER + config.official.key)
                    .clientConnector(new ReactorClientHttpConnector(
                            //configure proxy connections
                            HttpClient.create()
                                    .proxy(proxy -> proxy
                                            .type(ProxyProvider.Proxy.HTTP)
                                            //set proxy
                                            .address(new InetSocketAddress(config.proxy.ip, config.proxy.port)))
                    ));
            //thirdPartyModel
            case CUSTOM -> webClient
                    .baseUrl(config.custom.baseUrl)
                    .defaultHeader(AiConstants.AUTHORIZATION, AiConstants.BEARER + config.custom.key);
            default -> throw new CustomException(MsgConstants.TACTICS_ERR, 500);
        }
        return webClient;
    }


}
