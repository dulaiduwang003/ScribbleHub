package com.cn.bdth.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class BlogCommentVo implements Serializable {

    private Long seaCommentId;

    private String commentContent;

    private String userName;

    private LocalDateTime createdTime;


    private String avatar;


    private Boolean isSmall = false;


    private Boolean isDeleted = false;

}
