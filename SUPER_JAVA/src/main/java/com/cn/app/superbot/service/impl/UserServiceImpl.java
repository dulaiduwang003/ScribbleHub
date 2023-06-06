package com.cn.app.superbot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.app.superbot.entity.SuperUser;
import com.cn.app.superbot.mapper.UserMapper;
import com.cn.app.superbot.service.UserService;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.vo.UserPageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type User service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
@SuppressWarnings("all")
public class UserServiceImpl extends ServiceImpl<UserMapper, SuperUser> implements UserService {

    /**
     * The SuperUser mapper.
     */
    private final UserMapper userMapper;

    /**
     * Gets user page.
     *
     * @param pageNum the page num
     * @return the user page
     */
    @Override
    public IPage<UserPageVo> getUserPage(Integer pageNum) {
        Page<SuperUser> selectPage = userMapper.selectPage(new Page<>(pageNum, 5), new QueryWrapper<SuperUser>()
                .lambda()
                .select(
                        SuperUser::getCreatedTime,
                        SuperUser::getPassword,
                        SuperUser::getEmail,
                        SuperUser::getFrequency
                ).orderByDesc(SuperUser::getCreatedTime)
        );
        final Long aLong = userMapper.selectCount(null);
        return selectPage.convert(s -> {
                    return BeanUtils.copyClassProperTies(s, UserPageVo.class).setNumber(aLong);
                }
        );
    }
}
