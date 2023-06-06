package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Face model.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
@Accessors(chain = true)
public class SdModel {

    /**
     * The Prompt.
     */
    private String prompt;
    /**
     * The Steps.
     */
    private Integer steps = 120;

}
