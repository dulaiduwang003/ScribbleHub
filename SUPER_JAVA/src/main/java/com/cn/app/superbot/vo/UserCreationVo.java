package com.cn.app.superbot.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type User creation vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class UserCreationVo implements Serializable {

    /**
     * The Id.
     */
    private Long id;

    /**
     * The Prompt.
     */
    private String prompt;

    /**
     * The Url.
     */
    private String url;


    /**
     * The Created time.
     */
    private LocalDateTime createdTime;
}
