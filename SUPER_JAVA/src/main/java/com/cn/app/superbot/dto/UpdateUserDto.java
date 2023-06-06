package com.cn.app.superbot.dto;


import lombok.Data;
import lombok.experimental.Accessors;


/**
 * The type SuperUser.
 */
@Data
@Accessors(chain = true)
public class UpdateUserDto {

    private Long id;
    /**
     * The Email.
     */
    private String email;
    /**
     * The Password.
     */
    private String password;
    /**
     * The Frequency.
     */
    private Long frequency;

}
