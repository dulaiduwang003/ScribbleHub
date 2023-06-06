package com.cn.app.superbot.api;

import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.OrderService;
import com.cn.app.superbot.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Public api.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Slf4j
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicApi {


    /**
     * The Product service.
     */
    private final ProductService productService;


    /**
     * The Order service.
     */
    private final OrderService orderService;

    /**
     * Gets products.
     *
     * @return the products
     */
    @GetMapping(value = "/get/products", name = "渲染商品", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getProducts() {
        try {
            return Result.data(productService.getProducts());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * Alipay pullback.
     *
     * @param request the request
     */
    @PostMapping(value = "/callback/order", name = "支付宝授权回调地址", produces = MediaType.APPLICATION_JSON_VALUE)
    public String alipayPullback(HttpServletRequest request) {
        return orderService.alipayPullback(request);
    }

}
