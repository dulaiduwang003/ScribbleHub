package com.cn.app.superbot.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The type Ip util.
 */
public class IpUtil {
    private static final String UNKNOWN = "unknown";

    /**
     * Gets ip addr.
     *
     * @param request the request
     * @return the ip addr
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        int index = ipAddress.indexOf(",");
        if (index != -1) {
            ipAddress = ipAddress.substring(0, index);
        }
        return ipAddress;
    }
}
