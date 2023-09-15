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
@EqualsAndHashCode
@ToString
@Getter
@Accessors(chain = true)
public class BlogRankClassifyVo implements Serializable {


    private Long seaBlogId;


    private Long seaUserId;


    private Long seaResourceId;

    private String uri;

    private String title;


    private String summary;


    private String userName;


    private String avatar;


    private LocalDateTime createdTime;


    private Double Reading;



}
