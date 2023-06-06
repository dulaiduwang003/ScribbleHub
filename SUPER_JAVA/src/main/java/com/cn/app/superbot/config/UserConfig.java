package com.cn.app.superbot.config;

import lombok.Data;

/**
 * 运营管理配置
 * The type SuperUser config.
 *
 * @author bdth
 */
@Data
public class UserConfig {

    /**
     * 每天赠送次数
     * The Give frequency.
     */
    private Integer giveFrequency;

    /**
     * 用户观看广告获取次数
     * The Video reward.
     */
    private Integer videoReward;
}
