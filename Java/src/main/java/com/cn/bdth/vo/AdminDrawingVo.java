package com.cn.bdth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AdminDrawingVo implements Serializable {

    private Long seaImageId;

    private String isPublic;

    private String imageUrl;
}
