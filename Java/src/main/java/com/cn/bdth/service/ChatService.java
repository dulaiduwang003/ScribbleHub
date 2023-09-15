package com.cn.bdth.service;


import com.cn.bdth.dto.AiChatDto;
import reactor.core.publisher.Flux;

/**
 * bot业务处理接口
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface ChatService {

    /**
     * bot对话
     *
     * @return the flux
     */
    Flux<String> chatFlux(final AiChatDto dto);

    /**
     * Bing对话

     * @return  the flux
     */
    Flux<String> bingFlux(final String messages);
}
