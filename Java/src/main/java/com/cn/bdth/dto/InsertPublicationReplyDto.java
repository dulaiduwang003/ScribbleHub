package com.cn.bdth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type Publication comment dto.
 */
@Data
@Accessors(chain = true)
public class InsertPublicationReplyDto {

    /**
     * The Content.
     */
    @NotBlank(message = "回复内容不能为空")
    private String replyContent;

    /**
     * The Sea blog id.
     */
    @NotNull(message = "主评论不能为空")
    private Long seaCommentId;

    /**
     * The Reciprocity id.
     */
    private Long reciprocityId;

}
