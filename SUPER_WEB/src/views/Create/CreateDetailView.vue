<template>
    <ViewState v-if="load" LoadText="正在生成，请稍后..."/>
    <ViewState v-else-if="error" @ClickTheButton="back" Type="error" ErrorText="AI服务调用失败，正在紧急处理，请稍后使用。"
               IsShowBottom ButtonText="返回"/>
    <div v-else class="body">
        <div class="container">
            <div class="title">{{ item }}</div>
            <div class="result">
                <div class="completeText">
                    <v-md-editor :model-value="content" mode="preview"></v-md-editor>
                </div>
            </div>
            <button @click="router().back()" class="determineTheBuildBack" type="primary">返回上一页</button>
        </div>
    </div>
</template>

<script>
import {useRouter} from 'vue-router'
import {onMounted, ref} from "vue";
import ViewState from "@/components/ViewState.vue";
import router from "@/router";
import {GetUserInfo, Inspiration} from "../../../api/BSideApi";
import store from "@/store";
import {ElNotification} from "element-plus";


export default {
    name: "CreateDetailView",
    methods: {
        router() {
            return router
        }
    },
    components: {ViewState},
    setup: function () {
        let router = useRouter()
        let query = router.currentRoute.value.query;
        let role = ref(query.role ? JSON.parse(decodeURIComponent(query.role)) : false)
        let item = ref(JSON.parse(decodeURIComponent(query.item)));
        let load = ref(false)
        let error = ref(false)
        let content = ref('')
        let messages = ref([])

        // TODO 系统角色
        if (role.value) messages.value.push({
            role: 'system',
            content: role.value
        })

        // TODO 我
        messages.value.push({
            role: 'user',
            content: item.value
        })

        onMounted(() => {
            send()
        });

        async function send() {
            try {
                load.value = true
                let data = await Inspiration({
                    "messages": messages.value
                });
                let res = JSON.parse(data);
                content.value = res.choices[0].message.content
                load.value = false
            } catch (e) {
                content.value = e
                load.value = false
                error.value = true
            } finally {
                await getUser()
            }
        }

        async function getUser() {
            try {
                let res = await GetUserInfo()
                store.commit("setUserinfo", res);
            } catch (e) {
                ElNotification({
                    title: '错误',
                    message: e,
                    type: 'error',
                })
            }
        }

        function back() {
            router.go(-1);
        }

        return {
            load, error, content, item, back
        }
    }
}
</script>

<style scoped>
.body {
    scroll-behavior: smooth;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    flex-direction: column;
    flex: 1;
    align-items: center;
    padding: 0 20px 100px;
    display: flex;
    overflow: auto;
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
.container {
    max-width: 800px;
    width: 100%;
    box-sizing: border-box;
    padding: 0 20px 100px;
}

.title {
    font-size: 20px;
    font-weight: 500;
    margin-top: 60px;
    margin-bottom: 30px;
}

.result {
    min-height: 60px;
    max-width: 800px;
    width: 100%;
    background: #fff;
    border-radius: 8px;
    flex-direction: column;
    display: flex;
    position: relative;
    box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
}

.completeText {
    box-sizing: border-box;
    width: 100%;
    min-height: 28px;
    white-space: pre-wrap;
    flex: 1;
    padding: 16px 20px;
    font-size: 16px;
    line-height: 28px;
    position: relative;
}

>>> .mdPreview > .vuepress-markdown-body {
    padding: 0;
    color: #303030;
}

@media only screen and (max-width: 767px) {
    .container {
        padding: 0;
    }
}

.determineTheBuildBack {
    font-size: 15px;
    background-color: white;
    color: #303030;
    border-radius: 100px;
    height: 40px;
    width: 300px;
    max-width: 100%;
    line-height: 40px;
    border: 0;
    display: table;
    margin: 30px auto 30px auto;
    cursor: pointer;
}

.determineTheBuildBack:hover {
    background: #f0f0f0;
}
</style>
