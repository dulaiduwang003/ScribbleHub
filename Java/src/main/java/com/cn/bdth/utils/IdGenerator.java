package com.cn.bdth.utils;

import cn.ipokerface.snowflake.SnowflakeIdGenerator;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ID生成
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
public class IdGenerator {
    private final SnowflakeIdGenerator idGenerator;

    public IdGenerator() {
        this.idGenerator = new SnowflakeIdGenerator(0, 0);
    }

    public long getSnowflakeId() {
        // 生成 用户主键ID
        return idGenerator.nextId();
    }

    public String getOrderNo() {
        // 生成订单号，格式为 "yyyyMMddHHmmss" + "SnowflakeId"
        return DateFormatUtils.format(new Date(), "yyyyMMddHH") + idGenerator.nextId();
    }
}
