package com.cn.app.superbot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.constants.AiConstants;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.dto.CreationsDto;
import com.cn.app.superbot.dto.MappingDto;
import com.cn.app.superbot.entity.SuperCache;
import com.cn.app.superbot.entity.SuperCreation;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.exception.GenerateImagesException;
import com.cn.app.superbot.mapper.CacheMapper;
import com.cn.app.superbot.mapper.CreationMapper;
import com.cn.app.superbot.model.*;
import com.cn.app.superbot.service.FunctionalityService;
import com.cn.app.superbot.structure.StrategyStructure;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.utils.ChatUtils;
import com.cn.app.superbot.utils.UploadUtil;
import com.cn.app.superbot.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


/**
 * The type Function service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionalityServiceImpl implements FunctionalityService {


    /**
     * The Chat utils.
     */
    private final ChatUtils chatUtils;

    /**
     * The Img mapper.
     */
    private final CreationMapper creationMapper;


    /**
     * The Cache mapper.
     */
    private final CacheMapper cacheMapper;


    /**
     * The Upload util.
     */
    private final UploadUtil uploadUtil;


    /**
     * Cache flux.
     *
     * @param model the model
     * @return the flux
     */
    @Override
    public Flux<String> cache(final ChatGPTDto model) {
        Optional<String> msg = Optional.empty();
        final List<ChatGPTDto.Messages> messages = (model.getMessages());
        if (!messages.isEmpty() && messages.size() >= 2) {
            final ChatGPTDto.Messages userMsg = messages.get(messages.size() - 1);
            final ChatGPTDto.Messages userMach = messages.get(messages.size() - 2);
            final List<SuperCache> superCaches = cacheMapper.selectList(new QueryWrapper<SuperCache>().lambda().likeRight(SuperCache::getSecondWord, userMsg.getContent()).select(SuperCache::getSecondWord).last("limit 5")).parallelStream().collect(Collectors.toList());
            msg = superCaches.stream().filter(cache -> chatUtils.isSimilar(cache.getMach(), userMach.getContent())).map(SuperCache::getContent).findFirst();
        }
        return Flux.just(msg.orElseThrow(RuntimeException::new));
    }

    /**
     * Originality string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public String originality(final CreationsDto dto) {
        final long frequency = ChatComment.operateStructure.getChatThreeFrequency();
        final long userId = UserUtils.userIdToLong();
        chatUtils.checkDeduct(userId, frequency);
        return
                //写入配置
                chatUtils.chatWebClient(ChatComment.strategyStructure)
                        .build()
                        .post()
                        .uri(AiConstants.MODEL_GPT_URI)
                        //写入请求体
                        .body(BodyInserters.fromValue(BeanUtils.copyClassProperTies(dto, GptThreeModel.class).setStream(false)))
                        .retrieve()
                        .bodyToMono(String.class)
                        .onErrorMap(Exception.class, e -> {
                            chatUtils.compensate(userId, frequency);
                            throw new CustomException(MsgConstants.GPT_API_ERR, 400);
                        }).block();

    }

    /**
     * Generate image string.
     *
     * @return the string
     */
    @Override
    public String generateImage(final MappingDto dto) {
        //获取服务器参数
        final StrategyStructure structure = ChatComment.strategyStructure;
        //获取用户ID
        final long userId = UserUtils.userIdToLong();
        //获取运营服务追踪 绘图每次需要调取多少次数
        final long frequency = ChatComment.operateStructure.getMappingFrequency();
        //检查次数,不足时抛出移除
        chatUtils.checkDeduct(userId, frequency);
        String result = null;

        final StrategyStructure.Mapping mapping = structure.mapping;
        try {
            try {
                switch (mapping.choice) {
                    case SD -> {
                        //获取到图片 并且将SD图片转化为png图片下载到本地
                        final String images = uploadUtil.toImagesUrl(JSONObject.parseObject(
                                // 这里使用GPT进行润色
                                chatUtils.webClientBuild(mapping.sdUrl, BeanUtils.copyClassProperTies(dto, SdModel.class).setPrompt(retouching(structure, dto.getPrompt())))

                        ).getJSONArray("images").getString(0));
                        //执行异步 缓存图片到服务器 并且写入数据库地址
                        asyncAuthoring(dto.getPrompt(), images);
                        result = images;
                    }
                    case MJ ->
                            //这里方便二开
                            result = chatUtils.webClientBuild(mapping.mjUrl, BeanUtils.copyClassProperTies(dto, MjModel.class));
                }
            } catch (Exception e) {
                final String imageUrl = uploadUtil.downloadImage(
                        openAiDall(structure, BeanUtils.copyClassProperTies(dto, GptAlphaModel.class).setPrompt(retouching(structure, dto.getPrompt())))
                );
                //执行异步 缓存图片到服务器 并且写入数据库地址
                asyncAuthoring(dto.getPrompt(), imageUrl);
                result = imageUrl;
            }
        } catch (Exception e) {
            //抛出异常 回滚次数
            chatUtils.compensate(userId, frequency);
            throw new GenerateImagesException(MsgConstants.GENERATE_IMAGES_ERR, 500);
        }
        return result;

    }

    /**
     * Insert authoring.
     *
     * @param prompt the prompt
     * @param url    the url
     */
    @Async
    public void asyncAuthoring(final String prompt, final String url) {
        creationMapper.insert(new SuperCreation().setPrompt(prompt).setUrl(url).setUserId(UserUtils.userIdToLong()));

    }

    /**
     * Open ai dall string.
     *
     * @param structure the structure
     * @param model     the model
     * @return the string
     */
    public String openAiDall(final StrategyStructure structure, Object model) {
        final String block = chatUtils.chatWebClient(structure)
                .build()
                .post()
                .uri(AiConstants.MODEL_DALL_URI)
                .body(BodyInserters.fromValue(model))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(60))
                .onErrorMap(TimeoutException.class,
                        e -> new CustomException(MsgConstants.GPT_API_TIMEOUT, 400)
                )
                .onErrorMap(Exception.class, e ->
                        new CustomException(MsgConstants.GPT_API_ERR, 400)
                ).block();
        final JSONObject json = JSONObject.parseObject(block);
        assert json != null;
        return json.getJSONArray("data").getJSONObject(0).getString("url");
    }

    /**
     * Retouching.
     *
     * @param structure the structure
     * @param parameter the parameter
     */
    public String retouching(final StrategyStructure structure, final String parameter) {
        final GptThreeModel model = new GptThreeModel().setStream(false).setMessages(List.of(new GptThreeModel.Messages().setRole("user").setContent(AiConstants.RETOUCHING + parameter)));
        final String block = chatUtils.chatWebClient(structure).build().post().uri(AiConstants.MODEL_GPT_URI).body(BodyInserters.fromValue(model)).retrieve().bodyToMono(String.class).timeout(Duration.ofSeconds(30)).onErrorMap(TimeoutException.class, e -> new CustomException(MsgConstants.GPT_API_TIMEOUT, 400)).onErrorMap(Exception.class, e -> {
            throw new CustomException(MsgConstants.GPT_API_ERR, 400);
        }).block();
        final JSONObject json = JSONObject.parseObject(block);
        assert json != null;
        return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
