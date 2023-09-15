// 引用网络请求中间件
import request from './../utils/request';




/**
 *  文生图
 */
export function addDrawingTextTaskQueue(data) {
    return request({
        url: '/function/drawing/task/text/picture',
        method: 'POST',
        data
    })
}

/**
 *  检测绘图是否成功
 */
export function monitoringDrawingSuccess(data) {
    return request({
        url: '/function/drawing/monitoring',
        method: 'GET',
        data
    })
}


/**
 *  查看指定绘图
 */
export function getsSpecifiedPicture(data) {
    return request({
        url: '/function/drawing/get/picture',
        method: 'GET',
        data
    })
}



/**
 *  检查网络连通性
 */
export function whetherDrawingIsTurnedOn() {
    return request({
        url: '/function/drawing/detect',
        method: 'GET'
    })
}


/**
 *  获取我的创作
 */
export function currentUserOpus() {
    return request({
        url: '/function/drawing/user/opus',
        method: 'GET'
    })
}

/**
 *  获取公开传作
 */
export function getsPublicOpus() {
    return request({
        url: '/function/drawing/public/opus',
        method: 'GET'
    })
}

/**
 *     发表评论
 */
export function publicationComment(data) {
    return request({
        url: '/function/blog/publication/comment',
        method: 'POST',
        data
    })
}

/**
 * 回复评论 或 回复 (回复)
 * @param data
 * @returns {Promise<unknown>}
 */
export function publicationReply(data) {
    return request({
        url: '/function/blog/publication/reply',
        method: 'POST',
        data
    })
}

/**
 * 删除评论
 * @param data
 * @returns {Promise | Promise<unknown>}
 */
export function deletedComment(data) {
    return request({
        url: '/function/blog/delete/comment',
        method: 'POST',
        data
    })
}

/**
 * 删除回复
 * @param data
 * @returns {Promise | Promise<unknown>}
 */
export function deletedReply(data) {
    return request({
        url: '/function/blog/delete/reply',
        method: 'POST',
        data
    })
}
