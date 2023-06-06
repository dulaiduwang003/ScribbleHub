package com.cn.app.superbot.service;

import com.cn.app.superbot.dto.FavoriteAddDto;
import com.cn.app.superbot.vo.UserCreationVo;
import com.cn.app.superbot.vo.UserFavoriteVo;
import com.cn.app.superbot.vo.UserInfoVo;

import java.util.List;

/**
 * The interface SuperUser service.
 *
 * @author bdth
 */
public interface UserOperateService {


    /**
     * Delete creation.
     */
    void deleteCreation(final Long id);


    /**
     * Gets creation.
     *
     * @return the creation
     */
    List<UserCreationVo> getCreation();


    /**
     * Gets user info.
     *
     * @param id the id
     * @return the user info
     */
    UserInfoVo getUserInfo(final long id);


    /**
     * The constant deductions.
     *
     * @param id        the id
     * @param frequency the frequency
     */
    void deductions(final long id, long frequency);


    /**
     * Increase.
     *
     * @param id        the id
     * @param frequency the frequency
     */
    void increase(final long id, final long frequency);


    /**
     * SuperFavorite control.
     *
     * @param dto the dto
     */
    long addFavorite(final FavoriteAddDto dto);


    /**
     * Delete favorite.
     *
     * @param id the id
     */
    void deleteFavorite(final long id);


    /**
     * Gets my favorite columns.
     *
     * @return my favorite columns
     */
    List<UserFavoriteVo> getMyFavoriteColumns();

}
