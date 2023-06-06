package com.cn.app.superbot.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Verification code dto.
 */
@Data
@Accessors(chain = true)
public class VerificationCodeDto {

    /**
     * The Email.
     */
    @Email(message = "邮箱格式错误")
    private String email;
}
