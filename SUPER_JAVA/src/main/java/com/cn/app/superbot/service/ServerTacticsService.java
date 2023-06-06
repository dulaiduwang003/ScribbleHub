package com.cn.app.superbot.service;

import com.cn.app.superbot.dto.OperationPolicyDto;
import com.cn.app.superbot.dto.ServerPolicyDto;
import com.cn.app.superbot.vo.OperationStrategyVo;
import com.cn.app.superbot.vo.ServerStrategyVo;

/**
 * The interface Server tactics service.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
public interface ServerTacticsService {


    /**
     * Write server configure.
     */
    ServerStrategyVo writeServerConfigure(final ServerPolicyDto dto);


    /**
     * Gets server configure.
     *
     * @return the server configures
     */
    ServerStrategyVo getServerConfigure();


    /**
     * Write operation configure operation strategy vo.
     *
     * @param dto the dto
     * @return the operation strategy vo
     */
    OperationStrategyVo writeOperationConfigure(final OperationPolicyDto dto);


    /**
     * Gets operation configure.
     *
     * @return the operation configure
     */
    OperationStrategyVo getOperationConfigure();


}
