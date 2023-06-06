package com.cn.app.superbot.strategy.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.enums.ChatEnum;
import com.cn.app.superbot.handle.Chat;
import com.cn.app.superbot.interfaces.Callback;
import com.cn.app.superbot.strategy.ChatStrategy;
import com.cn.app.superbot.structure.StrategyStructure;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.Semaphore;

/**
 * Type Chat four strategy.
 *
 * @author bdth
 */


@SuppressWarnings("all")
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ChatBingStrategyImpl implements ChatStrategy {

    /**
     * Frequency long.
     *
     * @return the long
     */
    @Override
    public Long frequency() {
        return ChatComment.operateStructure.getBingFrequency();
    }

    /**
     * Type string.
     *
     * @return the string
     */
    @Override
    public String type() {
        return ChatEnum.BING.getDec();
    }

    /**
     * Is type match boolean.
     *
     * @param type the type
     * @return the boolean
     */
    @Override
    public boolean isTypeMatch(String type) {
        return type().equals(type);
    }


    /**
     * The Semaphore.
     */
    private Semaphore semaphore = new Semaphore(1);


    /**
     * Execution string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public Flux<String> execution(final StrategyStructure config, ChatGPTDto dto) {
        try {
            semaphore.acquire();
            final String parameter = ChatGPTDto.convertToBingString(dto);
            Chat chat;
            switch (config.choose) {
                //custom
                case AGENT, CUSTOM -> chat = new Chat("_U=" + config.getBing().cookie,
                        false
                ).setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxy().ip, config.getProxy().port)));

                case DIRECT -> chat = new Chat("_U=" + config.getBing().cookie,
                        false
                );
                default -> throw new RuntimeException();
            }
            return Flux.create(f -> {
                chat.newChat().newQuestion(parameter, new Callback() {
                    @Override
                    public void onSuccess(JsonObject rawData) {
                        f.complete();
                    }

                    @Override
                    public void onFailure(JsonObject rawData, String cause) {
                        f.complete();
                    }

                    @Override
                    public void onUpdate(JsonObject rawData) {
                        final JSONObject jsonObject = JSONObject.parseObject(rawData.toString());
                        //read data for
                        for (Object arguments : jsonObject.getJSONArray("arguments")) {
                            final JSONObject json = JSONObject.parseObject(arguments.toString());
                            // whether it's message
                            if (json.containsKey("messages") && (!json.containsKey("cursor"))) {
                                final JSONArray jsonArray = json.getJSONArray("messages").getJSONObject(0).getJSONArray("adaptiveCards");
                                final JSONObject body = jsonArray.getJSONObject(0).getJSONArray("body").getJSONObject(0);
                                //there data
                                if (body.containsKey("text")) {
                                    f.next(body.getString("text"));
                                }
                            }
                        }
                    }
                });
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
}
