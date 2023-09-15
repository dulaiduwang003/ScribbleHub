"use strict";

export default {
    //懒加载 渲染
    lazyLoading: true,
    // 管理员狗牌
    adminLabel: '超级BOSS',
    // 用户狗牌
    userLabel: '编程练习生',
    // 默认用户名
    user: '练习生',
    // 默认作者名
    author: '时间海',
    // HTTP请求 基类
    baseUrl: 'https://www.seattime.cn',
    // 长连接请求 基类
    baseWs: 'wss://www.seattime.cn',
    // 过滤用户输入关键词 (英文则大写) (初步校验 后端也会做校验)
    filtration: ["GPT", "OPENAI"],
    // BOT最大记忆 能记住最多几条 (初步记忆 后端也会做校验)
    memory: 6,
    // 每条历史词汇最大长度,
    contextLength: 500,
    //BOT 初始化词汇 每次打开将随机抽取一条作为显示信息
    botInitialization: [
        "你有什么问题或者困惑需要我帮助解答吗？",
        "你好！很高兴与您交流，有什么我可以为您做的吗？",
        "你好!有什么需要我帮忙的吗？",
        "如果你想了解一些编程和代码方面的知识，我也可以帮你。",
        "如果你需要一些娱乐，我可以和你玩一些文字游戏或者谜语",
        "请随时告诉我你需要什么，我会尽力满足你的需求！",
        "嗨！欢迎来到这里，有什么我可以为你做的吗？",
    ],
    //微信授权模板ID
    tmplIds: ['_K8fdGIqxa16m0djxS3N8YS-eCY0lZwpLXkqNrGIRzY']

}
