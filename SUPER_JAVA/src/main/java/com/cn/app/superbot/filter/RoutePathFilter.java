package com.cn.app.superbot.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.cache.RoleCache;
import com.cn.app.superbot.msg.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * The type configures.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Configuration
@Slf4j
public class RoutePathFilter {


    /**
     * Gets sa servlet filter.
     *
     * @return the sa servlet filter
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**").addExclude("/favicon.ico")
                .setAuth(obj -> {
                    SaRouter.notMatch("/auth/**", "/upload/**", "/chat/**", "/public/**").check(r -> StpUtil.checkLogin());
                    SaRouter.match("/order/**", r -> StpUtil.checkRoleOr(RoleCache.USER, RoleCache.ADMIN));
                    SaRouter.match("/function/**", r -> StpUtil.checkRoleOr(RoleCache.USER, RoleCache.ADMIN));
                    SaRouter.match("/user/**", r -> StpUtil.checkRoleOr(RoleCache.USER, RoleCache.ADMIN));
                    SaRouter.match("/server/**", r -> StpUtil.checkRole(RoleCache.ADMIN));
                })
                .setError(e -> {
                    if (e instanceof NotLoginException) {
                        return Result.error(MsgConstants.NOT_LOGGED_IN, 401);
                    }
                    return Result.error(MsgConstants.LOGIN_FAILED, 410);


                })
                .setBeforeAuth(r -> {
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Max-Age", "3600")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setServer("Zeus");
                    if (SaHolder.getRequest().getMethod().equals(HttpMethod.OPTIONS.toString())) {
                        SaRouter.back();
                    }

                });
    }
}
