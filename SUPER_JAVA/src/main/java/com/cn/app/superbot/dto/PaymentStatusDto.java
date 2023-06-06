package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * The type Payment status dto.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class PaymentStatusDto {

    /**
     * The Order no.
     */
    @NotBlank(message = "参数不能为空")
    private String orderNo;
}
