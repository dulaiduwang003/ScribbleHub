package com.cn.app.superbot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type SuperFavorite dto.
 */
@Getter
@Setter
@ToString
public class FavoriteDeleteDto {

    /**
     * The's.
     */
    @NotNull(message = "收藏标识不能为空")
   private Long id;

}
