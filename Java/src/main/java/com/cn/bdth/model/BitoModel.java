
package com.cn.bdth.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * BOT HTTP模型
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class BitoModel {

    private int bitoUserId;

    private String email;

    private String ideName;

    private String prompt;

    private String uId;

    private Long wsId;

    private Boolean stream = true;

    private String requestId;

    private List<Context> context;

    private String authorization;

    private String sessionId;

    private String outputLanguage;

    @Data
    @Accessors(chain = true)
    public static class Context {
        private String question;

        private String answer;
    }
}
