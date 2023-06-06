package com.cn.app.superbot.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Chat data exception.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatDataException extends RuntimeException {


    /**
     * The Message.
     */
    private String message;

    /**
     * The SuperCode.
     */
    private Integer code;


    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     * @param code    the code
     */
    public ChatDataException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
