package com.cn.app.superbot.api;

import com.cn.app.superbot.dto.OperationPolicyDto;
import com.cn.app.superbot.dto.ProductDto;
import com.cn.app.superbot.dto.ServerPolicyDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.OrderService;
import com.cn.app.superbot.service.ProductService;
import com.cn.app.superbot.service.ServerTacticsService;
import com.cn.app.superbot.service.UserService;
import com.cn.app.superbot.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Server control api.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
@Slf4j
public class ServerApi {

    /**
     * The Server service.
     */
    private final ServerTacticsService serverTacticsService;


    /**
     * The Product service.
     */
    private final ProductServiceImpl productServiceImpl;


    /**
     * The Product service.
     */
    private final ProductService productService;


    /**
     * The Order service.
     */
    private final OrderService orderService;


    /**
     * The User service.
     */
    private final UserService userService;


    /**
     * Cud product result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/cud/product", name = "上架 下架 删除 修改", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result cudProduct(@RequestBody @Validated ProductDto dto) {
        try {
            productServiceImpl.action(dto, dto.getType());
            return Result.ok();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * Gets user info.
     *
     * @return the user info
     */
    @PostMapping(value = "/put/operation", name = "添加运营参数", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result putOperationStrategy(@RequestBody OperationPolicyDto dto) {
        try {
            return Result.data(serverTacticsService.writeOperationConfigure(dto));
        } catch (CustomException e) {
            log.error(e.getMessage());
            return Result.error();
        }
    }

    /**
     * Gets user info.
     *
     * @return the user info
     */
    @GetMapping(value = "/get/operation", name = "获取运营参数", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getOperationStrategy() {
        try {
            return Result.data(serverTacticsService.getOperationConfigure());
        } catch (CustomException e) {
            log.error(e.getMessage());
            return Result.error();
        }
    }

    /**
     * Gets user info.
     *
     * @return the user info
     */
    @GetMapping(value = "/get/server", name = "获取服务器参数", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getServerStrategy() {
        try {
            return Result.data(serverTacticsService.getServerConfigure());
        } catch (CustomException e) {
            log.error(e.getMessage());
            return Result.error();
        }
    }

    /**
     * Gets user info.
     *
     * @return the user info
     */
    @PostMapping(value = "/put/server", name = "覆写服务器参数配置", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result putServerStrategy(@RequestBody ServerPolicyDto dto) {
        try {
            return Result.data(serverTacticsService.writeServerConfigure(dto));
        } catch (CustomException e) {
            log.error(e.getMessage());
            return Result.error();
        }
    }


    /**
     * Gets orders.
     *
     * @param pageNum the page num
     * @return the orders
     */
    @GetMapping(value = "/page/orders/{pageNum}", name = "获取所有订单", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getOrderPage(@PathVariable Integer pageNum) {
        try {
            return Result.data(orderService.getOrderPage(pageNum));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * Gets orders.
     *
     * @param pageNum the page num
     * @return the orders
     */
    @GetMapping(value = "/page/product/{pageNum}", name = "获取商品分页", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getProductPage(@PathVariable Integer pageNum) {
        try {
            return Result.data(productService.getProductPage(pageNum));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }


    /**
     * Gets user page.
     *
     * @param pageNum the page num
     * @return the user page
     */
    @GetMapping(value = "/page/user/{pageNum}", name = "获取用户分页", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getUserPage(@PathVariable Integer pageNum) {
        try {
            return Result.data(userService.getUserPage(pageNum));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }


}
