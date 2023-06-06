package com.cn.app.superbot.model;

import lombok.Data;

/**
 * The type Mj model.
 */
@Data
public class MjModel {
    /**
     * The Prompt.
     */
    private String prompt;
    /**
     * The Steps.
     */
    private Integer steps = 20;

}
