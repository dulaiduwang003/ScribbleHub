package com.cn.app.superbot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.app.superbot.vo.ProductPageVo;
import com.cn.app.superbot.vo.ProductVo;

import java.util.List;

public interface ProductService {


    /**
     * Gets products.
     *
     * @return the products
     */
    List<ProductVo> getProducts();


    /**
     * Gets product page vo.
     *
     * @param pageNum the page num
     * @return the product page vo
     */
    IPage<ProductPageVo> getProductPage(final Integer pageNum);

}
