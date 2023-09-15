package com.cn.bdth.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户信息模型
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UserInfoModel {

    private Long seaResourceId;

    private String userName;

    private String avatar;

    private String openId;

    private String target;

}
