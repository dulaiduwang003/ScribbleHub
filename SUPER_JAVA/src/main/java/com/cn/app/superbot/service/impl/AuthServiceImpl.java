package com.cn.app.superbot.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.comment.IdGenerator;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.cache.AuthLoginCache;
import com.cn.app.superbot.constants.cache.AuthPrefixCache;
import com.cn.app.superbot.constants.cache.RoleCache;
import com.cn.app.superbot.dto.UserLoginDto;
import com.cn.app.superbot.dto.UserRegisterDto;
import com.cn.app.superbot.dto.VerificationCodeDto;
import com.cn.app.superbot.entity.SuperUser;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.exception.RegisterException;
import com.cn.app.superbot.exception.RegistrationCodeException;
import com.cn.app.superbot.mapper.UserMapper;
import com.cn.app.superbot.service.AuthService;
import com.cn.app.superbot.structure.OperateStructure;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.utils.RedisUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;


/**
 * The type Auth service.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
@SuppressWarnings("all")
public class AuthServiceImpl extends ServiceImpl<UserMapper, SuperUser> implements AuthService {
    /**
     * The Username.
     */
    @Value(value = "${spring.mail.username}")
    private String username;


    /**
     * The SuperUser mapper.
     */
    private final UserMapper userMapper;

    /**
     * The Id generator.
     */
    private final IdGenerator idGenerator;


    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;

    /**
     * The Mail sender.
     */
    private final JavaMailSender mailSender;


    /**
     * The Template engine.
     */
    private final TemplateEngine templateEngine;

    /**
     * SuperUser login string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public String userLogin(final UserLoginDto dto, final boolean isAdmin) {
        final String password = SaSecureUtil.md5(dto.getPassword());
        final SuperUser superUser = userMapper.selectOne(new QueryWrapper<SuperUser>()
                .lambda()
                .eq(SuperUser::getEmail, dto.getEmail())
                .eq(SuperUser::getPassword, password)
        );
        if (superUser != null) {
            StpUtil.login(superUser.getId(), SaLoginConfig
                    .setExtra(AuthPrefixCache.AUTH_ROLE, isAdmin ? RoleCache.ADMIN : RoleCache.USER));
        } else {
            throw new CustomException(MsgConstants.PWD_ERR, 400);
        }
        return StpUtil.getTokenValue();
    }


    /**
     * SuperUser register string.
     *
     * @param dto the dto
     */
    @Override
    public void userRegister(final UserRegisterDto dto) {
        final String code = dto.getCode();
        final Object value = redisUtils.getValue(AuthLoginCache.EMAIL_CODE + dto.getEmail());
        if (value != null && value.equals(code)) {
            SuperUser superUser = userMapper.selectOne(new QueryWrapper<SuperUser>()
                    .lambda().eq(SuperUser::getEmail, dto.getEmail())
                    .select(SuperUser::getId));
            if (superUser != null) {

                throw new RegisterException(MsgConstants.ACCOUNT_ALREADY_EXISTS_ERR, 400);
            }
            final OperateStructure operateStructure = ChatComment.operateStructure;
            superUser = BeanUtils.copyClassProperTies(dto, SuperUser.class);
            superUser.setPassword(SaSecureUtil.md5(superUser.getPassword()));

            userMapper.insert(superUser.setId(idGenerator.getSnowflakeId()).setFrequency(operateStructure.getUserFrequency()));
        } else {
            throw new RegisterException(MsgConstants.CODE_ERR, 400);
        }

    }

    /**
     * Obtain verification code string.
     *
     * @param dto the dto
     */
    @Override
    public void obtainVerificationCode(final VerificationCodeDto dto) {
        final String code = RandomStringUtils.random(6, true, true).toUpperCase();
        Context context = new Context();
        context.setVariable("code", code);
        String process = templateEngine.process("emailCode.html", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("SuperBot注册验证码");
            helper.setFrom(username);
            helper.setTo(dto.getEmail());
            helper.setSentDate(new Date());
            helper.setText(process, true);
        } catch (MessagingException e) {
            throw new RegistrationCodeException(MsgConstants.VERIFICATION_CODE_ERR, 400);
        }
        redisUtils.setValueTimeout(AuthLoginCache.EMAIL_CODE + dto.getEmail(), code, 300);
        mailSender.send(mimeMessage);

    }

    /**
     * We chat logout.
     */
    public void weChatLogout() {
        final boolean isLogin = StpUtil.isLogin();
        try {
            if (isLogin) {
                StpUtil.logout();
            }
        } catch (Exception e) {
            throw new CustomException(MsgConstants.LOGOUT_ERR, 500);
        }
    }
}
