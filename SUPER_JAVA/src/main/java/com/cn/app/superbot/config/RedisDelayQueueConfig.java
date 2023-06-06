/**
 * @author 明明不是下雨天
 */
package com.cn.app.superbot.config;

import com.cn.app.superbot.constants.cache.OrderCache;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Redis delay queue config.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Configuration
public class RedisDelayQueueConfig {


    /**
     * Blocking deque r blocking deque.
     *
     * @param redissonClient the redisson client
     * @return the r blocking deque
     */
    @Bean
    public RBlockingDeque<String> blockingDeque(RedissonClient redissonClient) {
        return redissonClient.getBlockingDeque(OrderCache.ORDER_QUEUE);
    }

    /**
     * Delayed queue r delayed queue.
     *
     * @param redissonClient the redisson client
     * @return the r delayed queue
     */
    @Bean
    public RDelayedQueue<String> delayedQueue(RedissonClient redissonClient) {
        return redissonClient.getDelayedQueue(blockingDeque(redissonClient));
    }
}
