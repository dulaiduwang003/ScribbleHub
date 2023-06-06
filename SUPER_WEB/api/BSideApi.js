import request from '@/utils/BSideRequest'


/**
 *    创建支付宝付款订单
 */
export function AlipayPayment(data) {
    return request({
        url: '/order/alipay/order',
        method: 'POST',
        data
    })
}

/**
 * 心跳检查
 * @param data
 * @returns {*}
 * @constructor
 */
export function detect(data) {
    return request({
        url: '/order/alipay/order',
        method: 'POST',
        data
    })
}

/**
 *    管理员登录
 */
export function ServerLogin(data) {
    return request({
        url: '/auth/server/login',
        method: 'POST',
        data
    })

}

/**
 *    获取订单状态
 */
export function AlipayStatus(data) {
    return request({
        url: '/order/pay/status',
        method: 'POST',
        data
    })
}

/**
 *   获取我的所有订单
 * @param data
 * @returns {*}
 * @constructor
 */
export function OrderConsumePage(data) {
    return request({
        url: '/user/page/orders/' + data,
        method: 'GET'
    })
}


/**
 *    邮箱号码登录
 */
export function EmailLogin(data) {
    return request({
        url: '/auth/email/login',
        method: 'POST',
        data
    })
}

/**
 *    邮箱号码注册
 */
export function EmailEnroll(data) {
    return request({
        url: '/auth/email/register',
        method: 'POST',
        data
    })
}

/**
 *    获取用户信息
 */
export function GetUserInfo() {
    return request({
        url: '/user/get/user-info',
        method: 'GET'
    })
}



/**
 *    绘画
 */
export function Painting(data) {
    return request({
        url: '/function/generate/picture',
        method: 'POST',
        data
    })
}

/**
 *    获取收藏列表
 */
export function Favorites() {
    return request({
        url: '/user/get/favorites',
        method: 'GET'
    })
}

/**
 *    删除作品
 */
export function DeleteCreation(data) {
    return request({
        url: '/user/delete/picture',
        method: 'POST',
        data
    })
}

/**
 *    获取商品
 */
export function GetProducts() {
    return request({
        url: '/public/get/products',
        method: 'GET'
    })
}

/**
 *    添加收藏
 */
export function FavoritesAdd(data) {
    return request({
        url: '/user/add/favorite',
        method: 'POST',
        data
    })
}

/**
 *    获取我的创作
 */
export function IndividualPicture() {
    return request({
        url: '/user/individual/picture',
        method: 'GET'
    })
}

/**
 *    删除收藏
 */
export function FavoritesDel(data) {
    return request({
        url: '/user/delete/favorite',
        method: 'POST',
        data
    })
}

/**
 *    灵感创作
 */
export function Inspiration(data) {
    return request({
        url: '/function/inspiration/created',
        method: 'POST',
        data
    })

}


/**
 *    获取验证码
 */
export function GetCode(data) {
    return request({
        url: '/auth/code/register',
        method: 'POST',
        data
    })
}


/**
 *    退出登录
 */
export function logOutOfLogin() {
    return request({
        url: '/auth/wx/logout',
        method: 'POST'
    })
}

/**
*    上架修改删除商品
*/
export function CudProduct(data) {
    return request({
        url: '/server/cud/product',
        method: 'POST',
        data
    })
}

/**
 *    配置运营策略
 */
export function PutOperation(data) {
    return request({
        url: '/server/put/operation',
        method: 'POST',
        data
    })
}

/**
 *    获取运营策略
 */
export function GetOperation() {
    return request({
        url: '/server/get/operation',
        method: 'GET'
    })
}

/**
 *    获取服务器配置
 */
export function GetServer() {
    return request({
        url: '/server/get/server',
        method: 'GET'
    })
}
/**
 *    保存服务器配置
 */
export function PutServer(data) {
    return request({
        url: '/server/put/server',
        method: 'POST',
        data
    })
}

/**
 *    编辑用户数据
 */
export function UpdateUser(data) {
    return request({
        url: '/server/update/user',
        method: 'POST',
        data
    })
}

/**
 *    获取用户分页数据
 */
export function getUserPage(data) {
    return request({
        url: '/server/page/user/'+data,
        method: 'GET'
    })
}

/**
 *    获取商品分页数据
 */
export function getProductPage(data) {
    return request({
        url: '/server/page/product/'+data,
        method: 'GET'
    })
}


/**
 *    获取订单分页数据
 */
export function getOrderPage(data) {
    return request({
        url: '/server/page/orders/'+data,
        method: 'GET'
    })
}

