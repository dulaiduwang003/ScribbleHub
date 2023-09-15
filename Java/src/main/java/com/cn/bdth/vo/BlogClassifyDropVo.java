package com.cn.bdth.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Setter
@EqualsAndHashCode
@ToString
@Getter
@Accessors(chain = true)
public class BlogClassifyDropVo implements Serializable {


    private String classifyName;


    private Long seaClassifyId;
}
