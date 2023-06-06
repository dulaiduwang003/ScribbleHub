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
public enum ServerPolicyEnum {


    /**
     * Direct connection server policy enum.
     */
    DIRECT(),


    /**
     * Agent server policy enum.
     */
    AGENT(),


    /**
     * Custom server policy enum.
     */
    CUSTOM();

}
