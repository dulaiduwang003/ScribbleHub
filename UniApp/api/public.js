// 引用网络请求中间件
import request from './../utils/request';

/**
 *    获取推荐文章
 */
export function recommendedBlogList() {
    return request({
        url: '/public/blog/popular/article',
        method: 'GET'
    })
}

/**
 *    获取专栏 走马灯
 */
export function classifyMarquee() {
    return request({
        url: '/public/blog/marquee/classify',
        method: 'GET'
    })
}


/**
 *    获取所有文章
 */
export function blogPagination(data) {
    return request({
        url: '/public/blog/page/article/' + data,
        method: 'GET'
    })
}

/**
 * 根据类型获取博客列表
 * @param a
 * @returns {Promise<unknown>}
 */
export function blogBasedOnType(a) {
    return request({
        url: '/public/blog/topic/article/' + a,
        method: 'GET'
    })
}


/**
 *    查看指定文章
 */
export function blogArticle(data) {
    return request({
        url: '/public/blog/view/article/' + data,
        method: 'GET'
    })
}

/**
 * 根据专题ID获取文章列表
 * @returns {Promise | Promise<unknown>}
 * @param a

 */
export function articlesBasedOnFeaturedId(a) {
    return request({
        url: '/public/blog/classify/article/' + a,
        method: 'GET'
    })
}

/**
 * 获取评论
 * @returns {Promise | Promise<unknown>}
 * @param a
 * @param b
 */
export function blogComment(a) {
    return request({
        url: '/public/blog/article/comment/' + a,
        method: 'GET'
    })
}


/**
 * 获取评论
 * @returns {Promise | Promise<unknown>}
 * @param a
 * @param b
 */
export function blogReply(a) {
    return request({
        url: '/public/blog/comment/reply/' + a,
        method: 'GET'
    })
}


/**
 * 搜索随机读
 * @returns {Promise<unknown>}
 */
export function fetchDataRandomly() {
    return request({
        url: '/public/search/random/article',
        method: 'GET'
    })
}

/**
 * 搜索文章
 * @returns {Promise | Promise<unknown>}
 */
export function searchArticle(data) {
    return request({
        url: '/public/search/article',
        method: 'GET',
        data
    })
}
