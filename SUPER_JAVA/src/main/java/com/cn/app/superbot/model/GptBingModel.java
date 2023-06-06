
package com.cn.app.superbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * The type Gpt alpha model.
 */
@Data
@Accessors(chain = true)
public class GptBingModel {



    /**
     * The Messages.
     */
    private List<Messages> messages;

    /**
     * The type Messages.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    @Data
    public static class Messages {

        /**
         * The RoleCache.
         */
        private String role;

        /**
         * The Content.
         */
        private String content;
    }

}
