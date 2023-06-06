package com.cn.app.superbot.wss;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.constants.WssConstants;
import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.exception.InsufficientAmountException;
import com.cn.app.superbot.strategy.ChatStrategy;
import com.cn.app.superbot.strategy.ChatStrategyFactory;
import com.cn.app.superbot.structure.StrategyStructure;
import com.cn.app.superbot.utils.ChatUtils;
import com.cn.app.superbot.utils.SpringContextUtil;
import com.cn.app.superbot.utils.UserUtils;
import jakarta.websocket.*;
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
@ServerEndpoint("/chat/api/{type}/{token}")
@SuppressWarnings("all")
@Service
public class ChatWss {


    /**
     * 会话
     */
    private Session session;


    /**
     * 长连接缓冲池
     */
    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<>();

    /**
     * Ai对话多实例类
     */
    private static ChatStrategyFactory chatStrategyFactory;


    /**
     * GPT方面工具类
     */
    private static ChatUtils chatUtils;


    /**
     * On open.
     *
     * @param session the session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        /*
          鉴权
         */
        if (StpUtil.getLoginIdByToken(token) == null) {
            try {
                //抛出异常
                session.close(new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, MsgConstants.ILLEGAL_ACCESS));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        /*
         加载Bean
         */
        if (chatStrategyFactory == null) {
            // bean
            chatStrategyFactory = (ChatStrategyFactory) SpringContextUtil.getBean("chatStrategyFactory");
            // chatUtils uitls
            chatUtils = (ChatUtils) SpringContextUtil.getBean("chatUtils");
        }
        this.session = session;
        webSocketSet.put(session.getId(), session);
    }


    /**
     * 发送消息
     *
     * @param messages 请求体数据
     * @param type     执行类型
     * @param token    用户token
     */
    @OnMessage
    public void onMessage(String messages, @PathParam("type") String type, @PathParam("token") String token) {
        //获取用户ID
        final long userId = Long.parseLong(UserUtils.userIdByToken(token));
        // 读取请求体
        final ChatGPTDto dto = chatUtils.eliminate(JSONObject.parseObject(messages, ChatGPTDto.class));
        final StrategyStructure config = ChatComment.strategyStructure;
        //获取具体实例
        final ChatStrategy impl = chatStrategyFactory.getImpl(type);
        //获取该实例需要消耗多少次数
        final Long frequency = impl.frequency();
        try {
            /**
             * 检查用户次数 参数1 用户ID  ，功能消耗次数
             */
            chatUtils.checkDeduct(userId, frequency);
        } catch (InsufficientAmountException e) {
            //次数不足
            AppointSending(session.getId(), WssConstants.NO_FREQUENCY);
            handleWebSocketCompletion();
        }
        //执行响应式数据
        impl.execution(config, dto)
                /**
                 * 如果60秒无响应则抛出异常
                 * 并回滚用户次数
                 */
                .timeout(Duration.ofSeconds(60))
                .doOnError(TimeoutException.class, e -> {
                    //回滚用户次数
                    handleWebSocketError(userId, frequency, WssConstants.OVERTIME);
                })
                .subscribe(data -> {
                    //响应数据
                    AppointSending(session.getId(), data);
                }, throwable -> {
                    //中途发生异常 一样回滚次数
                    handleWebSocketError(userId, frequency, WssConstants.SEND_ERR);
                }, () -> {
                    //执行完毕 回收资源
                    handleWebSocketCompletion();
                });

    }

    /**
     * 补偿用户次数
     *
     * @param userId    the user id
     * @param frequency the frequency
     * @param errMsg    the err msg
     */
    private void handleWebSocketError(final long userId, final long frequency, final String errMsg) {
        chatUtils.compensate(userId, frequency);
        AppointSending(session.getId(), errMsg);
        handleWebSocketCompletion();
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
