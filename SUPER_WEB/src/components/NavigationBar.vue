<template>
    <nav class="header">
        <div class="header-side">
            <router-link class="logo hidden-xs-only" to="/">GPT</router-link>
            <div @click="appletDialogVisible = true" class="rechargeButton hidden-xs-only applet">
                <img class="appletIcon" :src="require('../assets/applet.svg')">
                <div class="rechargeButtonText">微信小程序</div>
            </div>
        </div>
        <div v-if="isHeadNavigation" class="header-center">
            <div class="switch-button">
                <block v-for="(item,index) in navigationList" :key="index">
                    <router-link active-class="switch-active" class="switch-item" :to="item.to">{{ item.title }}
                    </router-link>
                </block>
            </div>
        </div>

        <div v-if="store.getters.userinfo" class="header-side header-right">
            <div @click="router().push({
                    path: '/Purchase'
                  })" class="rechargeButton hidden-xs-only">
                <el-icon size="16">
                    <Goods/>
                </el-icon>
                <div class="rechargeButtonText">充值</div>
            </div>
            <div class="header-right">
                <div class="header-user-wrapper">
                    <el-dropdown ref="dropdown1" trigger="contextmenu">
                        <div @click="showClick" class="header-user-btn">
                            <div class="header-user-name">个人中心</div>
                        </div>
                        <template #dropdown>
                            <el-dropdown-menu class="dropdown-menu">
                                <el-dropdown-item v-if="store.getters.userinfo.role==='ADMIN'" @click="router().push({ path: '/Admin' })">管理控制台</el-dropdown-item>
                                <el-dropdown-item @click="router().push({ path: '/Orders' })">消费记录
                                </el-dropdown-item>
<!--                                <el-dropdown-item>网站公告</el-dropdown-item>-->
<!--                                <el-dropdown-item divided>账号设置</el-dropdown-item>-->
                                <el-dropdown-item divided @click="logout()">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>
        </div>
        <div v-else @click="dialogVisible = true" class="login">登录</div>
    </nav>

    <el-dialog v-model="appletDialogVisible" title="Ai" width="400" center align-center>
        <div class="wxAppletCodeRow">
            <img class="wxAppletCode" :src="require('../assets/wxAppletCode.jpg')">
            <div>微信扫一扫</div>
        </div>
    </el-dialog>

    <LoginDialog :show="dialogVisible" @close="dialogVisible = false"/>
</template>

<script>
import {defineComponent, ref, watch} from "vue";
import {useRouter} from "vue-router";
import {UserFilled} from '@element-plus/icons-vue'
import router from "@/router";
import store from "../store";
import LoginDialog from "@/components/LoginDialog.vue";
// eslint-disable-next-line no-unused-vars
import {FavoritesDel, logOutOfLogin} from "../../api/BSideApi";
import {ElLoading, ElNotification} from "element-plus";

export default defineComponent({
    name: "NavigationBar",
    components: {LoginDialog},
    computed: {
        store() {
            return store
        }
    },
    methods: {
        router() {
            return router
        }
    },
    setup() {
        let router = useRouter();
        // TODO DATA
        let dialogVisible = ref(false)
        let appletDialogVisible = ref(false)
        let isHeadNavigation = ref(false)
        let navigationList = ref([{
            title: '智能对话',
            to: '/'
        }, {
            title: '灵感创作',
            to: '/create'
        }, {
            title: '创意中心',
            to: '/originality'
        }
        ])

        watch(() => router.currentRoute.value, (newValue) => {
            isHeadNavigation.value = newValue.meta.isHeadNavigation;
        }, {
            immediate: true
        })

        const dropdown1 = ref()

        function showClick() {
            dropdown1.value.handleOpen()
        }

        // eslint-disable-next-line no-unused-vars
        async function logout() {
            try {
                // 显示加载图标
                ElLoading.service({
                    fullscreen: true,
                    text: '正在退出登录...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)',
                });
                await logOutOfLogin();
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                location.reload();
                // 关闭加载图标
                ElLoading.service().close();
                // 弹出退出登录成功提示框
                ElNotification({
                    message: '退出登录成功',
                    type: 'success',
                });

            } catch (e) {
                // 关闭加载图标
                ElLoading.service().close();
                ElNotification({
                    message: e,
                    type: 'error',
                });
            }
        }


        return {
            isHeadNavigation,
            navigationList,
            UserFilled,
            showClick,
            dropdown1,
            appletDialogVisible,
            dialogVisible,
            logout
        };
    },
});
</script>

<style scoped>
.header {
    user-select: none;
    height: 60px;
    box-sizing: border-box;
    width: 100%;
    border-bottom: 1px solid #e8e8e8;
    flex-shrink: 0;
    justify-content: space-between;
    align-items: center;
    padding: 0 16px;
    display: flex;
    position: relative;
    overflow: hidden;
}

.header-center {
    position: absolute;
    left: 50%;
    transform: translate(-50%);
}

@media only screen and (max-width: 767px) {
    .header-center {
        left: 16px;
        transform: translate(0);
    }
}

.switch-button {
    box-sizing: border-box;
    height: 34px;
    background: #e6e6e6;
    border-radius: 7px;
    align-items: center;
    padding: 0 2px;
    display: flex;
}

.switch-item {
    height: 30px;
    cursor: pointer;
    border-radius: 5px;
    justify-content: center;
    align-items: center;
    font-size: 14px;
    font-weight: 500;
    display: flex;
    color: #303030;
    text-decoration: none;
    box-sizing: border-box;
    padding: 0 16px;
}

.switch-active {
    background: #fff;
}

.header-side {
    height: 100%;
    align-items: center;
    display: flex;
}

.logo {
    cursor: pointer;
    font-size: 20px;
    font-weight: bold;
    color: var(--el-text-color-primary);
    text-decoration: none;
    animation: logoAnimation 0.3s;
}

@keyframes logoAnimation {
    from {
        transform: translateX(-100%);
    }

    to {
        transform: translateX(0);
    }
}

.header-right {
    animation: headerRightAnimation 0.3s;
}

.rechargeButton {
    width: 84px;
    height: 34px;
    border-radius: 6px;
    margin-right: 12px;
    padding: 0;
    font-size: 14px;
    background-color: var(--el-text-color-primary);
    color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}

.rechargeButtonText {
    margin-left: 10px;
}

@keyframes headerRightAnimation {
    from {
        transform: translateX(100%);
    }

    to {
        transform: translateX(0);
    }
}

.header-user-btn {
    height: 36px;
    cursor: pointer;
    border-radius: 6px;
    align-items: center;
    padding: 0 6px;
    display: flex;
}

.header-user-btn:hover {
    background: #e6e6e6;
}

.header-user-name {
    max-width: 120px;
    white-space: nowrap;
    text-overflow: ellipsis;
    word-break: break-all;
    margin-left: 8px;
    font-size: 15px;
    overflow: hidden;
}

.dropdown-menu {
    width: 150px;
    box-sizing: border-box;
    padding: 6px;
}

.applet {
    width: 120px;
    margin-left: 10px;
    background-color: #686efe;
}

.applet > .rechargeButtonText {
    margin-left: 5px;
}

.appletIcon {
    width: 18px;
    height: 18px;
}

.wxAppletCodeRow {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.wxAppletCode {
    width: 150px;
    height: 150px;
    margin: 20px 0;
}

.login {
    width: 72px;
    height: 34px;
    background: #686efe;
    border-radius: 6px;
    padding: 0;
    font-size: 14px;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
}
</style>
