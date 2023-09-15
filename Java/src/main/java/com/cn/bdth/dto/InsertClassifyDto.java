package com.cn.bdth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加专题
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class InsertClassifyDto {

    @NotBlank(message = "专题名称不能为空")
    private String classifyName;

    @NotNull(message = "所属类型不能为空")
    private Long isType;

    @NotNull(message = "封面不能为空")
    private MultipartFile file;

}
