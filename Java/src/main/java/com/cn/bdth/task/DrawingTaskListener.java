package com.cn.bdth.task;

import com.alibaba.fastjson.JSONObject;
import com.cn.bdth.common.AiConfig;
import com.cn.bdth.common.FunCommon;
import com.cn.bdth.common.WxSubscribe;
import com.cn.bdth.common.WxSubscribeTemplate;
import com.cn.bdth.constants.AiConstant;
import com.cn.bdth.entity.SeaImage;
import com.cn.bdth.entity.SeaResource;
import com.cn.bdth.enums.FileEnum;
import com.cn.bdth.mapper.SeaImageMapper;
import com.cn.bdth.mapper.SeaResourceMapper;
import com.cn.bdth.model.PictureDrawingModel;
import com.cn.bdth.structure.DrawingStructure;
import com.cn.bdth.utils.BaiduTranslationUtil;
import com.cn.bdth.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 绘图任务监听器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DrawingTaskListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final FunCommon funCommon;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final WebClient.Builder webClientBuilder;
    private final Semaphore semaphore;
    private final SeaImageMapper imageMapper;
    private final SeaResourceMapper resourceMapper;
    private final UploadUtil uploadUtil;
    private final WxSubscribeTemplate wxSubscribeTemplate;
    private final WxSubscribe wxSubscribe;

    private final BaiduTranslationUtil baiduTranslationUtil;

    @Autowired
    public DrawingTaskListener(BaiduTranslationUtil baiduTranslationUtil, WxSubscribe wxSubscribe, WxSubscribeTemplate wxSubscribeTemplate, SeaResourceMapper resourceMapper, RedisTemplate<String, Object> redisTemplate, FunCommon funCommon, WebClient.Builder webClientBuilder, SeaImageMapper imageMapper, UploadUtil uploadUtil) {
        this.baiduTranslationUtil = baiduTranslationUtil;
        this.wxSubscribe = wxSubscribe;
        this.resourceMapper = resourceMapper;
        this.uploadUtil = uploadUtil;
        this.redisTemplate = redisTemplate;
        this.imageMapper = imageMapper;
        this.wxSubscribeTemplate = wxSubscribeTemplate;
        this.funCommon = funCommon;
        this.webClientBuilder = webClientBuilder;
        this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        this.threadPoolTaskExecutor.setCorePoolSize(1);
        this.threadPoolTaskExecutor.setMaxPoolSize(1);
        this.threadPoolTaskExecutor.setThreadNamePrefix("DrawingTaskExecutor-");
        this.threadPoolTaskExecutor.initialize();
        this.semaphore = new Semaphore(1);

    }

    /**
     * 监听任务队列
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startListening() {
        threadPoolTaskExecutor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    semaphore.acquire();
                    // 尝试获取信号量许可
                    DrawingStructure drawingStructure = (DrawingStructure) redisTemplate.opsForList().rightPop(AiConstant.DRAWING_TASK_QUEUE, 2, TimeUnit.SECONDS);
                    if (drawingStructure != null) {
                        final PictureDrawingModel model = drawingStructure.getPictureDrawingModel();
                        //尝试翻译
                        try {
                            model.setPrompt(baiduTranslationUtil.translation(model.getPrompt()));
                        } catch (Exception e) {
                            log.error("绘图时翻译失败  原因：{} 位置：{}", e.getMessage(), e.getClass());
                        }
                        invokeDrawingApi(model, drawingStructure.getIsType() == AiConstant.DRAWING_IMAGE_TYPE ? AiConstant.DRAWING_IMAGE : AiConstant.DRAWING_TEXT);
                    }
                } catch (Exception e) {
                    log.error("绘图出现异常:{},位置:{}", e.getMessage(), e.getClass());
                } finally {
                    semaphore.release(); // 释放信号量许可
                }
            }
        });
    }


    /**
     * 调用 绘图API
     *
     * @param model the model
     */
    public void invokeDrawingApi(final PictureDrawingModel model, final String uri) {
        final AiConfig aiConfig = funCommon.currentConfigurationServer();
        final String block = webClientBuilder.build()
                .post()
                .uri(aiConfig.getSdUrl() + uri)
                .body(BodyInserters.fromValue(model))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        Pair<String, String> stringStringPair;
        try {
            final String dec = FileEnum.PAINTING.getDec();
            //下载图片
            stringStringPair = uploadUtil.downloadImage(Objects.requireNonNull(JSONObject.parseObject(
                    block
            )).getJSONArray("images").getString(0), dec);
            //保存图片至数据库
            final SeaResource seaResource = new SeaResource()
                    .setType(dec)
                    .setUri(stringStringPair.getRight())
                    .setTarget(stringStringPair.getLeft());
            resourceMapper.insert(seaResource);
            //回调至用户数据表
            imageMapper.updateById(
                    new SeaImage()
                            .setSeaImageId(model.getSeaImageId())
                            .setGenerateId(seaResource.getSeaResourceId())
            );
            //把制作结果发给用户
            final JSONObject jsonObject = wxSubscribeTemplate.drawingOutcomeNotice(model.getOpenId(), true, "创作成功!前往个人中心查看吧!", LocalDateTime.now());
            wxSubscribe.wxSubscribeMessages(jsonObject);
        } catch (IOException e) {
            final JSONObject jsonObject = wxSubscribeTemplate.drawingOutcomeNotice(model.getOpenId(), false, "创作失败!请检查关键词是否违规!", LocalDateTime.now());
            wxSubscribe.wxSubscribeMessages(jsonObject);
            log.error("在处理成功响应图片转化BASE64时出现报错:{} ,错误类:{}", e.getMessage(), e.getClass());
        }

    }
}
