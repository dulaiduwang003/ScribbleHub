package com.cn.app.superbot.dto;

import com.cn.app.superbot.model.GptFourModel;
import com.cn.app.superbot.model.GptThreeModel;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ChatGPTDto {

    /**
     * The Messages.
     */
    @NotEmpty(message = "消息数据不能为空")
    private List<Messages> messages;


    /**
     * Convert to gpt three-model gpt three model.
     *
     * @param item the item
     * @return the gpt three model
     */
    public static GptThreeModel convertToGptThreeModel(ChatGPTDto item) {
        if (item == null) {
            return null;
        }
        GptThreeModel result = new GptThreeModel();

        List<Messages> messages = item.getMessages();
        result.setMessages(messages.stream().map(ChatGPTDto::convertToThreeMessages).collect(Collectors.toList()));

        return result;
    }

    /**
     * Convert to bing string.
     *
     * @param item the item
     * @return the string
     */
    public static String convertToBingString(ChatGPTDto item) {
        if (item == null) {
            return null;
        }
        return item.messages.get(item.messages.size() - 1).getContent();
    }

    /**
     * Convert to gpt four model gpt four model.
     *
     * @param item the item
     * @return the gpt four model
     */
    public static GptFourModel convertToGptFourModel(final ChatGPTDto item) {
        if (item == null) {
            return null;
        }
        GptFourModel result = new GptFourModel();

        List<Messages> messages = item.getMessages();
        if (messages == null) {
            result.setMessages(null);
        } else {
            result.setMessages(messages.stream().map(ChatGPTDto::convertToFourMessages).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Convert to four messages gpt four model . messages.
     *
     * @param item the item
     * @return the gpt four model . messages
     */
    public static GptFourModel.Messages convertToFourMessages(Messages item) {
        if (item == null) {
            return null;
        }
        GptFourModel.Messages result = new GptFourModel.Messages();
        result.setRole(item.getRole());
        result.setContent(item.getContent());
        return result;
    }

    /**
     * Convert to messages gpt three models. Messages.
     *
     * @param item the item
     * @return the gpt three model. messages
     */
    public static GptThreeModel.Messages convertToThreeMessages(Messages item) {
        if (item == null) {
            return null;
        }
        GptThreeModel.Messages result = new GptThreeModel.Messages();
        result.setRole(item.getRole());
        result.setContent(item.getContent());
        return result;
    }


    /**
     * The type Messages.
     *
     * @author bdth
     */
    @Data
    @Accessors(chain = true)
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
