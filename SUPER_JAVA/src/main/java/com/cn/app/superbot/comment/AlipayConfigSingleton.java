package com.cn.app.superbot.comment;

import com.alipay.api.AlipayConfig;

/**
 * The enum Alipay config singleton.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
public enum AlipayConfigSingleton {
    INSTANCE;
    private final AlipayConfig alipayConfig;
    AlipayConfigSingleton() {
        alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
        alipayConfig.setFormat("json");
        alipayConfig.setCharset("UTF8");
        alipayConfig.setSignType("RSA2");
    }

    /**
     * Gets alipay config.
     *
     * @return the alipay config
     */
    public AlipayConfig getAlipayConfig() {
        return alipayConfig;
    }
}
