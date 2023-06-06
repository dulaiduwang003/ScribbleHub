package com.cn.app.superbot.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Operation strategy vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class OperationStrategyVo implements Serializable {

    /**
     * The Alipay callback url.
     */
    private String alipayCallbackUrl;

    /**
     * The Alipay public.
     */
    private String alipayPublic;

    /**
     * The Alipay private.
     */
    private String alipayPrivate;

    /**
     * The Alipay appid.
     */
    private String alipayAppid;

    /**
     * The Bing frequency.
     */
    private Long bingFrequency;

    /**
     * The Chat three frequency.
     */
    private Long chatThreeFrequency;


    /**
     * The Chat four frequency.
     */
    private Long chatFourFrequency;


    /**
     * The mapping frequency.
     */
    private Long mappingFrequency;

    /**
     * The Memory.
     */
    private Long memory;

    /**
     * The User frequency.
     */
    private Long userFrequency;

}
