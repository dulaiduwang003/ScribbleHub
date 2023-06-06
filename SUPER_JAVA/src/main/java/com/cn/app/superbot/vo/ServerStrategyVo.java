package com.cn.app.superbot.vo;

import com.cn.app.superbot.enums.ServerPolicyEnum;
import com.cn.app.superbot.structure.StrategyStructure;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Server vo.
 */
@Data
public class ServerStrategyVo implements Serializable {
    /**
     * 1 overseas 2 domestic 3 thirdParty
     */
    public ServerPolicyEnum choose;

    /**
     * The Proxy.
     */
    public StrategyStructure.Proxy proxy;

    /**
     * The Third custom.
     */
    public StrategyStructure.Custom custom;


    /**
     * The Cookie.
     */
    public StrategyStructure.Bing bing;


    /**
     * direct connection to the official website
     */
    public StrategyStructure.Official official;


    /**
     * The Sd
     */
    public StrategyStructure.Mapping mapping;
}
