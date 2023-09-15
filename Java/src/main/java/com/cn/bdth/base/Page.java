package com.cn.bdth.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据 基类
 *
 * @author 时间海 @github dulaiduwang003
 */
@Data
@Accessors(chain = true)
@Getter
@Setter
public class Page<T> implements Serializable {
    private long pageNum;
    private long pageSize;
    private long total;
    private List<T> list;
}
