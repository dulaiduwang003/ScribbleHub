package com.cn.bdth.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public class IpUtil {
    private static final String UNKNOWN = "unknown";


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
