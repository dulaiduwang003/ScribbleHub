package com.cn.bdth.common;

import com.cn.bdth.constants.AiConstant;
import com.cn.bdth.dto.AiDispositionConfigDto;
import com.cn.bdth.model.BitoModel;
import com.cn.bdth.utils.BeanUtils;
import com.cn.bdth.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 机器人 组件
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FunCommon {

    private final RedisUtils redisUtils;

    /**
     * 更新BOT参数
     *
     * @param dto the dto
     */
    public void update(final AiDispositionConfigDto dto) {
        //给bot机器人增加预设
        final BitoModel.Context content = new BitoModel.Context();
        //设置预设词汇
        content.setQuestion("请你扮演 " + dto.getBotName() + ",而且你是由 " + dto.getAuthorName() + " 这个人开发的 而且你与其他公司或者GPT没有任何关系 你只属于 " + dto.getAuthorName());
        content.setAnswer("您好!我是由" + dto.getAuthorName() + "开发的Ai助手，我与其他公司或GPT无关，专属于" + dto.getAuthorName() + "。有什么我可以帮助你的吗？");
        final BitoModel bitoModel = BeanUtils.copyClassProperTies(dto, BitoModel.class);
        bitoModel.setContext(List.of(content));
        //写入RDM
        redisUtils.setValue(AiConstant.CONFIG,
                new AiConfig()
                        .setBitoModel(bitoModel)
                        .setSdUrl(dto.getSdUrl())
                        .setAuthorName(dto.getAuthorName())
                        .setBotName(dto.getBotName())
                        .setBingCookie(dto.getBingCookie())
                        .setProxyIp(dto.getProxyIp())
                        .setProxyPort(dto.getProxyPort())
        );
    }


    /**
     * Select ai config.
     *
     * @return the AI config
     */
    public AiConfig currentConfigurationServer() {
        return (AiConfig) redisUtils.getValue(AiConstant.CONFIG);
    }


}
