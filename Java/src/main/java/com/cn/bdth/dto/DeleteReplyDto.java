package com.cn.bdth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 删除回复
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class DeleteReplyDto {

    @NotNull(message = "所选评论不能为空")
    private Long seaReplyId;
}
