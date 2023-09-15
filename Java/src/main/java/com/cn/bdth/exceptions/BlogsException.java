package com.cn.bdth.exceptions;


import lombok.Data;

/**
 * 文章类异常处理
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class BlogsException extends RuntimeException {

    private String message;

    private Integer code;


    public BlogsException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public BlogsException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }


    public BlogsException() {

    }
}
