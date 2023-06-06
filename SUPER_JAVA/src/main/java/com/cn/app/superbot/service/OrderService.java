package com.cn.app.superbot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.app.superbot.vo.AdminOrderPageVo;
import com.cn.app.superbot.vo.AlipayPayCodeVo;
import com.cn.app.superbot.vo.UserOrderPageVo;
import com.cn.app.superbot.vo.UserPageVo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The interface Order service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
public interface OrderService {


    /**
     * Alipay qr code pay alipay pay code vo.
     *
     * @param productId the product id
     * @return the alipay pay code vo
     */
    AlipayPayCodeVo alipayQRCodePay(final Long productId);


    /**
     * Alipay pullback string.
     *
     * @param request the request
     * @return the string
     */
    String alipayPullback(final HttpServletRequest request);


    /**
     * Payment status string.
     *
     * @param orderNo the order no
     * @return the string
     */
    String paymentStatus(final String orderNo);

    /**
     * Queue order check.
     *
     * @param orderNo the order no
     */
    void queueOrderCheck(final String orderNo);


    /**
     * Gets order consume page.
     *
     * @param pageNum the page num
     * @return the order consume page
     */
    IPage<UserOrderPageVo> getOrderConsumePage(final Integer pageNum);


    /**
     * Gets orders.
     *
     * @param pageNum the page num
     * @return the orders
     */
    IPage<AdminOrderPageVo> getOrderPage(final Integer pageNum);


    /**
     * Gets orders.
     *
     * @param pageNum the page num
     * @return the orders
     */
    IPage<UserPageVo> getUserPage(final Integer pageNum);

}
