package com.cn.app.superbot.api;

import com.cn.app.superbot.dto.DeleteCreationDto;
import com.cn.app.superbot.dto.FavoriteAddDto;
import com.cn.app.superbot.dto.FavoriteDeleteDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.OrderService;
import com.cn.app.superbot.service.UserOperateService;
import com.cn.app.superbot.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type User api.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    /**
     * The SuperUser service.
     */
    private final UserOperateService userOperateService;


    /**
     * The Order service.
     */
    private final OrderService orderService;


    /**
     * Gets order consume page.
     *
     * @param pageNum the page num
     * @return the order consume page
     */
    @GetMapping(value = "/page/orders/{pageNum}", name = "我的订单", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getOrderConsumePage(@PathVariable Integer pageNum) {
        try {
            return Result.data(orderService.getOrderConsumePage(pageNum));
        } catch (CustomException e) {
            return Result.error();
        }
    }


    /**
     * Gets individual creations.
     *
     * @return the individual creations
     */
    @GetMapping(value = "/individual/picture", name = "获取我的作品", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getIndividualCreations() {
        return Result.data(userOperateService.getCreation());
    }


    /**
     * Get individual creations a result.
     *
     * @return the result
     */
    @PostMapping(value = "/delete/picture", name = "删除我的作品", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteCreation(@Validated @RequestBody DeleteCreationDto dto) {
        try {
            userOperateService.deleteCreation(dto.getId());
            return Result.ok();
        } catch (CustomException e) {
            return Result.error();
        }
    }

    /**
     * Gets server config.
     *
     * @return the server config
     */
    @GetMapping(value = "/get/user-info", name = "获取我的登录信息", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getUserInfo() {
        try {
            return Result.data(userOperateService.getUserInfo(UserUtils.userIdToLong()));
        } catch (CustomException e) {
            log.error("failed to get user data");
            return Result.error();
        }
    }

    /**
     * My favorites result.
     *
     * @return the result
     */
    @GetMapping(value = "/get/favorites", name = "获取我的收藏对话", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result myFavorites() {
        try {
            return Result.data(userOperateService.getMyFavoriteColumns());
        } catch (CustomException e) {
            log.error("failed to obtain favorites");
            return Result.error();
        }
    }

    /**
     * Control collection result.
     *
     * @return the result
     */
    @PostMapping(value = "/add/favorite", name = "添加对话至收藏", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result controlFavorite(@RequestBody @Validated FavoriteAddDto dto) {
        try {
            return Result.data(userOperateService.addFavorite(dto));
        } catch (CustomException e) {
            return Result.error();
        }
    }


    /**
     * Delete favorite result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/delete/favorite", name = "删除某段收藏", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteFavorite(@RequestBody @Validated FavoriteDeleteDto dto) {
        try {
            userOperateService.deleteFavorite(dto.getId());
            return Result.ok();
        } catch (CustomException e) {
            return Result.error();
        }
    }


}
