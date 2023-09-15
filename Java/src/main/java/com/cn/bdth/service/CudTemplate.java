package com.cn.bdth.service;

import com.cn.bdth.enums.CudEnum;

/**
 * 通用CUD抽象类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public abstract class CudTemplate<T> {


    /**
     * 匹配.
     *
     * @param t       the t
     * @param cudEnum the cud enum
     */
    public void matching(T t, CudEnum cudEnum) {
        switch (cudEnum) {
            case UPDATE -> update(t);
            case INSERT -> insert(t);
            case DELETED -> delete(t);
            default -> throw new RuntimeException();
        }
    }

    /**
     * 新增.
     *
     * @param t the t
     */
    protected abstract void insert(T t);

    /**
     * 修改.
     *
     * @param t the t
     */
    protected abstract void update(T t);

    /**
     * 删除.
     *
     * @param t the t
     */
    protected abstract void delete(T t);
}
