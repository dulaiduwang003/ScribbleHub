package com.cn.app.superbot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.app.superbot.entity.SuperFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface SuperFavorite mapper.
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<SuperFavorite> {
}
