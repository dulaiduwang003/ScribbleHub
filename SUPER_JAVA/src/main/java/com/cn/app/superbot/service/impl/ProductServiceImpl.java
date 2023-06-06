package com.cn.app.superbot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.app.superbot.dto.ProductDto;
import com.cn.app.superbot.entity.SuperProduct;
import com.cn.app.superbot.mapper.ProductMapper;
import com.cn.app.superbot.service.CudAbstract;
import com.cn.app.superbot.service.ProductService;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.vo.ProductPageVo;
import com.cn.app.superbot.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Product service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl extends CudAbstract<ProductDto> implements ProductService {


    /**
     * The Product mapper.
     */
    private final ProductMapper productMapper;

    /**
     * Gets products.
     *
     * @return the products
     */
    @Override
    public List<ProductVo> getProducts() {
        return BeanUtils.copyArrayProperTies(productMapper.selectList(new QueryWrapper<SuperProduct>()
                .lambda()
                .select(SuperProduct::getId, SuperProduct::getPrice, SuperProduct::getFrequency)
        ), ProductVo.class);
    }

    /**
     * Gets product page vo.
     *
     * @param pageNum the page num
     * @return the product page vo
     */
    @Override
    public IPage<ProductPageVo> getProductPage(final Integer pageNum) {
        Page<SuperProduct> selectPage = productMapper.selectPage(new Page<>(pageNum, 5), new QueryWrapper<SuperProduct>()
                .lambda()
                .select(
                        SuperProduct::getId,
                        SuperProduct::getCreatedTime,
                        SuperProduct::getName,
                        SuperProduct::getPrice,
                        SuperProduct::getFrequency
                ).orderByDesc(SuperProduct::getCreatedTime)
        );
        return selectPage.convert(s -> BeanUtils.copyClassProperTies(s,ProductPageVo.class));
    }

    /**
     * Insert.
     *
     * @param dto the dto
     */
    @Override
    protected void insert(final ProductDto dto) {
        productMapper.insert(BeanUtils.copyClassProperTies(dto, SuperProduct.class));
    }

    /**
     * Delete.
     *
     * @param dto the dto
     */
    @Override
    protected void delete(final ProductDto dto) {
        productMapper.deleteById(dto.getId());
    }

    /**
     * Update.
     *
     * @param dto the dto
     */
    @Override
    protected void update(final ProductDto dto) {
        productMapper.updateById(BeanUtils.copyClassProperTies(dto, SuperProduct.class));
    }
}
