package com.cn.app.superbot.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The type Spring context util.
 *
 * @author bdth
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {


    /**
     * The constant applicationContext.
     */
    private static ApplicationContext applicationContext;


    /**
     * Sets application context.
     *
     * @param applicationContext the application context
     * @throws BeansException the bean exception
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }


    /**
     * Gets bean.
     *
     * @param beanId the bean id
     * @return the bean
     * @throws BeansException the bean exception
     */
    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }
}
