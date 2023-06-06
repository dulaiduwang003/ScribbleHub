package com.cn.app.superbot.comment;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.app.superbot.constants.ServerConstants;
import com.cn.app.superbot.entity.SuperUser;
import com.cn.app.superbot.mapper.UserMapper;
import com.cn.app.superbot.structure.OperateStructure;
import com.cn.app.superbot.structure.StrategyStructure;
import com.cn.app.superbot.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * The type Async chat task.
 *
 * @author bdth
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ChatComment {

    /**
     * The constant config.
     */
    public static StrategyStructure strategyStructure = new StrategyStructure();

    /**
     * The constant operateStructure.
     */
    public static OperateStructure operateStructure = new OperateStructure();


    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;


    /**
     * The User mapper.
     */
    private final UserMapper userMapper;

    /**
     * The Account.
     */
    @Value(value = "${console.email}")
    private String email;

    /**
     * The Password.
     */
    @Value(value = "${console.password}")
    private String password;


    /**
     * The I'd generator.
     */
    private final IdGenerator idGenerator;


    /**
     * 读取Redis中的服务器配置
     * Load server configuration.
     */
    @PostConstruct
    public void loadServerConfiguration() {
        /*
           获取Redis 服务器配置缓存 并插入 服务器内存当中
           服务器初始化时默认读取
         */
        final SuperUser superUser = userMapper.selectOne(new QueryWrapper<SuperUser>()
                .lambda().eq(SuperUser::getEmail, email)
        );
        password = SaSecureUtil.md5(password);
        if (superUser != null) {
            StpUtil.logout(superUser.getId());
            userMapper.updateById(superUser.setPassword(password));
        }else {
            //重新创建
            userMapper.insert(new SuperUser()
                    .setId(idGenerator.getSnowflakeId())
                    .setFrequency(0L)
                    .setEmail(email)
                    .setPassword(password)
            );
        }
        strategyStructure = (StrategyStructure) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        Optional.ofNullable(strategyStructure).ifPresentOrElse(s -> {
            log.info("检测到服务器参数已配置,更多上下文信息请访问后台管理系统");
            operateStructure = (OperateStructure) redisUtils.getValue(ServerConstants.OPERATE_CONFIG);
            if (operateStructure == null) {
                log.warn("当前未配置服务器运营策略 系统默认加载配置");
                operateStructure = new OperateStructure()
                        .setBingFrequency(1L)
                        .setChatThreeFrequency(1L)
                        .setChatFourFrequency(1L)
                        .setMemory(10L)
                        .setAlipayPublic("")
                        .setAlipayAppid("")
                        .setUserFrequency(10L)
                        .setMappingFrequency(10L);
                redisUtils.setValue(ServerConstants.OPERATE_CONFIG, operateStructure);
            }
        }, () -> log.error("当前服务器未进行任何配置,请前往控制台配置"));

    }


}
