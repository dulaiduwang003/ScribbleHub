package com.cn.app.superbot.strategy;


import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Chat strategy factory.
 *
 * @author bdth
 */
@Service
@RequiredArgsConstructor
public class ChatStrategyFactory {


    /**
     * The Manager strategies.
     */
    private final List<ChatStrategy> managerStrategies;

    /**
     * Gets .
     *
     * @param type the type
     * @return the
     */
    public ChatStrategy getImpl(String type) {
        return managerStrategies.stream()
                .filter(strategy -> strategy.isTypeMatch(type))
                .findAny()
                .orElseThrow(() -> new CustomException(MsgConstants.INTERFACE_DOES_NOT_EXIST_ERR, 500));
    }
}
