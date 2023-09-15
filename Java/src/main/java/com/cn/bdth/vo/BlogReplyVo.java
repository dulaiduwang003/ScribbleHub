package com.cn.bdth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class BlogReplyVo implements Serializable {

    private Long seaReplyId;

    private String replyContent;

    private Long reciprocityId;


    private String replyName;

    private LocalDateTime createdTime;

    private String userName;

    private String avatar;


    private Boolean isDeleted = false;


}
