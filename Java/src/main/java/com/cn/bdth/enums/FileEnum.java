package com.cn.bdth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 文件夹名
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum FileEnum {

    //头像
    AVATAR("avatar"),
    //博客
    BLOG("blog"),
    //绘图
    PAINTING("painting"),
    //文章封面
    COVER("cover");

    String dec;

}
