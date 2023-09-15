package com.cn.bdth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.bdth.entity.SeaUser;
import com.cn.bdth.model.UserInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户映射
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Mapper
public interface SeaUserMapper extends BaseMapper<SeaUser> {


    /**
     * 获取用户信息 带头像
     *
     * @param seaUserId the sea user id
     * @return the list
     */
    UserInfoModel selectUserInfoBySeaUserId( @Param("seaUserId") Long seaUserId);

}
