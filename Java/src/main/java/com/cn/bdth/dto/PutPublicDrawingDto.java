package com.cn.bdth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设置公开
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class PutPublicDrawingDto {

    @NotNull(message = "绘图标识不能为空")
    private Long seaImageId;
}
