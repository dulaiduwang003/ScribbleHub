package com.cn.bdth.exceptions;


import lombok.Data;

/**
 * BOT类异常处理
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@SuppressWarnings("all")
@Data
public class ChatException extends RuntimeException {

    private String message;

    private Integer code;


    public ChatException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ChatException() {

    }
}
