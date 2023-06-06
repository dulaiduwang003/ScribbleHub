package com.cn.app.superbot.utils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * The type SuperUser utils.
 *
 * @author bdth
 */
public class UserUtils {


    /**
     * SuperUser id to string long.
     *
     * @return the long
     */
    public static String userIdToString() {
        final Object loginId = StpUtil.getLoginId();
        return String.valueOf(loginId);
    }

    /**
     * SuperUser id to long.
     *
     * @return the long
     */
    public static Long userIdToLong() {
        final Object loginId = StpUtil.getLoginId();
        return Long.parseLong(String.valueOf(loginId));
    }


    /**
     * SuperUser id by token string.
     *
     * @param token the token
     * @return the string
     */
    public static String userIdByToken(String token){
        return String.valueOf( StpUtil.getLoginIdByToken(token));
    }


}
