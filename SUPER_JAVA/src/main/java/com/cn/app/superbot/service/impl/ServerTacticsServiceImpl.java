package com.cn.app.superbot.service.impl;

import com.cn.app.superbot.comment.ChatComment;
import com.cn.app.superbot.constants.ServerConstants;
import com.cn.app.superbot.dto.OperationPolicyDto;
import com.cn.app.superbot.dto.ServerPolicyDto;
import com.cn.app.superbot.service.ServerTacticsService;
import com.cn.app.superbot.structure.OperateStructure;
import com.cn.app.superbot.structure.StrategyStructure;
import com.cn.app.superbot.utils.BeanUtils;
import com.cn.app.superbot.utils.RedisUtils;
import com.cn.app.superbot.vo.OperationStrategyVo;
import com.cn.app.superbot.vo.ServerStrategyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Server service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ServerTacticsServiceImpl implements ServerTacticsService {


    /**
     * The Redis utils.
     */
    private final RedisUtils redisUtils;


    /**
     * Write server configure.
     *
     * @param dto the dto
     */
    @Override
    public ServerStrategyVo writeServerConfigure(final ServerPolicyDto dto) {
        final StrategyStructure strategyStructure = BeanUtils.copyClassProperTies(dto, StrategyStructure.class);
        redisUtils.setValue(ServerConstants.SERVER_CONFIG, strategyStructure);
        ChatComment.strategyStructure=strategyStructure;
        return BeanUtils.copyClassProperTies(strategyStructure, ServerStrategyVo.class);
    }

    /**
     * Gets server configure.
     *
     * @return the server configures
     */
    @Override
    public ServerStrategyVo getServerConfigure() {
        final StrategyStructure strategyStructure = (StrategyStructure) redisUtils.getValue(ServerConstants.SERVER_CONFIG);
        return BeanUtils.copyClassProperTies(strategyStructure, ServerStrategyVo.class);
    }

    /**
     * Gets operation configure.
     *
     * @return the operation configure
     */
    @Override
    public OperationStrategyVo getOperationConfigure() {
        final OperateStructure operateStructure = (OperateStructure) redisUtils.getValue(ServerConstants.OPERATE_CONFIG);
        return BeanUtils.copyClassProperTies(operateStructure, OperationStrategyVo.class);
    }

    /**
     * Write operation configure operation strategy vo.
     *
     * @param dto the dto
     * @return the operation strategy vo
     */
    @Override
    public OperationStrategyVo writeOperationConfigure(final OperationPolicyDto dto) {
        final OperateStructure operateStructure = BeanUtils.copyClassProperTies(dto, OperateStructure.class);
        redisUtils.setValue(ServerConstants.OPERATE_CONFIG, operateStructure);
        ChatComment.operateStructure = operateStructure;
        return BeanUtils.copyClassProperTies(operateStructure, OperationStrategyVo.class);
    }
}
