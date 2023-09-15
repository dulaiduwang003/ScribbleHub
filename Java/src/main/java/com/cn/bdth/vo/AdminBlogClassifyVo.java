package com.cn.bdth.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 *
 *  */
@Data
public class AdminBlogClassifyVo implements Serializable {

    private Long seaClassifyId;

    private String classifyName;


    private String cover;

    private LocalDateTime createdTime;

}
