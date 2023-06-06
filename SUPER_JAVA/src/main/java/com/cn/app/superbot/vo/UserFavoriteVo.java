package com.cn.app.superbot.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The type SuperUser favorite vo.
 */
@Data
public class UserFavoriteVo implements Serializable {


    /**
     * The id.
     */
    private Long id;

    /**
     * The SuperUser dialogue.
     */
    private String userDialogue;
    /**
     * The Ai dialogue.
     */
    private String aiDialogue;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;
}
