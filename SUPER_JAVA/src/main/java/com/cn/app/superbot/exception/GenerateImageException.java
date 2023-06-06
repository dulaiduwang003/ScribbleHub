package com.cn.app.superbot.exception;


import lombok.Data;

/**
 * The type Custom exception.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SuppressWarnings("all")
@Data
public class GenerateImageException extends RuntimeException {

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
    public GenerateImageException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
