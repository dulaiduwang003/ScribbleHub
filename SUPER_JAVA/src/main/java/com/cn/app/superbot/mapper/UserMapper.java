package com.cn.app.superbot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.app.superbot.entity.SuperUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * The interface SuperUser mapper.
 *
 * @author bdth
 */
@Mapper
public interface UserMapper extends BaseMapper<SuperUser> {


    @Delete("delete from super_user where email = #{email} ")
    int deleteRealById(@Param("email") String email);
}
