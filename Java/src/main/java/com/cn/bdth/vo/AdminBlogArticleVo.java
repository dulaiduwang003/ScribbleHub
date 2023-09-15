package com.cn.bdth.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class AdminBlogArticleVo implements Serializable {

    private Long seaBlogId;

    private String title;


    private String cover;


    private String summary;


    private Long isRecommend;

    private LocalDateTime createdTime;

}
