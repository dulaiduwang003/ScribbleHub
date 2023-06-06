package com.cn.app.superbot.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * The type Alipay cache structure.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Data
@Accessors(chain = true)
public class AlipayCacheStructure {

    /**
     * The Url.
     */
    private String url;

    /**
     * The Created time.
     */
    private LocalDateTime createdTime;

    /**
     * The Id.
     */
    private String id;

    /**
     * The Price.
     */
    private Double price;

    /**
     * The Name.
     */
    private String name;
}
