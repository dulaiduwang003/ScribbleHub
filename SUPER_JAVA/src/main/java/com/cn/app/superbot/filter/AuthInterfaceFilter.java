package com.cn.app.superbot.filter;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.app.superbot.constants.cache.AuthPrefixCache;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Auth interface.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@Component
public class AuthInterfaceFilter implements StpInterface {


    /**
     * Gets a role list.
     *
     * @param o the o
     * @param s the s
     * @return the role list
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        return List.of(
                (String) StpUtil.getExtra(AuthPrefixCache.AUTH_ROLE)
        );
    }

    /**
     * Gets a permission list.
     *
     * @param o the o
     * @param s the s
     * @return the permission list
     */
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }


}
