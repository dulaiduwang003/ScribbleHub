package com.cn.app.superbot.strategy.chat;

import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.constants.AiConstants;
import com.cn.app.superbot.dto.ChatGPTDto;
import com.cn.app.superbot.enums.ChatEnum;
import com.cn.app.superbot.strategy.ChatStrategy;
import com.cn.app.superbot.structure.StrategyStructure;
import com.cn.app.superbot.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

/**
 * Type Chat three strategy.
 *
 * @author bdth
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ChatThreeStrategyImpl implements ChatStrategy {

    /**
     * Frequency long.
     *
     * @return the long
     */
    @Override
    public Long frequency() {
        return ChatComment.operateStructure.getChatThreeFrequency();
    }

    /**
     * Type string.
     *
     * @return the string
     */
    @Override
    public String type() {
        return ChatEnum.GPT3_5.getDec();
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
     * The Chat utils.
     */
    private final ChatUtils chatUtils;


    /**
     * Execution string.
     *
     * @param dto the dto
     * @return the string
     */
    @Override
    public Flux<String> execution(final StrategyStructure config, final ChatGPTDto dto) {
        //return Stream
        return chatUtils.chatWebClient(config)
                .build()
                .post()
                .uri(AiConstants.MODEL_GPT_URI)
                .body(BodyInserters.fromValue(ChatGPTDto.convertToGptThreeModel(dto)))
                .retrieve()
                .bodyToFlux(String.class);
    }
}
