package com.cn.app.superbot.api;

import com.cn.app.superbot.dto.PaymentStatusDto;
import com.cn.app.superbot.dto.PlaceOrderDto;
import com.cn.app.superbot.exception.OrderException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Order api.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderApi {


    /**
     * The Order service.
     */
    private final OrderService orderService;


    /**
     * Alipay payment result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/alipay/order", name = "创建支付宝订单", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result alipayPayment(@RequestBody @Validated PlaceOrderDto dto) {
        try {
            return Result.data(orderService.alipayQRCodePay(dto.getId()));
        } catch (OrderException e) {
            e.printStackTrace();
            return Result.error();
        }
    }


    /**
     * Alipay status result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/pay/status", name = "订单支付状态", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result alipayStatus(@RequestBody @Validated PaymentStatusDto dto) {
        try {
            return Result.data(orderService.paymentStatus(dto.getOrderNo()));
        } catch (OrderException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }



}
