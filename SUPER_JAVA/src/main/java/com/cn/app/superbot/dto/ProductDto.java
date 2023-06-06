package com.cn.app.superbot.dto;

import com.cn.app.superbot.enums.CudEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Product dto.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Getter
@Setter
@ToString
public class ProductDto {

    /**
     * The Cud enum.
     */
    private CudEnum type;

    /**
     * The Id.
     */
    private Long id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Frequency.
     */
    private Long frequency;
    /**
     * The Price.
     */
    private Double price;


}
