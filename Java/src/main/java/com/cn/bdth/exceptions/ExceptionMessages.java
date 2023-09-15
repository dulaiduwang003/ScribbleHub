package com.cn.bdth.exceptions;

/**
 * 异常消息
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public interface ExceptionMessages {

    String UPLOAD_INVERT = " 转化BASE失败";

    String WECHAT_VIOLATIONS = " 请勿发布违反微信社区规则的内容";

    String GPT_TIMEOUT = ", 当前使用人数过多,请稍后再试";

    String UPLOAD_DELETE = "删除资源失败";

    String UPLOAD_ERR = "上传图片失败";

    String UPLOAD_DOWNLOAD = "下载资源失败";


    String BLOG_NOT_EXIST = "博客不存在";

    String IMAGE_NOT_EXIST = "图片不存在";


    String WECHAT_AUTHORIZATION = "微信图像识别能力调用失败";

    String COMMENT_NOT_EXIST = "评论不存在";

    String REPLY_NOT_EXIST = "回复不存在";


    String BLOG_EXIST = "当前专题下存在文章,请先删除文章";
}
