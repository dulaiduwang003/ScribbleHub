package com.cn.app.superbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * The enum Chat enum.
 *
 * @author bdth
 */
@AllArgsConstructor
@Getter
public enum ChatEnum {

    /**
     * Bing chat enum.
     */
    BING("BING"),


    /**
     * Gpt 3 5 chat enum.
     */
    GPT3_5("GPT3_5"),


    /**
     * Gpt 4 0 chat enum.
     */
    GPT4_0("GPT4_0");


    /**
     * 行为
     */
    private final String dec;

}
