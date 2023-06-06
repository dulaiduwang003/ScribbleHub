package com.cn.app.superbot.dto;

import com.cn.app.superbot.model.GptFourModel;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CreationsDto {

    /**
     * The Messages.
     */
    @NotEmpty(message = "消息数据不能为空")
    private List<GptFourModel.Messages> messages;


}
