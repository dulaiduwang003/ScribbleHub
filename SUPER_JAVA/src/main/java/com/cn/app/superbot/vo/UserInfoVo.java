package com.cn.app.superbot.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserInfoVo implements Serializable {

    /**
     * The Frequency.
     */
    private Long frequency;


    /**
     * The Role.
     */
    private String role;


}
