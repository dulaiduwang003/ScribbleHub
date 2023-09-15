package com.cn.bdth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设置推荐文章
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class PutPopularArticleDto {

    @NotNull(message = "博客ID不能为空")
    private Long seaBlogId;
}
