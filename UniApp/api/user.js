// 引用网络请求中间件
import request from './../utils/request';

/**
 *    获取当前登录用户信息
 */
export function getCurrentUserInfo() {
    return request({
        url: '/function/user/current/info',
        method: 'GET'
    })
}

/**
 * 修改用户昵称
 * @returns {Promise<unknown>}
 */
export function uploadName(data) {
    return request({
        url: '/function/user/update/name',
        method: 'POST',
        data
    })
}

