package com.cn.app.superbot.api;

import cn.dev33.satoken.stp.StpUtil;
import com.cn.app.superbot.dto.UserLoginDto;
import com.cn.app.superbot.dto.UserRegisterDto;
import com.cn.app.superbot.dto.VerificationCodeDto;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.exception.RegisterException;
import com.cn.app.superbot.exception.RegistrationCodeException;
import com.cn.app.superbot.msg.Result;
import com.cn.app.superbot.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录服务API
 * The type Auth api.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {


    /**
     * 登录逻辑业务
     * The Auth service.
     */
    private final AuthService authService;

    /**
     * 邮箱登录
     * Email login result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/email/login", name = "email login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result emailLogin(@RequestBody @Validated UserLoginDto dto) {
        try {
            
            return Result.data(authService.userLogin(dto, false));
        } catch (CustomException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * 邮箱注册
     * Email register result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/email/register", name = "email register", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result emailRegister(@RequestBody @Validated UserRegisterDto dto) {
        try {
            authService.userRegister(dto);
            return Result.ok();
        } catch (RegisterException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * 获取验证码
     * Generate verification code result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/code/register", name = "verification code", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result generateVerificationCode(@RequestBody @Validated VerificationCodeDto dto) {
        try {
            authService.obtainVerificationCode(dto);
            return Result.ok();
        } catch (RegistrationCodeException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }


    /**
     * 管理员服务登录
     * Server login result.
     *
     * @param dto the dto
     * @return the result
     */
    @PostMapping(value = "/server/login", name = "background login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result serverLogin(@RequestBody @Validated UserLoginDto dto) {
        try {
            return Result.data(authService.userLogin(dto, true));
        } catch (CustomException e) {
            return Result.error(e.getMessage(), e.getCode());
        }
    }

    /**
     * 退出登录
     * We chat a logout result.
     *
     * @return the result
     */
    @PostMapping(value = "/wx/logout", name = "sign out", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result weChatLogout() {
        try {
            if (StpUtil.isLogin()) {
                // user exists
                authService.weChatLogout();
            }
            return Result.ok();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }
}
