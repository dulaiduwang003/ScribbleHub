package com.cn.app.superbot.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type User order page vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@Accessors(chain = true)
public class UserPageVo implements Serializable {

    /**
     * The Id.
     */
    private String id;


    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Created time.
     */
    private String email;

    /**
     * The Created time.
     */
    private String password;

    /**
     * The Frequency.
     */
    private Long frequency;


    private Long number;


}
