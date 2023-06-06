package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type SuperFavorite dto.
 */
@Getter
@Setter
@ToString
public class FavoriteAddDto {

    /**
     * The SuperUser dialogue.
     */
    @NotBlank(message = "用户对话内容不能为空")
    private String userDialogue;

    /**
     * The Ai dialogue.
     */
    @NotBlank(message = "Ai对话不能为空")
    private String aiDialogue;


}
