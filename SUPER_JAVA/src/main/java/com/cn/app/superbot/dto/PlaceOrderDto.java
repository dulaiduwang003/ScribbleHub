package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Place order dto.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class PlaceOrderDto {

    /**
     * The Id.
     */
    @NotNull(message = "选定商品不能为空")
    private Long id;
}
