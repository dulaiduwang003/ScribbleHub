package com.cn.app.superbot.constants;

/**
 * The type Error msg constants.
 *
 * @author bdth
 */
public interface MsgConstants {

    /**
     * The constant IS_CLOSED.
     */
    String IS_CLOSED = "IS_CLOSED";

    /**
     * The constant PAID.
     */
    String PAID = "PAID";

    /**
     * The constant BE_PAID.
     */
    String BE_PAID = "BE_PAID";

    /**
     * The constant ORDER_TIMEOUT_ERR.
     */
    String ORDER_TIMEOUT_ERR = "用户未支付，订单自动关闭";

    /**
     * The constant CREATED_ORDER_ERR.
     */
    String CREATED_ORDER_ERR = "创建订单失败";

    /**
     * The constant PLACE_AN_ORDER_REPEATEDLY_ERR.
     */
    String PLACE_AN_ORDER_REPEATEDLY_ERR = "请勿重复下单";


    /**
     * The constant PRODUCT_NULL_ERR.
     */
    String PRODUCT_NULL_ERR = "商品不存在";

    /**
     * The constant VERIFICATION_CODE_SENT.
     */
    String VERIFICATION_CODE_ERR = "生成验证码失败";

    /**
     * The constant VERIFICATION_CODE_SENT.
     */
    String VERIFICATION_CODE_SENT = "请勿频繁发送验证码";

    /**
     * The constant CONNECTION_FAILED.
     */
    String LOGIN_HAS_FAILED = "注册失败,无法正确获取请求,请联系管理员解决";

    /**
     * The constant CONNECTION_FAILED.
     */
    String CONNECTION_FAILED = "该功能管理员暂未启动";

    /**
     * The constant NOT_LOGGED_IN.
     */
    String NOT_LOGGED_IN = "登录会话失效,请重新登录";

    /**
     * The constant LOGIN_FAILED.
     */
    String LOGIN_FAILED = "登录失败";


    /**
     * The constant INTERFACE_DOES_NOT_EXIST_ERR.
     */
    String INTERFACE_DOES_NOT_EXIST_ERR = "接口参数异常";


    /**
     * The constant ILLEGAL_ACCESS
     */
    String ILLEGAL_ACCESS = "未经授权访问";

    /**
     * The constant EXCHANGE_CODE_SUCCESS.
     */
    String EXCHANGE_CODE_SUCCESS = "兑换成功";


    /**
     * The constant UPLOAD_ERR_IMAGE.
     */
    String UPLOAD_ERR_IMAGE = "上传图片失败";


    /**
     * The constant EXCHANGE_CODE_LOCK_ERR.
     */
    String EXCHANGE_CODE_LOCK_ERR = "兑换繁忙请稍后再试";


    /**
     * The constant EXCHANGE_CODE_NULL_ERR.
     */
    String EXCHANGE_CODE_NULL_ERR = "该兑换码不存在";

    /**
     * The Gpt api err.
     */
    String GPT_API_ERR = "服务貌似出了点问题,工作人员正在抢修中~";

    /**
     * The constant SUSCEPTIBLE_ERR.
     */
    String GPT_SUSCEPTIBLE_ERR = "抱歉您刚刚的回答不符合微信社区规定,我不予答复";

    /**
     * The constant SERVER_API_ERR.
     */
    String SERVER_API_ERR = "API调用失败";

    /**
     * The constant REWARD_API_ERR.
     */
    String REWARD_API_ERR = "发放奖励失败~";


    /**
     * The constant SD_API_ENABLE_ERR.
     */
    String SD_API_ENABLE_ERR = "暂时无法生成,该功能可能被管理员禁用";

    /**
     * The Gpt api timeout.
     */
    String GPT_API_TIMEOUT = "回复时间超时";


    /**
     * The constant WECHAT_API_ERR.
     */
    String WECHAT_API_ERR = "调取微信接口失败";

    /**
     * The constant PWD_ERR.
     */
    String PWD_ERR = "账号或密码错误";

    /**
     * The constant ACCOUNT_ALREADY_EXISTS.
     */
    String ACCOUNT_ALREADY_EXISTS_ERR = "该账号已存在";


    /**
     * The constant CODE_ERR.
     */
    String CODE_ERR = "验证码错误";


    /**
     * The constant FREQUENTLY_REGISTER.
     */
    String FREQUENTLY_REGISTER = "请勿频繁注册 请15分钟后再试";

    /**
     * The constant FREQUENTLY_REGISTER.
     */
    String GENERATE_IMAGES_ERR = "生成图片失败 请重试, 如多次失败请在微信小程序中联系客服解决";

    /**
     * The constant LOGOUT_ERR.
     */
    String LOGOUT_ERR = "注销失败";


    /**
     * The constant TACTICS_ERR.
     */
    String TACTICS_ERR = "参数策略异常";

    /**
     * The constant NEW_BING_CONFIG_ERR.
     */
    String NEW_BING_CONFIG_ERR = "请联系管理员设置NewBing参数";


    /**
     * The constant NOT_PROXY_ERR.
     */
    String NOT_PROXY_ERR = "如果使用代理模式 请配置 OpenKey 代理ip 代理端口";

    /**
     * The constant OVERSEAS_KEY_ERR.
     */
    String OVERSEAS_KEY_ERR = "如果使用直连方式 请配置 OpenKey";


    /**
     * The constant CONFIG_ERR.
     */
    String CONFIG_ERR = "请先配置服务器参数";

    /**
     * The constant SUSCEPTIBLE_ERR.
     */
    String SUSCEPTIBLE_ERR = "请勿发布违反微信社区规定的发言";

    /**
     * The constant QUEUE_FILL_ERR.
     */
    String QUEUE_FILL_ERR = "抱歉,目前队列已排满,请稍后再试";

    /**
     * The constant USER_INFO_ERR.
     */
    String USER_INFO_ERR = "获取用户数据失败";

    /**
     * The constant THIRD_PARTY_CONFIG_ERR.
     */
    String THIRD_PARTY_CONFIG_ERR = "如果使用第三方配置 请配置 url 以及 token";
    /**
     * The constant NEW_BING_TIME_ERR.
     */
    String NEW_BING_TIME_ERR = "哦豁!服务貌似出了点问题请稍后重新发送😀";

    /**
     * The constant NEW_BING_SERVER_ERR.
     */
    String NEW_BING_SERVER_ERR = "NewBing服务目前已关闭";

    /**
     * The constant SERVER_CONFIG_ERR.
     */
    String SERVER_CONFIG_ERR = "请联系管理员配置服务器参数";


    /**
     * The constant SD_CONFIG_ERR.
     */
    String SD_CONFIG_ERR = "请联系管理员设置SD参数";

    /**
     * The constant GPT_4_CONFIG_ERR.
     */
    String GPT_4_CONFIG_ERR = "请联系管理员设置该功能参数";


    /**
     * The constant LIMIT_NOT.
     */
    String LIMIT_NOT_ERR = "SUPER币不足,请赞助";
}
