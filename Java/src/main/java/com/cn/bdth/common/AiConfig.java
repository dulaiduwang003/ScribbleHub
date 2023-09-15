
package com.cn.bdth.common;

import com.cn.bdth.model.BitoModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * BOT配置类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AiConfig {

    /**
     * bot参数模型
     */
    private BitoModel bitoModel;


    /**
     * 绘图URL
     */
    private String sdUrl;


    /**
     * 作者名称
     */
    private String authorName;


    /**
     * bot名称
     */
    private String botName;

    /**
     * NewBingCookie
     */
    private String bingCookie;

    /**
     * 代理ip
     */
    private String proxyIp = "127.0.0.1";

    /**
     * 代理端口
     */
    private Integer proxyPort = 7892;
}
