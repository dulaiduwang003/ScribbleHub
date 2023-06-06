package com.cn.app.superbot.exception;


import lombok.Data;

/**
 * The type Order exception.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@SuppressWarnings("all")
@Data
public class OrderException extends RuntimeException {

    /**
     * The Message.
     */
    private String message;

    /**
     * The Code.
     */
    private Integer code;


    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     * @param code    the code
     */
    public OrderException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
