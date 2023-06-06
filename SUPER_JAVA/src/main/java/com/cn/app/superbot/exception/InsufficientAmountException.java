package com.cn.app.superbot.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The type Custom exception.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
public class InsufficientAmountException extends RuntimeException {

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
    public InsufficientAmountException(final String message, final Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
