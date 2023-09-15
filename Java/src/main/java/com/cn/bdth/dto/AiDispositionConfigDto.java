package com.cn.bdth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * BOT 以及绘图配置
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AiDispositionConfigDto {

    @NotNull(message = "bitoUserId不能为空")
    private Long bitoUserId;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "email邮箱格式错误")
    private String email;

    @NotBlank(message = "ideName环境不能为空")
    private String ideName;

    @NotBlank(message = "uId环境不能为空")
    private String uId;

    @NotNull(message = "wsId环境不能为空")
    private Long wsId;

    @NotBlank(message = "requestId环境不能为空")
    private String requestId;

    @NotBlank(message = "sessionId环境不能为空")
    private String sessionId;

    @NotBlank(message = "回复语言不能为空")
    private String outputLanguage;

    @NotBlank(message = "authorization不能为空")
    private String authorization;

    @NotBlank(message = "stable diffusion API 不能为空")
    private String sdUrl;

    @NotBlank(message = "机器人昵称不能为空")
    private String botName;

    @NotBlank(message = "作者昵称不能为空")
    private String authorName;

    @NotNull(message = "代理端口不能为空")
    private Integer proxyPort;

    @NotBlank(message = "代理IP不能为空")
    private String proxyIp;

    @NotBlank(message = "BingCookie不能为空")
    private String bingCookie;


}
