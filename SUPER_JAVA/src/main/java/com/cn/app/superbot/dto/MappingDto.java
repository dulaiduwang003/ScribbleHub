package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Mapping dto.
 */
@Getter
@Setter
@ToString
public class MappingDto {

    /**
     * The Prompt.
     */
    @NotBlank(message = "prompt参数不能为空")
    private String prompt;

}
