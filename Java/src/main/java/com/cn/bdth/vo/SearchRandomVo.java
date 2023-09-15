package com.cn.bdth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class SearchRandomVo implements Serializable {

    private List<Collections> front;


    private List<Collections> backEnd;

    private List<Collections> middleware;

    private List<Collections> other;


    @Data
    public static class Collections {
        private Long seaBlogId;

        private String title;
    }
}
