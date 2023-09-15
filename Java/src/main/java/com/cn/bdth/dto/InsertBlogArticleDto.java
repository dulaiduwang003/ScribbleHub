package com.cn.bdth.dto;

import com.cn.bdth.entity.SeaBlog;
import com.cn.bdth.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加文章
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class InsertBlogArticleDto {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotNull(message = "文章封面不能为空")
    private MultipartFile file;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    private String summary;

    @NotBlank(message = "文章标签不能为空")
    private String label;

    @NotNull(message = "文章所属专栏不能为空")
    private Long seaClassifyId;

    private String fileResourceIds;

    @NotNull(message = "文章类型不能为空")
    private Long isRecommend;
    public static SeaBlog convertToSeaBlog(InsertBlogArticleDto item) {
        if (item == null) {
            return null;
        }
        SeaBlog result = new SeaBlog();
        result.setSeaClassifyId(item.getSeaClassifyId());
        result.setTitle(item.getTitle());
        result.setIsRecommend(item.getIsRecommend());
        result.setSummary(item.getSummary());
        result.setContent(item.getContent());
        result.setLabel(item.getLabel());
        if (item.getFileResourceIds() != null && StringUtils.isNotBlank(item.getFileResourceIds().trim())) {
            result.setFileResourceIds(item.fileResourceIds);
        }
        return result;
    }


}
