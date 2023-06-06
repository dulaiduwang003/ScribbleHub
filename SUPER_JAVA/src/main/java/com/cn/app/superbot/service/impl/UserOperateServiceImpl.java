package com.cn.app.superbot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.cache.AuthPrefixCache;
import com.cn.app.superbot.dto.FavoriteAddDto;
import com.cn.app.superbot.entity.SuperCreation;
import com.cn.app.superbot.entity.SuperFavorite;
import com.cn.app.superbot.entity.SuperUser;
import com.cn.app.superbot.exception.CustomException;
import com.cn.app.superbot.mapper.CreationMapper;
import com.cn.app.superbot.mapper.FavoriteMapper;
import com.cn.app.superbot.mapper.UserMapper;
import com.cn.app.superbot.service.UserOperateService;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.utils.UserUtils;
import com.cn.app.superbot.vo.UserCreationVo;
import com.cn.app.superbot.vo.UserFavoriteVo;
import com.cn.app.superbot.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserOperateServiceImpl implements UserOperateService {


    /**
     * The SuperUser mapper.
     */
    private final UserMapper userMapper;


    /**
     * The SuperFavorite mapper.
     */
    private final FavoriteMapper favoriteMapper;


    /**
     * The Creation mapper.
     */
    private final CreationMapper creationMapper;


    /**
     * Delete creation.
     *
     * @param id the id
     */
    @Override
    public void deleteCreation(final Long id) {
        try {
            creationMapper.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(MsgConstants.SERVER_API_ERR, 500);
        }
    }

    /**
     * Gets creation.
     *
     * @return the creation
     */
    @Override
    public List<UserCreationVo> getCreation() {
        return BeanUtils.copyArrayProperTies(creationMapper.selectList(
                new QueryWrapper<SuperCreation>().lambda()
                        .eq(SuperCreation::getUserId, UserUtils.userIdToLong())
                        .select(SuperCreation::getUrl, SuperCreation::getPrompt, SuperCreation::getCreatedTime, SuperCreation::getId)
        ), UserCreationVo.class);
    }

    /**
     * Get user info user info vo.
     *
     * @return the user info vo
     */

    @Override
    public UserInfoVo getUserInfo(final long id) {
        final UserInfoVo userInfoVo = BeanUtils.copyClassProperTies(userMapper.selectOne(new QueryWrapper<SuperUser>().lambda().eq(SuperUser::getId, id).select(SuperUser::getFrequency).last("limit 1")), UserInfoVo.class);
        return  userInfoVo.setRole((String) StpUtil.getExtra(StpUtil.getTokenValueByLoginId(id),AuthPrefixCache.AUTH_ROLE));
    }

    /**
     * Deductions.
     *
     * @param id        the id
     * @param frequency the frequency
     */
    @Override
    public void deductions(final long id, final long frequency) {
        userMapper.update(null, new UpdateWrapper<SuperUser>().lambda().eq(SuperUser::getId, id).setSql("frequency = frequency -" + frequency));
    }

    /**
     * Increase.
     *
     * @param id        the id
     * @param frequency the frequency
     */
    @Override
    public void increase(final long id, final long frequency) {
        userMapper.update(null, new UpdateWrapper<SuperUser>().lambda().eq(SuperUser::getId, id).setSql("frequency = frequency +" + frequency));
    }

    /**
     * SuperFavorite conversations.
     *
     * @param dto the dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addFavorite(final FavoriteAddDto dto) {
        final SuperFavorite superFavorite = BeanUtils.copyClassProperTies(dto, SuperFavorite.class).setUserId(UserUtils.userIdToLong());
        favoriteMapper.insert(superFavorite);
        return superFavorite.getId();
    }

    /**
     * Delete favorite.
     *
     * @param id the id
     */
    @Override
    public void deleteFavorite(final long id) {
        favoriteMapper.deleteById(id);
    }

    /**
     * Gets my favorite columns.
     *
     * @return my favorite columns
     */
    @Override
    public List<UserFavoriteVo> getMyFavoriteColumns() {
        return favoriteMapper.selectList(new QueryWrapper<SuperFavorite>().lambda().eq(SuperFavorite::getUserId, UserUtils.userIdToLong()).select(SuperFavorite::getUserDialogue, SuperFavorite::getAiDialogue, SuperFavorite::getId, SuperFavorite::getCreatedTime)).parallelStream().map(superFavorite -> BeanUtils.copyClassProperTies(superFavorite, UserFavoriteVo.class)).sorted(Comparator.comparing(UserFavoriteVo::getId, Comparator.reverseOrder())).collect(Collectors.toList());
    }

}
