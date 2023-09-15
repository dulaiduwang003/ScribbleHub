package com.cn.bdth.dto;

import com.cn.bdth.model.BitoModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Bot聊天
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AiChatDto {

    private String prompt;

    private List<BitoModel.Context> context;

}
