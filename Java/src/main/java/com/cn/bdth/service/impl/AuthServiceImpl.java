package com.cn.bdth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.bdth.constants.auth.AuthConstant;
import com.cn.bdth.entity.SeaUser;
import com.cn.bdth.mapper.SeaUserMapper;
import com.cn.bdth.service.AuthService;
import com.cn.bdth.utils.IdGenerator;
import com.cn.bdth.utils.WeChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 登录授权业务处理类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SeaUserMapper seaUserMapper;

    private final WeChatUtils weChatUtils;

    private final IdGenerator idGenerator;

    @Value("#{'${admin}'.split(',')}")
    private Set<String> administrators;

    /**
     * 微信授权登录
     *
     * @param code the code
     * @return the string
     */
    @Override
    public String wechatAuthorizedLogin(final String code) {
        //获取微信用户ID
        final String openId = weChatUtils.getOpenId(code);
        //是否存在
        SeaUser seaUser = seaUserMapper.selectOne(new QueryWrapper<SeaUser>()
                .lambda()
                .eq(SeaUser::getOpenId, openId)
                .select(SeaUser::getSeaUserId, SeaUser::getOpenId)
        );
        //不存在则写入DB
        if (seaUser == null) {
            seaUser = new SeaUser()
                    //生成用户ID
                    .setSeaUserId(idGenerator.getSnowflakeId())
                    //写入OPENID
                    .setOpenId(openId);
            seaUserMapper.insert(seaUser);
        }
        //匹配是否为管理员用户
        boolean isAdmin = administrators.contains(seaUser.getOpenId());
        StpUtil.login(seaUser.getSeaUserId());
        //设置具体TOKEN Session权限
        StpUtil.getSession()
                .set(AuthConstant.ROLE, isAdmin ? AuthConstant.ADMIN : AuthConstant.USER)
                .set(AuthConstant.OPEN_ID, seaUser.getOpenId());
        // 返回TOKEN
        return StpUtil.getTokenValue();
    }

    /**
     * 注销登录
     */
    @Override
    public void logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
    }
}
