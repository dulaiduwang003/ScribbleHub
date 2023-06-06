<template>
    <el-scrollbar height="500px">
        <div class="s-questions">
            <div style="font-size: 29px;padding-bottom: 50px">服务配置</div>
            <el-form label-width="130px" label-position="left" :inline="true">
                <el-form-item label="服务器策略">
                    <el-select
                            v-model="choose"
                            placeholder="选择策略"
                        style="width: 200px"
                        >
                        <el-option label="直连模式" value="DIRECT"/>
                        <el-option label="代理模式" value="AGENT"/>
                        <el-option label="自定义模式" value="CUSTOM"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="官方密钥">
                    <el-input v-model="officialKey"/>
                </el-form-item>
                <el-form-item label="官方API">
                    <el-input v-model="officialUrl"/>
                </el-form-item>
                <el-form-item label="自定义密钥">
                    <el-input v-model="customKey"/>
                </el-form-item>
                <el-form-item label="自定义API">
                    <el-input v-model="customBaseUrl"/>
                </el-form-item>
                <el-form-item label="Clash代理IP">
                    <el-input v-model="proxyIp"/>
                </el-form-item>
                <el-form-item label="Clash代理端口">
                    <el-input v-model="proxyPort"/>
                </el-form-item>
                <el-form-item label="SD API">
                    <el-input v-model="mappingSdUrl"/>
                </el-form-item>
                <el-form-item label="MjServerID">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="MjChannelID">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="MjBotToken">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="BingCookie">
                    <el-input v-model="bingCookie"/>
                </el-form-item>
            </el-form>

        </div>
        <div style="text-align: center;padding-top: 50px">
            <el-button type="primary" style="background-color: rgb(104,110,254);color: white" size="large"
                       @click="submit">
                保存服务器配置
            </el-button>
        </div>
    </el-scrollbar>
</template>

<script>
import {onMounted, ref} from "vue";
import store from "@/store";
import {GetServer, PutServer} from "../../../api/BSideApi";
import {ElNotification} from "element-plus";


export default {
    name: "ordersView",
    computed: {
        store() {
            return store
        }
    },

    setup() {
        const bingCookie = ref('')
        const customBaseUrl = ref('')
        const customKey = ref('')
        const mappingMjUrl = ref('')
        const mappingSdUrl = ref('')
        const officialUrl = ref('')
        const officialKey = ref('')
        const proxyIp = ref('')
        const proxyPort = ref('')
        const choose = ref('')


        onMounted(() => {
            init()
        })

        async function init() {
            try {
                let data = await GetServer();
                bingCookie.value = data.bing.cookie
                customBaseUrl.value = data.custom.baseUrl
                customKey.value = data.custom.key
                mappingMjUrl.value = data.mapping.mjUrl
                mappingSdUrl.value = data.mapping.sdUrl
                officialUrl.value = data.official.baseUrl
                officialKey.value = data.official.key
                proxyIp.value = data.proxy.ip
                proxyPort.value = data.proxy.port
                choose.value = data.choose
                // eslint-disable-next-line no-empty
            } catch (e) {

            }
        }

        async function submit() {
            try {
                // eslint-disable-next-line no-unused-vars
                await PutServer(
                    {
                        "proxy": {
                            "ip": proxyIp.value,
                            "port": proxyPort.value,
                        },
                        "custom": {
                            "baseUrl": customBaseUrl.value,
                            "key": customKey.value
                        },
                        "bing": {
                            "cookie": bingCookie.value
                        },
                        "official": {
                            "baseUrl": officialUrl.value,
                            "key": officialKey.value
                        },
                        "mapping": {
                            "mjUrl": mappingMjUrl.value,
                            "choice": "SD",
                            "sdUrl": mappingSdUrl.value
                        },
                        "choose": choose.value
                    }
                );
                ElNotification({
                    title: '操作成功',
                    message: '数据已被重置,30秒后自动接入配置',
                    type: 'success',
                })
            } catch (e) {
                ElNotification({
                    title: '操作失败',
                    message: e,
                    type: 'error',
                })
            }
        }

        return {
            submit,
            init,
            customBaseUrl,
            customKey,
            mappingMjUrl,
            mappingSdUrl,
            officialUrl,
            officialKey,
            choose,
            proxyPort,
            proxyIp,
            bingCookie
        };

    }

}
</script>

<style scoped>
.s-questions {
    padding-left: 200px;
    padding-top: 60px;
    height: 100%;
    animation: explainAnimation 0.3s;

}

@keyframes explainAnimation {
    from {
        transform: scale(0);
    }

    to {
        transform: scale(1);
    }
}


</style>
