<template>
    <el-dialog v-model="dialogVisible" width="400px" class="login-dialog" :show-close="false" align-center
               @close="close">
        <div class="login-box" v-if="!adminVisible">
            <div class="head">
                <img :src="require('../assets/login-header.png')" alt=""/>
            </div>
            <div class="form">
                <img class="profile-avatar" :src="require('../assets/logoHead.svg')" alt="" @click="adminVisible=true"
                />
                <div class="content">
                    <el-form @keyup.enter="onSubmit" ref="formRef" size="large">
                        <el-form-item prop="username">
                            <el-input
                                    ref="usernameRef"
                                    type="text"
                                    clearable
                                    v-model="email"
                                    placeholder="请输入邮箱号码"
                            >
                                <template #prefix>
                                    <el-icon :size="16" color="var(&#45;&#45;el-input-icon-color)">
                                        <UserFilled/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input
                                    ref="passwordRef"
                                    v-model="password"
                                    type="password"
                                    placeholder="请输入密码"
                                    show-password
                            >
                                <template #prefix>
                                    <el-icon :size="16" color="var(&#45;&#45;el-input-icon-color)">
                                        <Platform/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="code" v-show="isLogin===false">
                            <el-input
                                    maxlength="6"
                                    minlength="6"
                                    ref="codeRef"
                                    type="text"
                                    clearable
                                    v-model="code"
                                    placeholder="请输入验证码"
                            >
                                <template #prefix>
                                    <el-icon :size="16" color="var(&#45;&#45;el-input-icon-color)">
                                        <Connection/>
                                    </el-icon>
                                </template>
                                <template #append>
                                    <el-button :disabled="disabled" @click="startCountdown"
                                               v-text="buttonText"></el-button>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button :loading="loginLoading" class="submit-button" round type="primary" size="large"
                                       @click="onSubmit">
                                {{ isLogin ? '登录' : '注册' }}
                            </el-button>
                        </el-form-item>
                    </el-form>
                    <div @click="isLogin = !isLogin" class="switch">{{ !isLogin ? '前往登录' : '前往注册' }}</div>
                </div>
            </div>
        </div>
        <div class="login-box" v-else>
            <div class="head">
                <img :src="require('../assets/login-header.png')" alt=""/>
            </div>
            <div class="form">
                <img class="profile-avatar" :src="require('../assets/logoHead.svg')" alt="" @click="adminVisible=false"
                     style="filter: brightness(1) hue-rotate(20deg) saturate(110%);"
                />
                <div class="content">
                    <el-form @keyup.enter="onSubmit" ref="formRef" size="large">
                        <el-form-item prop="username">
                            <el-input
                                    ref="usernameRef"
                                    type="text"
                                    clearable
                                    v-model="email"
                                    placeholder="请输入账号"
                            >
                                <template #prefix>
                                    <el-icon :size="16" color="var(&#45;&#45;el-input-icon-color)">
                                        <UserFilled/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input
                                    ref="passwordRef"
                                    v-model="password"
                                    type="password"
                                    placeholder="请输入密码"
                                    show-password
                            >
                                <template #prefix>
                                    <el-icon :size="16" color="var(&#45;&#45;el-input-icon-color)">
                                        <Platform/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>

                        <el-form-item>
                            <el-button :loading="loginLoading" class="submit-button" round type="primary" size="large"
                                       @click="onSubmit">
                                验证身份
                            </el-button>
                        </el-form-item>
                    </el-form>

                </div>
            </div>
        </div>
    </el-dialog>
</template>

<script>
import {defineComponent, ref, watch} from "vue";
import store from "@/store";
import {Connection, Platform, UserFilled} from "@element-plus/icons-vue";
import {EmailEnroll, EmailLogin, GetCode, GetUserInfo, ServerLogin} from "../../api/BSideApi";
import {ElMessage, ElNotification} from "element-plus";
export default defineComponent({
    name: "LoginDialog",
    components: {Platform, UserFilled, Connection},
    props: {
        show: {
            type: Boolean,
            default: false
        }
    },
    setup(props, {emit}) {
        let email = ref("")
        let password = ref("")
        let code = ref("")
        let loginLoading = ref(false)
        let dialogVisible = ref(false)
        let isLogin = ref(true)
        //code
        const disabled = ref(false)
        const buttonText = ref('获取验证码')
        const countdown = ref(null)
        const adminVisible = ref(false)


        watch(() => props.show, (newValue) => {
            if (newValue) dialogVisible.value = true
        }, {
            immediate: true
        })

        async function startCountdown() {
            if (!email.value) {
                ElMessage.warning('验证邮箱不能为空')
                return
            }
            let seconds = 120
            try {
                await GetCode({
                    email: email.value,
                });
                ElMessage.info('验证码发送成功')
                disabled.value = true
            } catch (e) {
                ElNotification({
                    title: '错误',
                    message: e,
                    type: 'error',
                })
                return
            }
            countdown.value = setInterval(() => {
                if (seconds === 0) {
                    clearInterval(countdown.value)
                    countdown.value = null
                    disabled.value = false
                    buttonText.value = '重新获取验证码'
                } else {
                    seconds--
                    buttonText.value = `${seconds}` + '后重新获取'
                }
            }, 1000)
        }


        function onSubmit() {
            if (adminVisible.value) {
                onSubmitLogin(true)
            } else {
                isLogin.value ? onSubmitLogin(false) : register()
            }

        }

        async function onSubmitLogin(isAdmin) {
            try {
                if (!email.value) {
                    ElMessage.warning('登录邮箱不能为空')
                    return
                }

                if (!password.value) {
                    ElMessage.warning('登陆密码不能为空')
                    return
                }
                loginLoading.value = true
                let res;
                if (isAdmin) {
                    res = await ServerLogin({
                        email: email.value,
                        password: password.value
                    })

                } else {
                    res = await EmailLogin({
                        email: email.value,
                        password: password.value
                    })

                }
                localStorage.setItem('token', res);
                dialogVisible.value = false
                loginLoading.value = false
                try {
                    let res = await GetUserInfo()
                    store.commit("setUserinfo", res);
                    // eslint-disable-next-line no-empty
                } catch (e) {
                    console.log(e)
                }

                ElNotification({
                    title: '登录成功',
                    message: '欢迎来到Ai',
                    type: 'success',
                })

                emit('loginSucceeded')
            } catch (e) {
                loginLoading.value = false
                ElNotification({
                    title: '错误',
                    message: e,
                    type: 'error',
                })
            }
        }

        async function register() {
            try {
                if (!email.value) {
                    ElMessage.warning('注册邮箱不能为空')
                    return
                }
                if (!password.value) {
                    ElMessage.warning('登陆密码不能为空')
                    return
                }
                if (!code.value) {
                    ElMessage.warning('验证码不能为空')
                    return
                }
                loginLoading.value = true
                await EmailEnroll({
                    email: email.value,
                    password: password.value,
                    code: code.value
                })
                ElNotification({
                    title: '注册成功',
                    message: '快登录体验Ai吧',
                    type: 'success',
                })
                loginLoading.value = false
                isLogin.value = true
            } catch (e) {
                loginLoading.value = false
                ElNotification({
                    title: '错误',
                    message: e,
                    type: 'error',
                })
            }
        }

        function close() {
            emit('close')
        }

        return {
            email,
            code,
            password,
            loginLoading,
            dialogVisible,
            close,
            onSubmit,
            adminVisible,
            isLogin,
            disabled,
            buttonText,
            startCountdown
        }
    }
});
</script>

<style scoped>

.inputBox {
    height: 40px;
    width: 300px;
}

.login-box {
    overflow: hidden;
    width: 100%;
    padding: 0;
    background: #FFFFFF;
}

.login-box > .head {

}

.login-box > .head > img {
    display: block;
    width: 100%;
    margin: 0 auto;
    user-select: none;
}

.form {
    position: relative;
}

.submit-button {
    width: 100%;
    letter-spacing: 2px;
    font-weight: 300;
    margin-top: 15px;
    --el-button-bg-color: #686efe;
    border: none;
}

.submit-button:hover, .submit-button:focus, .submit-button:active {
    background-color: #7d80ff;
    outline: 0;
}


.form > .content {
    padding: 100px 40px 40px 40px;
}

.profile-avatar {
    display: block;
    position: absolute;
    height: 100px;
    width: 100px;
    border-radius: 50%;
    border: 4px solid #ffffff;
    top: -50px;
    right: calc(50% - 50px);
    z-index: 2;
    user-select: none;
}

.switch {
    text-align: center;
    font-size: 14px;
    cursor: pointer;
}
</style>
