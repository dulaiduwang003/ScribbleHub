package com.cn.bdth.exceptions;


import lombok.Data;

/**
 * 操作文件类异常处理
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class UploadException extends RuntimeException {

    private String message;

    private Integer code;


    public UploadException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public UploadException(final String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

    public UploadException() {

    }
}
