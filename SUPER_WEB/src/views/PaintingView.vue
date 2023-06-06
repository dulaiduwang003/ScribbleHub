<template>
    <div class="body" ref="scrollRef">
        <div v-if="!conversationList.length" class="explain">
            <img class="logo" alt="Vue logo" src="../assets/painting_big.png">
            <div class="expositoryCase">欢迎使用神经 AI绘画</div>
            <div class="consume">
                <el-icon>
                    <Goods/>
                </el-icon>
                <div class="consumeText">一张图片消耗5个Ai币</div>
            </div>
            <div class="beCareful">请注意不支持违法、违规等不当信息内容</div>
        </div>
        <div v-else class="questions">
            <div v-for="(item,index) in conversationList" :key="index" class="item">
                <div class="question">
                    <el-avatar style="background-color: #b6ccee" class="flexShrink" :size="28" :icon="UserFilled"
                               :src="require('../assets/my.png')"/>
                    <div class="text">{{ item.user }}</div>
                </div>
                <div class="answer">
                    <el-avatar class="flexShrink" :size="28" :icon="UserFilled"
                               :src="require('../assets/painting_small.png')" style="background-color: #b6ccee"/>
                    <div v-if="item.error" class="text">{{ item.error }}</div>
                    <div v-else-if="item.url" class="imgPreview">
                        <el-row :gutter="15">
                            <el-col :xs="12" :sm="8" :md="6"
                                    style="margin-bottom: 15px">
                                <el-image :src="item.url" :preview-src-list="picturePreview(item.url)" fit="contain">
                                    <template #placeholder>
                                        <div class="image-slot">
                                            <img class="image-slot-img" src="../assets/load.svg"/>
                                        </div>
                                    </template>
                                </el-image>
                            </el-col>
                        </el-row>

                    </div>
                    <div v-else class="typing"></div>
                </div>
            </div>
        </div>

        <div class="footer">
            <div class="footer-bar">
                <el-input @keyup.enter="onSubmit" v-model="input" :placeholder="aiPrompt"
                          :disabled="aiLoading"></el-input>
                <div style="display: flex;padding-right: 10px" v-show="aiLoading">
                    <div class="dot0"></div>
                    <div class="dot1"></div>
                    <div class="dot2"></div>
                    <div class="dot3"></div>
                    <div class="dot4"></div>
                </div>
                <div @click="onSubmit" class="sendIcon" v-show="!aiLoading">
                    <el-icon :size="20">
                        <Promotion/>
                    </el-icon>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {onMounted, ref} from "vue";
import {Goods, Promotion, UserFilled} from "@element-plus/icons-vue";
import {GetUserInfo, Painting} from "../../api/BSideApi";
import {ElNotification} from "element-plus";
import store from "@/store";

export default {
    name: "PaintingView",
    components: {Promotion, Goods},
    computed: {
        UserFilled() {
            return UserFilled
        }
    },
    setup() {
        let scrollRef = ref(null);
        let input = ref('')
        let conversationList = ref([])
        let aiLoading = ref(false)
        let aiPrompt = ref('请提供创意词 如 美女,天使，女武神 使用逗号隔开')


        onMounted(() => {
            if (store.getters.userinfo) getUser()
        })

        async function onSubmit() {
            if (input.value === '') return
            let index = conversationList.value.length
            aiLoading.value = true;
            aiPrompt.value = '正在根据创意词构建作品....'
            try {
                let content = input.value
                input.value = ''
                conversationList.value.push({
                    user: content
                })
                scrollToTheBottom()
                let res = await Painting({
                    prompt: content,
                })

                // TODO 滚动到底部
                scrollToTheBottom()
                conversationList.value[index].url = res
                aiPrompt.value = '请提供创意词 如 美女,天使,女武神 使用逗号隔开'
                aiLoading.value = false;
                await getUser();
            } catch (err) {
                await getUser();
                aiPrompt.value = '请提供创意词 如 美女,天使,女武神 使用逗号隔开'
                aiLoading.value = false;
                scrollToTheBottom()
                conversationList.value[index].error = err
            }
        }

        async function getUser() {
            try {
                let res = await GetUserInfo()
                store.commit("setUserinfo", res);
            } catch (e) {
                ElNotification({
                    message: '获取用户数据失败',
                    type: 'error',
                })
            }
        }

        // TODO 滚动到底部
        function scrollToTheBottom() {
            setTimeout(() => scrollRef.value.scrollTop = scrollRef.value.scrollHeight, 20);
        }

        // TODO 预览格式转换
        function picturePreview(url) {
            let image = [];
            image.push(url)
            return image;
        }

        return {
            onSubmit, input, conversationList, scrollRef, picturePreview, aiLoading, aiPrompt
        }
    }
}
</script>

<style scoped>
.slide-animation {
    animation: slideEase 0.5s ease-in-out forwards;
}

@keyframes slideEase {
    0% {
        transform: translateX(-100px);
    }
    100% {
        transform: translateX(0);
    }
}


@keyframes jumpT {
    0%,
    80%,
    100% {
        transform: scale(0);
        background-color: #F9F9F9;
    }
    40% {
        transform: scale(1.0);
        background-color: rgb(182, 204, 238);
    }
}

.dot0,
.dot1,
.dot2,
.dot3 {
    background: #6585D5FF;
    width: 10px;
    height: 10px;
    border-color: #464646;
    border-radius: 50%;
}


.dot0 {
    animation: jumpT 1.3s -0.64s linear infinite;
}

.dot1 {
    animation: jumpT 1.3s -0.32s linear infinite;
}

.dot2 {
    animation: jumpT 1.3s -0.16s linear infinite;
}

.dot3 {
    animation: jumpT 1.3s linear infinite;
}

.body {
    scroll-behavior: smooth;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    flex-direction: column;
    flex: 1;
    align-items: center;
    padding: 0 20px 120px;
    display: flex;
    overflow: auto;
}


@keyframes footerBarAnimation {
    from {
        transform: translateY(150%);
    }

    to {
        transform: translateY(0);
    }
}

.footer-bar {
    min-height: 60px;
    max-width: 800px;
    width: 100%;
    pointer-events: auto;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
    display: flex;
    align-items: center;
    animation: footerBarAnimation 0.3s;
}


.footer {
    width: 100%;
    box-sizing: border-box;
    z-index: 1;
    pointer-events: none;
    background: linear-gradient(rgba(246, 246, 246, 0), #f6f6f6 25%);
    flex-shrink: 0;
    justify-content: center;
    padding: 30px 20px;
    display: flex;
    position: absolute;
    bottom: 0;
    overflow: hidden;
}

>>> .footer-bar > .el-input > .el-input__wrapper {
    box-shadow: none;
    font-size: 16px;
    box-sizing: border-box;
    width: 100%;
    min-height: 60px;
    resize: none;
    -webkit-appearance: none;
    background: 0 0;
    border: 0;
    flex: 1;
    margin: 0;
    padding: 16px 20px;
    line-height: 28px;
}

>>> .footer-bar > .el-input > .el-input-group__prepend {
    box-shadow: none;
    font-size: 16px;
    box-sizing: border-box;
    min-height: 60px;
    resize: none;
    -webkit-appearance: none;
    background: 0 0;
    border: 0;
    margin: 0;
    padding: 0;
    line-height: 28px;
}

>>> .footer-bar > .el-input > .el-input-group__prepend > .el-select {
    margin: 0;
}

>>> .footer-bar > .el-input > .el-input-group__prepend > .el-select > .select-trigger > .el-input > .el-input__wrapper {
    box-shadow: none !important;
    font-size: 15px;
    height: 62px;
    padding: 0 20px;
}


.sendIcon {
    flex-shrink: 0;
    margin: 0 12px;
    width: 36px;
    height: 36px;
    color: #fff;
    cursor: pointer;
    background: rgb(81, 126, 231);
    border-radius: 50%;
    justify-content: center;
    align-items: center;
    display: flex;
}


.questions {
    width: 100%;
    max-width: 800px;
    box-sizing: border-box;
    padding: 0 32px;
}

@media only screen and (max-width: 767px) {
    .questions {
        padding: 0;
    }
}

.questions > .item {
    box-sizing: border-box;
    background: #fff;
    border: 1px solid #ececec;
    border-radius: 8px;
    flex-direction: column;
    margin-top: 26px;
    padding: 0 20px;
    display: flex;
    overflow: hidden;
}

.flexShrink {
    flex-shrink: 0;
}

.question, .answer {
    padding: 20px 8px;
    display: flex;
}

.answer {
    border-top: 1px solid #ececec;
    position: relative;
}

.question > .text, .answer > .text {
    min-height: 28px;
    box-sizing: border-box;
    white-space: pre-wrap;
    flex: 1;
    margin-left: 16px;
    font-size: 16px;
    line-height: 28px;
    position: relative;
}

.imgPreview {
    margin-left: 16px;
    flex: 1;
    width: 0;
}

>>> .mdPreview > .vuepress-markdown-body {
    padding: 0;
    color: #303030;
}

.explain {
    margin: auto;
    display: flex;
    flex-direction: column;
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

.logo {
    width: 100px;
    margin-bottom: 20px;
    animation: beating 0.7s infinite alternate;
}

@keyframes beating {
    0% {
        transform: translateY(0);
    }
    100% {
        transform: translateY(-10px);
    }
}


.expositoryCase {
    font-size: 20px;
    margin-top: 15px;
    text-align: center;
}

.consume {
    display: flex;
    align-items: center;
    margin-top: 30px;
}

.consumeText {
    margin-left: 10px;
    font-size: 15px;
}

.beCareful {
    padding: 40px 6px 12px;
    color: #848484;
    font-size: 15px;
    line-height: 1.6;
}

.typing {
    height: 20px;
    width: 3px;
    background-color: var(--el-text-color-primary);
    border-radius: 100px;
    margin-top: 4px;
    margin-left: 16px;
    animation: typingAnimation 0.6s linear infinite;
}

@keyframes typingAnimation {
    from {
        visibility: hidden;
    }
    50% {
        visibility: hidden;
    }
    to {
        visibility: visible;
    }
}

.image-slot {
    width: 100%;
    height: 100%;
    background-color: #f6f6f6;
    display: flex;
    justify-content: center;
    align-items: center;
}

.image-slot-img {
    width: 20px;
    height: 20px;
}
</style>
