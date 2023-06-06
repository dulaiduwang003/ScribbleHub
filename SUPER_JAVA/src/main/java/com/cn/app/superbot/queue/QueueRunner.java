
package com.cn.app.superbot.queue;

import com.cn.app.superbot.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * The type Queue runner.
 *
 * @author 明明不是下雨天
 */
@Component
@RequiredArgsConstructor
@Slf4j

public class QueueRunner implements CommandLineRunner {


    /**
     * The Order service.
     */
    private final OrderService orderService;

    /**
     * The Blocking fair queue.
     */
    private final RBlockingDeque<String> blockingFairQueue;


    /**
     * Run.
     *
     * @param args the args
     */
    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                String orderNo;
                try {
                    orderNo = blockingFairQueue.take();
                } catch (Exception e) {
                    continue;
                }
                orderService.queueOrderCheck(orderNo);
            }
        }).start();
    }
}
