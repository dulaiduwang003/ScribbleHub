<template>
  <view class="nerve-container">
    <loading-component ref="loading"/>
    <view class="nerve-title">
      NERVE参数配置
    </view>
    <scroll-view scroll-y style="height: 75vh;background-color: red">
      <van-field
          placeholder="请设置密钥"
          border="true"
          label="BITO_ID"
          :value="form.bitoUserId"
          :error-message="formErrMsg.bitoUserId"
          type="number"
          @change="onChange($event,'bitoUserId')"
      />
      <van-field
          maxlength="20"
          placeholder="请设置作者"
          border="true"
          label="作者昵称"
          :value="form.authorName"
          :error-message="formErrMsg.authorName"
          @change="onChange($event,'authorName')"
      />
      <van-field

          placeholder="请设置邮箱"
          border="true"
          label="个人邮箱"
          :value="form.email"
          :error-message="formErrMsg.email"
          @change="onChange($event,'email')"
      />
      <van-field
          placeholder="请设置环境"
          border="true"
          label="IDE环境"
          :value="form.ideName"
          :error-message="formErrMsg.ideName"
          @change="onChange($event,'ideName')"
      />
      <van-field
          maxlength="10"
          placeholder="请设置BOT昵称"
          border="true"
          label="BOT昵称"
          :value="form.botName"
          :error-message="formErrMsg.botName"
          @change="onChange($event,'botName')"
      />
      <van-field
          placeholder="请设置目标语言"
          border="true"
          label="回复语言"
          :value="form.outputLanguage"
          :error-message="formErrMsg.outputLanguage"
          @change="onChange($event,'outputLanguage')"
      />
      <van-field
          placeholder="请设置WS_ID环境"
          border="true"
          label="WS_ID环境"
          type="number"
          :value="form.wsId"
          :error-message="formErrMsg.wsId"
          @change="onChange($event,'wsId')"
      />
      <van-field
          placeholder="请设置Session_Id环境"
          border="true"
          label="SESSION_Id环境"
          :value="form.sessionId"
          :error-message="formErrMsg.sessionId"
          @change="onChange($event,'sessionId')"
      />
      <van-field
          placeholder="请设置Request_Id环境"
          border="true"
          label="REQUEST_ID环境"
          :value="form.requestId"
          :error-message="formErrMsg.requestId"
          @change="onChange($event,'requestId')"
      />
      <van-field
          placeholder="请设置用户标识ID"
          border="true"
          label="U_ID环境"
          :value="form.uId"
          :error-message="formErrMsg.uId"
          @change="onChange($event,'uId')"
      />
      <van-field
          placeholder="请设置authorization"
          border="true"
          label="AUTH"
          :value="form.authorization"
          :error-message="formErrMsg.authorization"
          @change="onChange($event,'authorization')"
      />
      <van-field
          placeholder="请设置stable diffusion API"
          border="true"
          label="SD_API"
          :value="form.sdUrl"
          :error-message="formErrMsg.sdUrl"
          @change="onChange($event,'sdUrl')"
      />
      <van-field
          placeholder="请设置代理IP"
          border="true"
          label="代理IP"
          :value="form.proxyIp"
          :error-message="formErrMsg.proxyIp"
          @change="onChange($event,'proxyIp')"
      />
      <van-field
          placeholder="请设置代理端口"
          border="true"
          label="代理端口"
          :value="form.proxyPort"
          :error-message="formErrMsg.proxyPort"
          @change="onChange($event,'proxyPort')"
      />
      <van-field
          placeholder="请设置BingCookie"
          border="true"
          label="BingCookie"
          :value="form.bingCookie"
          type="textarea"
          autosize
          :error-message="formErrMsg.bingCookie"
          @change="onChange($event,'bingCookie')"
      />
    </scroll-view>

    <view class="nerve-btn">
      <van-button round type="default" size="large" color="#7232dd" @click="submit">重载</van-button>
    </view>
  </view>
</template>

<script>
import LoadingComponent from "@/wxcomponents/components/LoadingComponent.vue";
import {botConfiguration, botConfigurationUpdate} from "@/api/admin";

export default {
  components: {LoadingComponent},
  onLoad() {
    let loading = this.$refs.loading;
    loading.handlePopupOpen()
    this.getBotConfiguration()
    setTimeout(() => {
      loading.handlePopupClose()
    }, 500)
  },
  methods: {
    /**
     * 绑定Value值
     */
    onChange: function (e, a) {
      this.form[a] = e.detail
      this.formErrMsg[a] = ''
    },
    /**
     * 获取BOT服务器配置
     * @returns {Promise<void>}
     */
    getBotConfiguration: async function () {
      try {
        let promise = await botConfiguration();
        if (promise) {
          this.form = promise.bitoModel
          this.form.sdUrl = promise.sdUrl
          this.form.authorName = promise.authorName
          this.form.botName = promise.botName
          this.form.proxyIp = promise.proxyIp
          this.form.bingCookie = promise.bingCookie
          this.form.proxyPort = promise.proxyPort
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          title: '获取服务器数据失败~',
          icon: 'none',
          duration: 4000
        })
      }
    },
    /**
     * 提交表单
     */
    submit: async function () {
      const form = this.form;
      const formErrMsg = this.formErrMsg;
      const requiredFields = ['bitoUserId', 'authorName', 'email', 'ideName', 'botName', 'outputLanguage', 'wsId', 'sessionId', 'requestId', 'uId', 'authorization', 'sdUrl',"proxyIp","proxyPort","bingCookie"];
      const fieldNames = {
        'bitoUserId': 'bitoUserId密钥',
        'authorName': '作者昵称',
        'email': '邮箱',
        'ideName': 'IDE环境',
        'botName': 'BOT昵称',
        'outputLanguage': '输出语言',
        'wsId': 'WS_ID密钥',
        'sessionId': 'sessionId密钥',
        'requestId': 'requestId密钥',
        'uId': 'uId密钥',
        'authorization': 'Bearer认证',
        'sdUrl': 'SD API',
        "proxyIp":'代理IP',
        "proxyPort":'代理端口',
        "bingCookie":'BingCookie'
      };
      for (let i = 0; i < requiredFields.length; i++) {
        const field = requiredFields[i];
        if (!form[field]) {
          formErrMsg[field] = `请填写${fieldNames[field]}`;
          return;
        }
      }

      let loading = this.$refs.loading;
      try {
        loading.handlePopupOpen();
        await botConfigurationUpdate(this.form);
        uni.showToast({
          title: '更新服务器数据成功~',
          icon: 'none',
          duration: 4000
        })
      } catch (e) {
        console.log(e)
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 4000
        })
      } finally {
        setTimeout(() => {
          loading.handlePopupClose();
        }, 500)
      }

    }
  },
  data() {
    return {
      form: {
        authorName: '',
        ideName: '',
        botName: '',
        outputLanguage: '',
        email: '',
        sdUrl: '',
        wsId: undefined,
        sessionId: '',
        requestId: '',
        bitoUserId: undefined,
        authorization: '',
        uId: '',
        proxyPort:undefined,
        proxyIp:'',
        bingCookie:''

      },
      formErrMsg: {
        authorName: '',
        authorization: '',
        ideName: '',
        botName: '',
        outputLanguage: '',
        email: '',
        sdUrl: '',
        wsId: undefined,
        sessionId: '',
        requestId: '',
        bitoUserId: undefined,
        uId: '',
        proxyPort:undefined,
        proxyIp:'',
        bingCookie:''
      }

    };
  }
}
</script>

<style lang="scss">
page {
  background-color: white;
}

.nerve-container {
  padding: 20rpx
}

.nerve-title {
  font-size: 50rpx;
  padding-bottom: 30rpx
}

.nerve-btn {
  padding: 0 20rpx;
  padding-top: 50rpx;
}
</style>
