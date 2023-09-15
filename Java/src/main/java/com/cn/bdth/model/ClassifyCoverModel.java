package com.cn.bdth.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 专题带封面
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class ClassifyCoverModel {

    private Long seaResourceId;

    private String classifyName;

    private String cover;

    private String target;


}
