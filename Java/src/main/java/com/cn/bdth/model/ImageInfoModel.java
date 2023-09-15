package com.cn.bdth.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图片模型
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public  class ImageInfoModel {

    private String prompt;

    private Long seaImageId;

    private LocalDateTime createdTime;

}
