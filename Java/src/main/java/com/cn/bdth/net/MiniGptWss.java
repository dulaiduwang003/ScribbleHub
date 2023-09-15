package com.cn.bdth.net;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.bdth.dto.AiChatDto;
import com.cn.bdth.exceptions.ExceptionMessages;
import com.cn.bdth.exceptions.ViolationsException;
import com.cn.bdth.exceptions.WechatException;
import com.cn.bdth.service.ChatService;
import com.cn.bdth.service.impl.ChatServiceImpl;
import com.cn.bdth.utils.ChatUtils;
import com.cn.bdth.utils.SpringContextUtil;
import com.cn.bdth.utils.UserUtils;
import com.cn.bdth.utils.WeChatUtils;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;


/**
 * 长连接响应.
 *
 * @author bdth
 */
@Slf4j
@ServerEndpoint("/gpt/api/{token}")
@SuppressWarnings("all")
@Service
public class MiniGptWss {

    private Session session;
    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<>();
    private static ChatUtils chatUtils;
    private static WeChatUtils weChatUtils;
    private static ChatService chatService;


    /**
     * On open.
     *
     * @param session the session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        webSocketSet.put(session.getId(), session);
        if (StpUtil.getLoginIdByToken(token) == null) {
            //关闭连接
            handleWebSocketCompletion();
        }
        /*
         加载Bean
         */
        if (chatService == null) {
            chatService = (ChatServiceImpl) SpringContextUtil.getBean("chatServiceImpl");
            chatUtils = (ChatUtils) SpringContextUtil.getBean("chatUtils");
            weChatUtils = (WeChatUtils) SpringContextUtil.getBean("weChatUtils");
        }

    }


    /**
     * 发送消息
     *
     * @param messages 请求体数据
     * @param type     执行类型
     * @param token    用户token
     */
    @OnMessage
    public void onMessage(String messages, @PathParam("token") String token) {
        try {
            final AiChatDto dto = JSONObject.parseObject(messages, AiChatDto.class);
            // 微信文字识别能力 防止用户发送色情 政治信息
            weChatUtils.filterText(dto.getPrompt(), UserUtils.getOpenIdByToken(token));
            // 缓存文字
            final StringBuilder builder = new StringBuilder(100);
            chatService.chatFlux(dto)
                    .timeout(Duration.ofSeconds(60))
                    //建立连接超过60秒无回应抛出异常
                    .doOnError(TimeoutException.class, e -> handleWebSocketError(ExceptionMessages.GPT_TIMEOUT))
                    .doFinally(signal -> handleWebSocketCompletion())
                    .subscribe(data -> {
                        //删除“data”标头并转换为有效的 JSON 字符串
                        final String replace = data.replaceFirst("data:", "");
                        if (JSON.isValid(replace)) {
                            final JSONObject jsonObject = JSONObject.parseObject(replace);
                            // 获取文本节点
                            final JSONObject choices = jsonObject.getJSONArray("choices").getJSONObject(0);
                            // 获取特定内容
                            final String text = choices.getString("text");
                            // 是否开启自定义校验
                            if (chatUtils.isSensitive) {
                                builder.append(text.toUpperCase());
                                // 检查敏感词
                                if (chatUtils.isSusceptible(builder.toString())) {
                                    //抛出异常
                                    handleWebSocketError(ExceptionMessages.WECHAT_VIOLATIONS);
                                    //结束连接
                                    handleWebSocketCompletion();
                                    return;
                                }
                            }
                            //回复数据
                            AppointSending(session.getId(), text);
                        }
                    }, throwable -> {
                        log.error("调用GPT时出现异常 异常信息:{} 异常类:{}", throwable.getMessage(), throwable.getClass());
                        handleWebSocketError(ExceptionMessages.GPT_TIMEOUT);
                    });
        } catch (WechatException | ViolationsException e) {
            AppointSending(session.getId(), e.getMessage());
            handleWebSocketCompletion();
            return;
        }

    }

    /**
     * 发送错误消息
     *
     * @param errMsg the err msg
     */
    private void handleWebSocketError(final String errMsg) {
        AppointSending(session.getId(), errMsg);
    }

    /**
     * 回收资源
     */
    @OnClose
    public void handleWebSocketCompletion() {
        webSocketSet.remove(this.session.getId());
        try {
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 发送消息
     *
     * @param id      the id
     * @param message the message
     */
    public void AppointSending(final String id, final String message) {
        try {
            final Session conversation = webSocketSet.get(id);
            conversation.getBasicRemote().sendText(message);
        } catch (Exception e) {

        }
    }

}
