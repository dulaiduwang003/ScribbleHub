package com.cn.bdth.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * BOT工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ChatUtils {


    @Value("${sensitive}")
    private String regex;


    @Value("${isSensitive}")
    public boolean isSensitive;

    /**
     * Is similar boolean.
     *
     * @param str1 the str 1
     * @param str2 the str 2
     * @return the boolean
     */
    public boolean isSimilar(final String str1, final String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        int maxLen = Math.max(len1, len2);
        double similarity = 1 - (double) dp[len1][len2] / maxLen;
        return similarity > 0.9;
    }


    /**
     * Is susceptible boolean.
     *
     * @param data the data
     * @return the boolean
     */
    public boolean isSusceptible(final String data) {
        // 将字符串中的英文转换为大写并去除所有空格
        String processedInput = data.toUpperCase().replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(processedInput);
        return matcher.find();
    }


}
