package com.cn.bdth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 删除文章内容资源
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class DeleteArticleContentResource {

    @NotEmpty(message = "资源ID不能为空")
    private List<Long> seaResourceIdList;

    @NotNull(message = "指向不能为空")
    private Boolean isUrl;

    private String uri;
}
