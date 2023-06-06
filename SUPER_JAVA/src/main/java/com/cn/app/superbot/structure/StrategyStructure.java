package com.cn.app.superbot.structure;

import com.cn.app.superbot.enums.MappingEnum;
import com.cn.app.superbot.enums.ServerPolicyEnum;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


/**
 * The type Server config.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Data
public class StrategyStructure {
    /**
     * 1 overseas 2 domestic 3 thirdParty
     */
    public ServerPolicyEnum choose;

    /**
     * The Proxy.
     */
    public Proxy proxy;

    /**
     * The Third custom.
     */
    public Custom custom;


    /**
     * The Cookie.
     */
    public Bing bing;


    /**
     * direct connection to the official website
     */
    public Official official;


    /**
     * The Mapping.
     */
    public Mapping mapping;

    /**
     * The type Proxy.
     */
    @Data
    @ToString
    public static class Proxy {

        /**
         * The Ip.
         */
        public String ip;

        /**
         * The Port.
         */
        public Integer port;


    }

    /**
     * custom configuration
     */
    @Data
    @Accessors(chain = true)
    public static class Custom {

        /**
         * The Base url.
         */
        public String baseUrl;

        /**
         * The Key.
         */
        public String key;


    }

    /**
     * The type Sd.
     */
    @Data
    @Accessors(chain = true)
    public static class Mapping {

        /**
         * The Mj url.
         */
        public String mjUrl;


        /**
         * The Choice.
         */
        public MappingEnum choice;


        /**
         * SD配置
         * The SD URL.
         */
        public String sdUrl;


    }

    /**
     * The official chat gpt mode
     */
    @Data
    @Accessors(chain = true)
    public static class Official {

        /**
         * The Base url.
         */
        public String baseUrl;

        /**
         * official open key
         */
        public String key;

    }

    /**
     * The type Bing.
     */
    @Data
    @Accessors(chain = true)
    public static class Bing {

        /**
         * new bing
         */
        public String cookie;

    }
}
