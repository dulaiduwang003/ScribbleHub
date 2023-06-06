package com.cn.app.superbot.service;

import com.cn.app.superbot.constants.MsgConstants;
import com.cn.app.superbot.enums.CudEnum;
import org.springframework.stereotype.Component;

/**
 * The type Cud abstract.
 *
 * @param <T> the type parameter
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Component
public abstract class CudAbstract<T> {

    /**
     * Action.
     *
     * @param t       the t
     * @param cudEnum the cud enum
     */
    public void action(T t, CudEnum cudEnum) {
        switch (cudEnum) {
            case INSERT -> this.insert(t);
            case UPDATE -> this.update(t);
            case DELETED -> this.delete(t);
            default -> throw new RuntimeException(MsgConstants.TACTICS_ERR);
        }
    }

    /**
     * Insert.
     *
     * @param t the t
     */
    protected abstract void insert(T t);

    /**
     * Delete.
     *
     * @param t the t
     */
    protected abstract void delete(T t);

    /**
     * Update.
     *
     * @param t the t
     */
    protected abstract void update(T t);

}
