package com.cn.bdth.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.bdth.constants.auth.AuthConstant;
import com.cn.bdth.msg.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * 路由拦截器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
public class RoutePathFilter {


    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**").addExclude("/favicon.ico")
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .notMatch("/auth/**", "/upload/**", "/bing/**","/gpt/**",  "/public/**")
                            .check(r -> StpUtil.checkLogin());
                    SaRouter.match("/admin/**", r -> StpUtil.checkRole(AuthConstant.ADMIN));
                    SaRouter.match("/function/**", r -> StpUtil.checkRoleOr(AuthConstant.USER, AuthConstant.ADMIN));
                }).setError(e -> Result.error("登录信息已过期,请重新登录", 401))
                .setBeforeAuth(r -> {
                    SaHolder.getResponse()
                            .setServer("sa-server")
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            .setHeader("X-Content-Type-Options", "nosniff");
                    if (SaHolder.getRequest().getMethod().equals(HttpMethod.OPTIONS.toString())) {
                        SaRouter.back();
                    }
                });
    }
}
