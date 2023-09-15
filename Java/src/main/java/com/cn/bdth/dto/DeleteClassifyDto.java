package com.cn.bdth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 删除专栏
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class DeleteClassifyDto {

    @NotNull(message = "专题ID不能为空")
    private Long seaClassifyId;


}
