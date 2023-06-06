package com.cn.app.superbot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.app.superbot.entity.SuperUser;
import com.cn.app.superbot.vo.UserPageVo;

/**
 * The interface User service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
public interface UserService extends IService<SuperUser> {





    /**
     * Gets user page.
     *
     * @param pageNum the page num
     * @return the user page
     */
    IPage<UserPageVo> getUserPage(final Integer pageNum);

}
