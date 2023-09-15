package com.cn.bdth.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
public class BlogClassifyArticleVo implements Serializable {

    private String classifyName;


    private String cover;


    private List<BlogRankClassifyVo> list;
}
