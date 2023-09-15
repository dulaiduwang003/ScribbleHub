package com.cn.bdth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新昵称
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class UpdateNickNameDto {

    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 3, max = 8, message = "长度应该在3到8之间")
    private String userName;
}
