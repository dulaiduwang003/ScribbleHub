package com.cn.app.superbot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.app.superbot.dto.UserLoginDto;
import com.cn.app.superbot.dto.UserRegisterDto;
import com.cn.app.superbot.dto.VerificationCodeDto;
import com.cn.app.superbot.entity.SuperUser;

/**
 * The interface Mini program user service.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
public interface AuthService extends IService<SuperUser> {


    /**
     * SuperUser register string.
     *
     * @param dto the dto
     */
    void userRegister(final UserRegisterDto dto);


    /**
     * SuperUser login string.
     *
     * @param dto the dto
     * @return the string
     */
    String userLogin(final UserLoginDto dto, final boolean isAdmin);


    /**
     * Obtain verification code string.
     *
     * @param dto the dto
     */
    void obtainVerificationCode(final VerificationCodeDto dto);


    /**
     * We chat logout.
     */
    void weChatLogout();


}
