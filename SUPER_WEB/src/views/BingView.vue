<template>
  <div class="body" ref="scrollRef">
    <div v-if="!conversationList.length" class="explain">
      <img class="logo" alt="Vue logo" src="../assets/bing_big.png" />
      <div class="expositoryCase">欢迎使用微软新必应对话</div>
      <div class="consume">
        <el-icon>
          <Goods />
        </el-icon>
        <div class="consumeText">每次提问消耗1次Ai币</div>
      </div>
      <div class="beCareful">请注意不支持违法、违规等不当信息内容</div>
    </div>
    <div v-else class="questions">
      <div
        v-for="(item, index) in conversationList"
        :key="index"
        class="item slide-animation"
      >
        <div class="question">
          <el-avatar
            class="flexShrink"
            :size="28"
            :icon="UserFilled"
            :src="require('../assets/my.png')"
            style="background-color: #ffeed3"
          />
          <div class="text">{{ item.user }}</div>
        </div>
        <div class="answer">
          <el-avatar
            class="flexShrink"
            :size="28"
            :icon="UserFilled"
            :src="require('../assets/bing_small.png')"
            style="background-color: #ffeed3"
          />
          <div v-if="item.assistant" style="flex: 1">
            <v-md-editor
              :model-value="item.assistant"
              mode="preview"
              @copy-code-success="handleCopyCodeSuccess"
            />
            <div v-if="!item.isError" class="operation">
              <div @click="copyAnswer(item.assistant)" class="operationItem">
                <el-icon size="14">
                  <CopyDocument />
                </el-icon>
                <div class="operationExplain">复制结果</div>
              </div>
              <div
                @click="onCollection(item, index)"
                class="operationItem"
                v-show="!item.isCollection"
                :class="item.isCollection ? 'operationItemSelected' : ''"
              >
                <el-icon size="14">
                  <Star />
                </el-icon>
                <div class="operationExplain">
                  {{ item.isCollection ? "取消收藏" : "收藏" }}
                </div>
              </div>
            </div>
          </div>
          <div v-else class="typing"></div>
        </div>
      </div>
    </div>
    <div class="footer">
      <div class="footer-bar">
        <el-input
          @keyup.enter="onSubmit"
          v-model="input"
          :placeholder="aiPrompt"
          :disabled="aiLoading"
        ></el-input>
        <div style="display: flex; padding-right: 10px" v-show="aiLoading">
          <div class="dot0"></div>
          <div class="dot1"></div>
          <div class="dot2"></div>
          <div class="dot3"></div>
          <div class="dot4"></div>
        </div>
        <div @click="onSubmit" class="sendIcon" v-show="!aiLoading">
          <el-icon :size="20">
            <Promotion />
          </el-icon>
        </div>
      </div>
    </div>
  </div>
  <LoginDialog :show="loginVisible" @close="loginVisible = false" />
</template>

<script>
import { onMounted, ref } from "vue";
import {
  CopyDocument,
  Goods,
  Promotion,
  Star,
  UserFilled,
} from "@element-plus/icons-vue";
import { ElNotification } from "element-plus";
import { FavoritesAdd, FavoritesDel, GetUserInfo } from "../../api/BSideApi";
import { useStore } from "vuex";
import LoginDialog from "@/components/LoginDialog.vue";

export default {
  name: "IndexView",
  components: { Star, CopyDocument, Goods, Promotion, LoginDialog },
  computed: {
    UserFilled() {
      return UserFilled;
    },
  },
  setup() {
    let store = useStore();
    let scrollRef = ref(null);
    let input = ref("");
    let conversationList = ref([]);
    let loginVisible = ref(false);
    let socket = ref(null);

    let aiLoading = ref(false);
    let aiPrompt = ref("有问题尽管问我....");

    onMounted(() => {
      if (store.getters.userinfo) getUser();
    });

    // TODO 提交问题
    async function onSubmit() {
      if (!store.getters.userinfo) return (loginVisible.value = true);
      if (input.value === "") return;
      aiLoading.value = true;
      aiPrompt.value = "思考中....";
      let index = conversationList.value.length;
      try {
        let content = input.value;
        input.value = "";
        conversationList.value.push({
          user: content,
        });

        // TODO 滚动到底部
        scrollToTheBottom();

        // TODO 上下文
        let messages = [];
        conversationList.value
          .slice(-4)
          .forEach(({ isError, user, assistant }) => {
            if (!isError) {
              messages.push({
                role: "user",
                content: user,
              });
              if (assistant)
                messages.push({
                  role: "assistant",
                  content: assistant,
                });
            }
          });

        webSocket({
          messages: {
            messages: messages,
          },
          index: index,
        });
      } catch (err) {
        conversationList.value[index].assistant = err;
        conversationList.value[index].isError = true;
        aiLoading.value = false;
      }
    }

    // eslint-disable-next-line no-unused-vars
    function webSocket({ messages, index }) {
      if (typeof WebSocket == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        let socketUrl =
          "ws://gptjava.hcolor.pro/chat/api/BING/" +
          localStorage.getItem("token");
        if (socket.value != null) {
          socket.value.close();
          socket.value = null;
        }
        socket.value = new WebSocket(socketUrl);
        // TODO 建立连接
        socket.value.onopen = function () {
          socket.value.send(JSON.stringify(messages));
        };
        conversationList.value[index].assistant = "";
        // TODO 接收消息
        socket.value.onmessage = function (msg) {
          try {
            if (msg.data === "SEND_ERR") {
              socket.value.close();
              conversationList.value[index].assistant =
                "哦豁!网络貌似出了点小问题";
              conversationList.value[index].isError = true;
              // eslint-disable-next-line no-empty
            } else if (msg.data === "NO_FREQUENCY") {
              socket.value.close();
              conversationList.value[index].assistant =
                "当前可用次数不足以使用该功能,请前往个人中心赞助";
              conversationList.value[index].isError = true;
            } else if (msg.data === "OVERTIME") {
              socket.value.close();
              conversationList.value[index].assistant =
                "哦豁!回复时间过长已自动截停";
              conversationList.value[index].isError = true;
            } else {
              conversationList.value[index].assistant = msg.data;
            }
            scrollToTheBottom();
          } catch (e) {
            socket.value.close();
            getUser();
          }
        };
        // TODO 关闭连接
        socket.value.onclose = function () {
          getUser();
          aiPrompt.value = "有问题尽管问我....";
          aiLoading.value = false;
        };
        // TODO 处理错误
        socket.value.onerror = function () {
          getUser();
          conversationList.value[index].assistant =
            "与服务器建立连接失败，请联系在微信小程序中咨询客服解决问题";
          conversationList.value[index].isError = true;
          aiPrompt.value = "有问题尽管问我....";
          aiLoading.value = false;
        };
      }
    }

    async function getUser() {
      try {
        let res = await GetUserInfo();
        store.commit("setUserinfo", res);
      } catch (e) {
        ElNotification({
          message: "获取用户数据失败",
          type: "error",
        });
      }
    }

    // TODO 滚动到底部
    function scrollToTheBottom() {
      setTimeout(
        () => (scrollRef.value.scrollTop = scrollRef.value.scrollHeight),
        100
      );
    }

    // TODO 复制代码块
    function handleCopyCodeSuccess(code) {
      if (aiLoading.value === false) {
        navigator.clipboard.writeText(code);
        ElNotification({
          message: "复制成功",
          type: "success",
        });
      } else {
        ElNotification({
          message: "请等待回复完成后复制",
          type: "success",
        });
      }
    }

    // TODO 复制答案
    function copyAnswer(data) {
      if (aiLoading.value === false) {
        navigator.clipboard.writeText(data);
        ElNotification({
          message: "复制成功",
          type: "success",
        });
      } else {
        ElNotification({
          message: "请等待回复完成后复制",
          type: "success",
        });
      }
    }

    async function onCollection(item, index) {
      if (aiLoading.value === false) {
        try {
          let bol = !conversationList.value[index].isCollection;
          if (bol) {
            conversationList.value[index].id = await FavoritesAdd({
              userDialogue: item.user,
              aiDialogue: item.assistant,
            });
            ElNotification({
              message: "收藏成功",
              type: "success",
            });
          } else {
            await FavoritesDel({
              id: item.id,
            });
          }
          conversationList.value[index].isCollection = bol;
        } catch (e) {
          ElNotification({
            message: e,
            type: "error",
          });
        }
      } else {
        ElNotification({
          message: "请等待回复完成后收藏",
          type: "success",
        });
      }
    }
    return {
      aiLoading,
      aiPrompt,
      onSubmit,
      input,
      conversationList,
      scrollRef,
      handleCopyCodeSuccess,
      loginVisible,
      onCollection,
      copyAnswer,
    };
  },
};
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
    background-color: #f9f9f9;
  }
  40% {
    transform: scale(1);
    background-color: #eabf95ff;
  }
}

.dot0,
.dot1,
.dot2,
.dot3 {
  background: rgb(254, 164, 78);
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

.footer {
  width: 100%;
  box-sizing: border-box;
  z-index: 1;
  pointer-events: none;
  background: linear-gradient(rgba(246, 246, 246, 0), #f6f6f6 25%);
  flex-shrink: 0;
  padding: 30px 20px;
  display: flex;
  position: absolute;
  bottom: 0;
  overflow: hidden;
  flex-direction: column;
  align-items: center;
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

@keyframes footerBarAnimation {
  from {
    transform: translateY(150%);
  }

  to {
    transform: translateY(0);
  }
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

.sendIcon {
  flex-shrink: 0;
  margin: 0 12px;
  width: 36px;
  height: 36px;
  color: #fff;
  cursor: pointer;
  background: #fea44e;
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
  /*border: 1px solid #ececec;*/
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

.question,
.answer {
  padding: 20px 8px;
  display: flex;
}

.answer {
  border-top: 1px solid #f4f6f8;
  position: relative;
}

.question > .text,
.answer > .text {
  min-height: 28px;
  box-sizing: border-box;
  white-space: pre-wrap;
  flex: 1;
  margin-left: 16px;
  font-size: 16px;
  line-height: 28px;
  position: relative;
}

>>> .vuepress-markdown-body {
  margin-left: 16px;
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
  animation: beating 0.7s infinite alternate;
  width: 100px;
  margin-bottom: 20px;
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

@keyframes beating {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-10px);
  }
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

>>> .answer > .el-avatar,
>>> .question > .el-avatar {
  background-color: white;
}

.operation {
  display: flex;
  margin-top: 20px;
}

.operationItem {
  justify-content: center;
  height: 30px;
  cursor: pointer;
  box-sizing: border-box;
  padding: 0 15px;
  margin-left: 15px;
  margin-right: 5px;
  display: flex;
  align-items: center;
  background-color: #7d80ff;
  color: white;
  border-radius: 100px;
  font-size: 13px;
}

.operationItemSelected {
  background-color: #7d80ff;
  color: white;
}

.operationExplain {
  font-size: 8px;
  margin-left: 5px;
}
</style>
