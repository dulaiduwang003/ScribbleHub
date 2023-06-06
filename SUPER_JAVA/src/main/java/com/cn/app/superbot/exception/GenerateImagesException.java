package com.cn.app.superbot.exception;


import lombok.Data;

/**
 * The type Chat data exception.
 */

@Data
public class GenerateImagesException extends RuntimeException {


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
    public GenerateImagesException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
