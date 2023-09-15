package com.cn.bdth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 添加评论
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class InsertPublicationCommentDto {

    @NotBlank(message = "内容不能为空")
    private String commentContent;

    @NotNull(message = "所属文章不能为空")
    private Long seaBlogId;

}
