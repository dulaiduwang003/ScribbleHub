package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Delete creation dto.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class DeleteCreationDto {

    /**
     * The Id.
     */
    @NotNull(message = "ID不能为空 ")
    private Long id;
}
