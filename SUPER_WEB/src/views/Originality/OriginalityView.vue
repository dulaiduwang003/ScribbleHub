<template>
    <div class="questions">
        <div class="tab-box">
            <el-tabs v-model="activeName">
                <el-tab-pane label="我的创作" name="first" v-if="store.getters.userinfo">
                    <ViewState class="state" v-if="!store.getters.userinfo" Type="error"
                               ErrorText="登录后查看我的作品" IsShowBottom ButtonText="登录"
                               @ClickTheButton="loginVisible = true"/>
                    <ViewState class="state" v-else-if="load" LoadText="正在加载，请稍后..."/>
                    <ViewState class="state" v-else-if="empty" Type="empty" ErrorText="暂无任何创作的数据"/>
                    <ViewState class="state" v-else-if="error" @ClickTheButton="init" Type="error"
                               ErrorText="加载错误，请重试"
                               IsShowBottom ButtonText="重新加载"/>
                    <my-work v-else :images="images" @flushed="flushed"/>
                </el-tab-pane>
            </el-tabs>
        </div>
        <LoginDialog :show="loginVisible" @close="loginVisible = false" @loginSucceeded="init"/>
    </div>
</template>

<script>
import {onMounted, ref} from "vue";
import {IndividualPicture} from "../../../api/BSideApi";
import MyWork from "@/views/Originality/MyWork.vue";

import ViewState from "@/components/ViewState.vue";
import store from "@/store";
import LoginDialog from "@/components/LoginDialog.vue";

export default {
    name: "PromptList",
    computed: {
        store() {
            return store
        }
    },
    components: {LoginDialog, ViewState, MyWork},
    setup() {
        const activeName = ref('first')
        let loginVisible = ref(false)
        const images = ref([])
        let load = ref(false)
        let empty = ref(false)
        let error = ref(false)

        async function init() {
            try {
                load.value = true;
                let res = await IndividualPicture();
                if (res.length) {
                    images.value = res
                } else {
                    empty.value = true
                }
                load.value = false
                error.value = false
            } catch (e) {
                load.value = false
                error.value = true
            }
        }

        async function flushed() {
            try {
                load.value = true;
                let res = await IndividualPicture();
                if (res.length) {
                    images.value = res
                } else {
                    empty.value = true
                }
                load.value = false
                error.value = false
            } catch (e) {
                load.value = false
                error.value = true
            }
        }

        onMounted(() => {
            init()
        })

        return {
            load,
            init,
            empty,
            error,
            images,
            activeName,
            loginVisible,
            flushed
        };
    }

}
</script>

<style scoped>
.questions {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
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

.state {
    margin-top: 100px;
    margin-bottom: 10px
}

.tab-box {
    background-color: white;
    width: 93%;
    height: 90%;
    border-radius: 15px;
    padding: 20px
}


</style>
