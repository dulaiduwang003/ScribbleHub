<template>
  <view class="personal-container">
    <!--个性卡片-->
    <view class="wx-top">
      <view>
        <view class="wx-name">
          <view class="name">{{ userInfo.userName ? userInfo.userName : env.user }}</view>
          <view class="wx-tabs">
            <text class="wx-user-label">
              {{ userInfo.type === "ADMIN" ? env.adminLabel : env.userLabel }}
            </text>
          </view>
        </view>
        <view class="wx-image">
          <view class="wx-user-avatar">
            <image :src="userInfo.avatar?env.baseUrl+userInfo.avatar:'/static/images/individual/defaultAvatar.jpg'"/>
          </view>
        </view>
        <image src="/static/images/individual/twilight.jpeg"
               class="wx-b-image"/>
      </view>
    </view>
    <!--中间操作卡片-->
    <view class="wx-bottom-box">
      <view class="wx-bottom-inner" @click="toSuper">
        <view class="wx-img-b">
          <image src="/static/images/individual/satellite.png"/>
        </view>
        <view class="wx-auth">
          <view class="wx-bottom-font">SUPERAI</view>
          <view class="wx-bottom-color">SUPERUSER</view>
        </view>
      </view>
      <view class="wx-bottom-inner" open-type="contact">
        <view class="wx-bottom-icon">
          <image src="/static/images/individual/article.png"/>
        </view>
        <view class="wx-auth">
          <view class="wx-bottom-font">关于作者</view>
          <view class="wx-bottom-color">联系作者探讨学术</view>
        </view>
      </view>
      <button open-type="contact" class="customer">
      </button>
    </view>
    <view class="wx-middle-box">
      <view class="wx-middle-button" @click="openUploadPanel">
        编辑资料
      </view>
      <view class="wx-middle-button" @click="toTaskOrchestration">
        我的编排
      </view>
    </view>
    <!--    底部TAB-->
    <van-tabs :active="active" animated @click="onChangeTabs">
      <van-tab title="创作">
        <scroll-view scroll-y class="tabs_scroll" :scroll-with-animation="true" lower-threshold="100"
                     @scrolltolower="onScrollToLower()">
          <view class="tabs_scroll_model">
            <!--  图片流-->
            <custom-waterfalls-flow ref="creationFlowRef" v-if="data.list.length>0" :value="data.list"
                                    @imageClick="toDrawingDetail">
              <view class="item" v-for="(parentItem,parentIndex) in data.list" :key="parentIndex"
                    slot="slot{{parentIndex}}">
                <view class="flow_slot_model">
                  <view v-for="(childItem,childIndex) in parentItem.value.prompt" :key="childIndex"
                        slot="slot{{childIndex}}" class="flow_slot_block">
                    {{ childItem }}
                  </view>
                </view>
              </view>
            </custom-waterfalls-flow>
            <empty-component msg="暂未找到任何作品" height="40" v-if="!isFlushed&&data.list.length===0"/>
            <loading-data-component v-if="isFlushed"/>
          </view>
        </scroll-view>
      </van-tab>
      <van-tab title="鉴赏">
        <scroll-view scroll-y class="tabs_scroll" :scroll-with-animation="true" lower-threshold="100"
                     @scrolltolower="onScrollToLowerPublic()">
          <view class="tabs_scroll_model">
            <!--  图片流-->
            <custom-waterfalls-flow ref="appreciationFlowRef" v-if="publicData.list.length>0" :value="publicData.list"
                                    @imageClick="toDrawingDetail">
              <view class="item" v-for="(parentItem,parentIndex) in publicData.list" :key="parentIndex"
                    slot="slot{{parentIndex}}">
                <view class="flow_slot_model">
                  <view v-for="(childItem,childIndex) in parentItem.value.prompt" :key="childIndex"
                        slot="slot{{childIndex}}" class="flow_slot_block">
                    {{ childItem }}
                  </view>
                </view>
              </view>
            </custom-waterfalls-flow>
            <empty-component msg="暂未任何公开作品" height="40" v-if="!isFlushed&&publicData.list.length===0"/>
            <loading-data-component v-if="isFlushed"/>
          </view>
        </scroll-view>
      </van-tab>
    </van-tabs>
    <!--修改个人信息弹出层-->
    <uni-popup type="center" ref="personalInfoPopup">
      <view class="upload-container">
        <view class="upload-head">
          <view>
            个性化
          </view>
          <view>
            <van-icon name="cross" @click="this.$refs.personalInfoPopup.close()"/>
          </view>
        </view>
        <view class="user-upload">
          <button class="avatar-wrapper" open-type="chooseAvatar" @chooseavatar="uploadAvatar"
                  :plain="true">
            <view class="user-upload-avatar">
              <image
                  :src="userInfoForm.avatar?env.baseUrl+userInfoForm.avatar :'/static/images/individual/defaultAvatar.jpg'"/>
            </view>
          </button>
          <view class="user-upload-form">
            <view>
              <input class="user-upload-name" type="nickname" placeholder="请输入昵称" maxlength="10"
                     v-model="userInfoForm.userName"
                     @blur="onNickName($event)" confirm-type="success"/>
            </view>
            <view>
              <input class="user-upload-id" disabled readonly v-model="userInfoForm.openId"/>
            </view>
          </view>
        </view>
        <view class="upload_an_avatar">点击上传头像</view>
        <view class="upload-btn">
          <view class="btn__right">
            <button @click="uploadName">修改昵称</button>
            <button @click="copyOpenId">复制ID</button>
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import {getCurrentUserInfo, uploadName} from "@/api/user";
import env from "@/utils/env";
import {getToken, setUser} from "@/utils/utils";
import {currentUserOpus, getsPublicOpus} from "@/api/function";
import LoadingDataComponent from "@/wxcomponents/components/LoadingDataComponent.vue";
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";

export default {
  components: {EmptyComponent, LoadingDataComponent},
  computed: {
    env() {
      return env
    }
  },
  data() {
    return {
      isFlushed: false,
      //用户信息
      userInfo: {},
      //用户信息表单
      userInfoForm: {
        userName: '',
      },
      //我的创作
      data: {
        logged: true,
        list: []
      },
      //公开作品
      publicData: {
        logged: false,
        list: []
      },
      active: 0
    };
  },
  created() {
    let isLogin = this.$parent.handleIsLogin();
    if (isLogin) {
      this.getUserInfo();
      this.getCurrentUserOpus();
    }
  },
  methods: {
    /**
     * 用户切换TABS
     * @param e
     */
    onChangeTabs: function (e) {
      if (e.detail.index === 0) {
        this.getCurrentUserOpus()
      } else {
        this.getPublicOpus()
      }

    },
    /**
     *  触底刷新数据
     */
    onScrollToLower: function () {
      this.getCurrentUserOpus()
    },
    /**
     *  触底刷新数据
     */
    onScrollToLowerPublic: function () {
      this.getPublicOpus()
    },
    /**
     * 绘图详情
     * @param e
     */
    toDrawingDetail: function (e) {
      uni.navigateTo({
        url: '/pages/super/view/drawingDetailedView?seaImageId=' + e.value.seaImageId
      })
    },
    /**
     * 获取公开作品
     * @returns {Promise<void>}
     */
    getPublicOpus: async function () {
      try {
        this.isFlushed = true
        let newVar = await getsPublicOpus();
        if (newVar) {
          newVar.forEach(n => {
            // 拼接路由
            n.image = env.baseUrl + n.image;
            const temp = (n.value.prompt).toString();
            // 切割提示词
            const arr2 = temp.split(/[,，]/)
            if (Array.isArray(arr2)) {
              n.value.prompt = arr2.slice(0, 4);
            }
          });

          this.publicData.list = newVar
          // let appreciationFlowRef = this.$refs.appreciationFlowRef;
          // if (appreciationFlowRef) {
          //   appreciationFlowRef.refresh()
          // }
          const _this = this
          setTimeout(() => {
            _this.isFlushed = false
          }, 1000)
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: e
        });
      }
    },
    /**
     * 获取登录用户作品
     * @returns {Promise<void>}
     */
    getCurrentUserOpus: async function () {
      try {
        this.isFlushed = true
        let newVar = await currentUserOpus();
        if (newVar) {
          newVar.forEach(n => {
            // 拼接路由
            n.image = env.baseUrl + n.image;
            const temp = (n.value.prompt).toString();
            // 切割提示词
            const arr2 = temp.split(/[,，]/);

            if (Array.isArray(arr2)) {
              n.value.prompt = arr2.slice(0, 4);
            }
          });
          this.data.list = newVar
          // let creationFlowRef = this.$refs.creationFlowRef;
          // if (creationFlowRef) {
          //   creationFlowRef.refresh()
          // }
          const _this = this
          setTimeout(() => {
            _this.isFlushed = false
          }, 1000)
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: e
        });
      }
    },
    /**
     * 获取登录用户信息
     * @returns {Promise<void>}
     */
    getUserInfo: async function () {
      try {
        let currentUserInfo = await getCurrentUserInfo();

        if (currentUserInfo) {
          this.userInfo = currentUserInfo
          setUser(this.userInfo)
          //深拷贝
          this.userInfoForm = JSON.parse(JSON.stringify(this.userInfo));
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: '获取用户数据失败'
        });
      }
    },
    /**
     * 跳转任务编排
     */
    toTaskOrchestration: function () {
      uni.navigateTo({
        url: '/pages/choreography/choreography'
      })

    },
    /**
     * 跳转SUPERBOT
     */
    toSuper: function () {
      uni.navigateTo({
        url: '/pages/super/super'
      })

    },
    /**
     * 打卡个性化面板
     */
    openUploadPanel: function () {
      this.$refs.personalInfoPopup.open('center')
    },
    /**
     * 获取微信用户名
     * @param e
     */
    onNickName: function (e) {
      this.userInfoForm.userName = e.detail.value
    },
    /**
     * 修改头像
     * @returns {Promise<void>}
     */
    uploadAvatar: async function (e) {
      const baseUrl = env.baseUrl
      const _this = this
      wx.uploadFile({
        url: baseUrl + '/function/user/upload/avatar',
        filePath: e.detail.avatarUrl,
        name: 'avatar',
        header: {
          'token': getToken()
        },
        success() {
          _this.getUserInfo()
          uni.showToast({
            title: '上传头像成功',
            icon: 'none',
            duration: 2000
          })
        },
        fail(res) {
          console.log(res)
          uni.hideLoading()
          uni.showToast({
            title: '上传头像失败,请稍后重试',
            icon: 'none',
            duration: 2000
          })
        }
      })

    },
    /**
     * 修改昵称
     * @returns {Promise<void>}
     */
    uploadName: async function () {
      const {userName} = this.userInfoForm
      if (!(userName.length > 0)) {
        uni.showToast({
          title: "昵称不能为空",
          icon: 'none',
          duration: 2000
        })
        return
      }
      if (!(userName === this.userInfo.userName)) {
        try {
          uni.showLoading({
            title: '正在修改 ing~',
            mask: true
          });
          await uploadName({
            userName: this.userInfoForm.userName
          });
          await this.getUserInfo();
          uni.hideLoading()
          uni.showToast({
            title: "修改昵称成功",
            icon: 'none',
            duration: 2000
          })
        } catch (e) {
          uni.showToast({
            title: '昵称不合法!',
            icon: 'none',
            duration: 2000
          })
        }
      }

    },
    /**
     * 复制OpenID
     * @returns {Promise<void>}
     */
    copyOpenId: async function () {
      uni.setClipboardData({
        data: this.userInfo.openId, // data的值为你要复制的内容
        success: res => {
          uni.showToast({
            title: "已复制粘贴板",
            icon: 'none',
            duration: 2000
          })
        }
      });
    }
  }
}
</script>

<style lang="scss">
page {
  --tabs-default-color: rgb(255, 255, 255);
  --tabs-nav-background-color: rgb(0, 0, 0);
  --tabs-bottom-bar-color: rgb(238, 179, 118);
}

input {
  width: 100rpx;
}

.upload-btn button {
  color: white;
  background-color: #4d347d;
  margin-left: 10rpx;
  font-size: 25rpx;

}

.wx-top {
  width: 100%;
  overflow: hidden;
  box-shadow: 0 -50px 30px -10px #000000 inset;
}

.wx-bottom-box {
  padding: 0 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.wx-bottom-inner {
  display: flex;
  justify-content: center;
  width: 300rpx;

  color: white;

}

.customer {
  top: 450rpx;
  right: 0;
  width: 300rpx;
  height: 90rpx;
  background-color: unset;
  color: white;
  z-index: 3;
  position: absolute;
}

.user-upload {
  margin-top: 40rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
}

.user-upload-avatar {
  width: 130rpx;
  height: 130rpx;
  overflow-x: hidden;
  border-radius: 100%;

}

.user-upload-avatar image {
  width: 100%;
  height: 100%
}

.user-upload-form {
  padding-left: 40rpx
}

.tabs_scroll {
  height: 48vh;
  margin-top: 20rpx
}

.tabs_scroll_model {
  padding: 0 40rpx 10vh;
}

.flow_slot_model {
  display: flex;
  flex-wrap: wrap;
  font-size: 20rpx;
  margin-bottom: 20rpx
}

.flow_slot_block {
  margin-top: 20rpx;
  color: white;
  background-color: rgb(72, 72, 72);
  padding: 5rpx;
  padding-left: 10rpx;
  padding-right: 10rpx;
  border-radius: 15rpx;
  margin-left: 5rpx;
  margin-right: 5rpx;
}

.user-upload-name {
  padding-bottom: 5rpx;
  margin-top: 10rpx;
  width: 360rpx;
  font-weight: 550;
  font-size: 28rpx;
  color: #868686;
  border-bottom: 1rpx solid #4d4d4d;
  padding-bottom: 10rpx;
}

.user-upload-id {
  padding-bottom: 10rpx;
  margin-top: 20rpx;
  width: 360rpx;
  font-weight: 550;
  font-size: 25rpx;
  color: #868686;
  border-bottom: 1rpx solid #282828
}

.wx-img-b {
  width: 70rpx;
  background-color: rgb(21, 21, 20);
  height: 74rpx;
  padding: 2%;
  border-radius: 20rpx;
}

.wx-auth {
  padding-top: 10rpx;

}

.wx-bottom-font {
  padding-bottom: 2px;
  padding-left: 20rpx;
  font-size: 27rpx;
}

.wx-bottom-color {
  padding-bottom: 2rpx;
  padding-left: 20rpx;
  font-size: 22rpx;
  color: rgb(134, 134, 134);
}

.wx-middle-box {
  padding: 5%;
  display: flex;
}

.wx-middle-button {
  margin-left: 2%;
  width: 370rpx;
  height: 65rpx;
  background-color: rgb(20, 20, 20);
  text-align: center;
  border-radius: 5rpx;
  color: rgb(134, 134, 134);
  font-size: 25rpx;
  padding-top: 25rpx;
}

.wx-bottom-icon {
  width: 70rpx;
  background-color: rgb(21, 21, 20);
  height: 74rpx;
  padding: 2%;
  border-radius: 20rpx;
}

.wx-name {
  z-index: 32;
  position: absolute;
  font-size: 43rpx;
  color: white;
  margin-top: 33%;
  margin-left: 28%;
  font-weight: 500;
}

.wx-tabs {
  font-size: 20rpx;
  margin-top: 0.5vh;
  color: white;
  font-weight: 400;
  border-radius: 15rpx;
}

.wx-image {
  z-index: 32;
  position: absolute;
  margin-top: 30%;
  margin-left: 5%;
}

.wx-b-image {
  position: relative;
  z-index: -1;
  width: 750rpx;
  height: 450rpx;
}

.personal-content {
  margin-top: 450rpx;
  margin-left: 1%;
  width: 98%;
}

.span-image {
  margin-left: 140px;
  margin-bottom: 60rpx;
  border-radius: 100%;
  overflow: hidden;
  width: 200rpx;
  height: 200rpx;
}

.upload-btn {
  padding-top: 20rpx;
  width: 100%;
  display: flex;
  justify-content: flex-end;
}

.upload_an_avatar {
  padding-left: 40rpx;
  padding-top: 30rpx;
  color: #444444;
  font-size: 25rpx
}

.name {
  padding-bottom: 15rpx;
}

.wx-user-label {
  background-color: rgb(255, 110, 180);
  padding: 8rpx;
  border-radius: 10rpx;
  font-size: 20rpx;
  font-weight: 600
}

.upload-container {
  width: 600rpx;

  background-color: #131313;
  border-radius: 20rpx;
  padding: 20rpx
}

.btn__right {
  display: flex;
  padding-right: 20rpx
}

.upload-head {
  font-weight: 550;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #707070;
  font-size: 26rpx
}

.wx-user-avatar {
  border-radius: 100%;
  overflow: hidden;
  width: 150rpx;
  height: 150rpx;
}

.wx-user-avatar image {
  width: 100%;
  height: 100%
}

.wx-img-b image {
  width: 73rpx;
  height: 73rpx;
}

.personal-container {

  animation: fadeIn 0.5s ease-in-out forwards;
}


.wx-bottom-icon image {
  width: 73rpx;
  height: 73rpx;
}

.span-text {
  color: rgb(168, 167, 163);
  margin-bottom: 3%;
  margin-left: 17%;
  font-size: 30rpx;
}

button[plain] {
  border: 0
}
</style>
