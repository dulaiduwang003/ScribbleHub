package com.cn.bdth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Accessors(chain = true)
@Data
public class UploadResourceVo implements Serializable {

    private Long seaResourceId;


    private String uri;
}
