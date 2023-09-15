package com.cn.bdth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.bdth.common.AiConfig;
import com.cn.bdth.common.FunCommon;
import com.cn.bdth.dto.AiChatDto;
import com.cn.bdth.handler.Chat;
import com.cn.bdth.interfaces.Callback;
import com.cn.bdth.model.BitoModel;
import com.cn.bdth.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * 博客文章业务处理类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    /**
     * The constant WEB_CLIENT.
     */
    private static final WebClient.Builder WEB_CLIENT = WebClient.builder();
    /**
     * The constant URL.
     */
    private static final String URL = "https://bitoai.bito.ai/ai/v2/chat/?processSilently=true";
    /**
     * The constant ORIGIN.
     */
    private static final String ORIGIN = "http://ideapp";
    /**
     * The Fun common.
     */
    private final FunCommon funCommon;

    /**
     * 数据流
     *
     * @param dto the dto
     * @return the flux
     */
    @Override
    public Flux<String> chatFlux(final AiChatDto dto) {
        //获取ai配置参数
        final AiConfig aiConfig = funCommon.currentConfigurationServer();
        //Chat Model
        final BitoModel bitoModel = aiConfig.getBitoModel();
        bitoModel.setPrompt(dto.getPrompt());
        if (dto.getContext() != null) {
            final List<BitoModel.Context> content = bitoModel.getContext();
            dto.getContext().addAll(content);
            bitoModel.setContext(dto.getContext());
        }
        return WEB_CLIENT
                .baseUrl(URL)
                .clientConnector(new ReactorClientHttpConnector())
                .defaultHeader(HttpHeaders.AUTHORIZATION, bitoModel.getAuthorization())
                .defaultHeader(HttpHeaders.ORIGIN, ORIGIN)
                .build()
                .post()
                .body(BodyInserters.fromValue(bitoModel))
                .retrieve()
                .bodyToFlux(String.class);

    }

    /**
     * 数据流
     *
     * @param messages the messages
     * @return the flux
     */
    @Override
    public Flux<String> bingFlux(String messages) {
        //获取ai配置参数
        final AiConfig aiConfig = funCommon.currentConfigurationServer();
        Chat chat = new Chat("_U=" + aiConfig.getBingCookie(), false)
                .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(aiConfig.getProxyIp(), aiConfig.getProxyPort())));
        return Flux.create(f -> {
            chat.newChat().newQuestion(messages, new Callback() {
                @Override
                public void onSuccess(JSONObject rawData) {
                    f.complete();
                }

                @Override
                public void onFailure(JSONObject rawData, String cause) {
                    f.complete();
                }

                @Override
                public void onUpdate(JSONObject rawData) {
                    f.next(String.valueOf(rawData));
                }
            });
        });
    }
}
