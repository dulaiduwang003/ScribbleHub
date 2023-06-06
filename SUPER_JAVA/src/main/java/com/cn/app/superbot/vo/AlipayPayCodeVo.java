package com.cn.app.superbot.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Alipay pay code vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@Accessors(chain = true)
public class AlipayPayCodeVo implements Serializable {

    /**
     * The I'd.
     */
    private String id;


    /**
     * The Name.
     */
    private String name;


    /**
     * The Created time.
     */
    private LocalDateTime createdTime;


    /**
     * The Price.
     */
    private Double price;


    /**
     * The Qr code.
     */
    private String qrCode;



}
