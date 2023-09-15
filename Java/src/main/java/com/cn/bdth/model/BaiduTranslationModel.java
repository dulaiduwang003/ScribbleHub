package com.cn.bdth.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 雨纷纷旧故里草木深
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class BaiduTranslationModel {

        private String q;

        private String from="auto";

        private String to="zh";

        private String appid;

        private String salt;

        private String sign;
}
