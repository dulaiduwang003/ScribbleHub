package com.cn.app.superbot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.app.superbot.entity.SuperOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Order mapper.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Mapper
public interface OrderMapper extends BaseMapper<SuperOrder> {
}
