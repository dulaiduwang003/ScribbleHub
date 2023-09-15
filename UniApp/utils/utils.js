const TokenKey = 'token';

const UserKey = 'user';

const HistoryKey = 'history';

const FlowerKey = 'flower';

//获取token缓存
export function getToken() {
    return uni.getStorageSync(TokenKey)
}

//设置token缓存
export function setToken(data) {
    return uni.setStorageSync(TokenKey, data)
}

//移除token缓存
export function removeToken() {
    return uni.removeStorageSync(TokenKey)
}

//获取用户信息缓存
export function getUser() {
    return uni.getStorageSync(UserKey)
}

//设置用户信息缓存
export function setUser(data) {
    return uni.setStorageSync(UserKey, data)
}

//移除用户信息缓存
export function removeUser() {
    return uni.removeStorageSync(UserKey)
}

//获取搜索记录缓存
export function getHistory() {
    return uni.getStorageSync(HistoryKey)
}

//设置搜索记录缓存
export function setHistory(data) {
    return uni.setStorageSync(HistoryKey, data)
}

//移除搜索记录缓存
export function removeHistory() {
    return uni.removeStorageSync(HistoryKey)
}


export function removeFlower() {
    return uni.removeStorageSync(FlowerKey)
}


export function setFlower(data) {
    return uni.setStorageSync(FlowerKey, data)
}

//移除搜索记录缓存
export function getFlower() {
    return uni.getStorageSync(FlowerKey)
}
