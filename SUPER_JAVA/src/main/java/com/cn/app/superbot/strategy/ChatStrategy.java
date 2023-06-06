package com.cn.app.superbot.strategy;

import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.structure.StrategyStructure;
import reactor.core.publisher.Flux;


/**
 * The interface Chat strategy.
 *
 * @author bdth
 */
public interface ChatStrategy {

    /**
     * Frequency long.
     *
     * @return the long
     */
    Long frequency();


    /**
     * Type string.
     *
     * @return the string
     */
    String type();


    /**
     * Is type match boolean.
     *
     * @param type the type
     * @return the boolean
     */
    boolean isTypeMatch(final String type);


    /**
     * Execution flux.
     *
     * @param config the config
     * @param dto    the dto
     * @return the flux
     */
    Flux<String> execution(final StrategyStructure config, final ChatGPTDto dto);
}
