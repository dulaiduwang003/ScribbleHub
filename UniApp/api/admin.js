// 引用网络请求中间件
import request from './../utils/request';

/**
 *   获取BOT参数
 */
export function botConfiguration() {
    return request({
        url: '/admin/server/get/config',
        method: 'GET'
    })
}


/**
 *   设置BOT参数
 */
export function botConfigurationUpdate(data) {
    return request({
        url: '/admin/server/put/config',
        method: 'POST',
        data
    })
}


/**
 *   删除博客
 */
export function deletedBlogArticle(data) {
    return request({
        url: '/admin/blog/delete/article',
        method: 'POST',
        data
    })
}


/**
 *    获取专栏信息列表
 */
export function getClassifyInfo() {
    return request({
        url: '/admin/blog/drop/classify',
        method: 'GET'
    })
}

/**
 *    获取所有绘图
 */
export function getAllDrawings() {
    return request({
        url: '/admin/picture/all/creation',
        method: 'GET'
    })
}

/**
 *    切换图片状态
 */
export function setPublicDrawing(data) {
    return request({
        url: '/admin/drawing/set/public',
        method: 'POST',
        data
    })
}

/**
 *    删除博客内容文件
 */
export function deleteBlogFile(data) {
    return request({
        url: '/admin/blog/delete/resource',
        method: 'POST',
        data
    })
}

/**
 *    获取所有博客文章
 */
export function getAllBlogPosts() {
    return request({
        url: '/admin/blog/all/article',
        method: 'GET'
    })
}


/**
 *    设置推荐文章
 */
export function setRecommend(data) {
    return request({
        url: '/admin/blog/popular/article',
        method: 'POST',
        data
    })
}

/**
 * 获取所有专题
 * @returns {Promise | Promise<unknown>}
 */
export function getAllClassify() {
    return request({
        url: '/admin/blog/all/classify',
        method: 'GET'
    })
}

/**
 * 删除指定专栏
 * @param data
 * @returns {Promise | Promise<unknown>}
 */
export function deletedClassify(data) {
    return request({
        url: '/admin/blog/delete/classify',
        method: 'POST',
        data
    })
}
