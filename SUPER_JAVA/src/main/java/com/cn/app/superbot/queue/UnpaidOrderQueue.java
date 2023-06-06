
package com.cn.app.superbot.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * The type Unpaid order queue.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UnpaidOrderQueue {

    /**
     * The Delayed queue.
     */
    private final RDelayedQueue<String> delayedQueue;


    /**
     * Add order.
     *
     * @param orderNo the order no
     */
    public void addOrder(final String orderNo) {
        //加入队列
        delayedQueue.offer(orderNo, 290, TimeUnit.SECONDS);
    }
}
