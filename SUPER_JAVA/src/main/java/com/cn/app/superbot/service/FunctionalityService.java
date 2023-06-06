package com.cn.app.superbot.service;

import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.dto.CreationsDto;
import com.cn.app.superbot.dto.MappingDto;
import reactor.core.publisher.Flux;

/**
 * The interface Functionality service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
public interface FunctionalityService {


    /**
     * Generate image string.
     *
     * @param dto the dto
     * @return the string
     */
    String generateImage(final MappingDto dto);


    /**
     * Cache flux.
     *
     * @param model the model
     * @return the flux
     */
    Flux<String> cache(final ChatGPTDto model);


    /**
     * Originality string.
     *
     * @param dto the dto
     * @return the string
     */
    String originality(final CreationsDto dto);


}
