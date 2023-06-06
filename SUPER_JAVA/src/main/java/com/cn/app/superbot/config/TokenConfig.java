package com.cn.app.superbot.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Token config.
 */
@Configuration
public class TokenConfig {

    /**
     * Gets stp logic jwt.
     *
     * @return the stp logic jwt
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}
