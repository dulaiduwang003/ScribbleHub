package com.cn.app.superbot.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * The type Product vo.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
public class ProductVo {


    /**
     * The Id.
     */
    private Long id;
    /**
     * The Frequency.
     */
    private Long frequency;
    /**
     * The Price.
     */
    private BigDecimal price;


}
