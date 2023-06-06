<template>
    <el-scrollbar height="500px">
        <div class="o-questions">
            <div style="font-size: 29px;padding-bottom: 50px">功能消耗次数</div>
            <el-form label-width="130px" label-position="left" :inline="true" v-model="form">
                <el-form-item label="ChatGPT3.5">
                    <el-input v-model="form.chatThreeFrequency"/>
                </el-form-item>
                <el-form-item label="ChatGPT4.0">
                    <el-input v-model="form.chatFourFrequency"/>
                </el-form-item>
                <el-form-item label="NewBing">
                    <el-input v-model="form.bingFrequency"/>
                </el-form-item>
                <el-form-item label="Mapping">
                    <el-input v-model="form.mappingFrequency"/>
                </el-form-item>

                <el-form-item label="Internet GPT">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="Function GPT">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="Function Music">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="Ai Contended">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
                <el-form-item label="Girl Friend">
                    <el-input placeholder="禁用" disabled/>
                </el-form-item>
            </el-form>
        </div>

        <div class="o-questions">
            <div style="font-size: 29px;padding-bottom: 50px">功能性设置</div>
            <el-form label-width="120px" v-model="form">
                <el-form-item label="新用户奖励次数">
                    <el-col :span="11">
                        <el-input v-model="form.userFrequency"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="小程序激励次数">
                    <el-col :span="11">
                        <el-input placeholder="禁用" disabled/>
                    </el-col>
                </el-form-item>
                <el-form-item label="域名(公网)">
                    <el-col :span="11">
                        <el-input v-model="form.alipayCallbackUrl"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="支付宝APPID">
                    <el-col :span="11">
                        <el-input v-model="form.alipayAppid"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="支付宝公钥">
                    <el-col :span="11">
                        <el-input v-model="form.alipayPublic"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="支付宝私钥">
                    <el-col :span="11">
                        <el-input v-model="form.alipayPrivate"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="记忆(倍数)">
                    <el-col :span="11">
                        <el-input v-model="form.memory"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="上下文压缩">
                    <el-switch :value="true"/>
                </el-form-item>
                <el-form-item label="对话缓存(GPT)" >
                    <el-switch/>
                </el-form-item>
                <el-form-item label="绘图首选">
                    <el-select placeholder="禁用" disabled>
                        <el-option label="SD优先" value="shanghai"/>
                        <el-option label="MJ优先" value="beijing"/>
                    </el-select>
                </el-form-item>
            </el-form>
        </div>
        <div style="text-align: center;padding-top: 50px">
            <el-button type="primary" style="background-color: rgb(104,110,254);color: white" size="large" @click="submit">保存设置
            </el-button>
        </div>
    </el-scrollbar>

</template>

<script>
import {ref, onMounted} from "vue";
import store from "@/store";
import {GetOperation, PutOperation} from "../../../api/BSideApi";
import {ElNotification} from "element-plus";


export default {
    name: "OperationConfig",
    computed: {
        store() {
            return store
        }
    },
    setup() {
        const form = ref({
            alipayAppid: '',
            alipayCallbackUrl: '',
            alipayPrivate: '',
            alipayPublic: '',
            bingFrequency: undefined,
            chatFourFrequency: undefined,
            chatThreeFrequency: undefined,
            mappingFrequency: undefined,
            memory: undefined,
            userFrequency: undefined

        })
        onMounted(() => {
            init()
        })

        async function init() {

            try{
                let res = await GetOperation();
                if (res){
                    form.value=res
                }
            }catch (e) {

            }
        }

        async function submit() {
            try {
                await PutOperation(form.value);
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
            form,
            submit
        };
    }

}
</script>

<style scoped>
.o-questions {
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
