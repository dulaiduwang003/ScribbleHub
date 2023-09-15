<template>
  <view class="comments-container">
    <!--评论-->
    <view v-if="replyData.length>0">
      <view class="comment-model" v-for="(item,index) in replyData" :key="index"
            @longpress="onLongPress(item.seaReplyId, item.isDeleted, index)">
        <view class="head-model">
          <view class="avatar-location">
            <view class="avatar">
              <image :src="item.avatar?env.baseUrl+item.avatar:'/static/images/individual/defaultAvatar.jpg'"/>
            </view>
            <view class="info-model">
              <view class="info-name">{{ item.userName ? item.userName : env.user }}</view>
              <view class="info-label">{{
                  conversionTime(item.createdTime) ? conversionTime(item.createdTime) : '刚刚'
                }} {{
                  item.replyName ? '@回复 ' + item.replyName : ''
                }}
              </view>
            </view>
          </view>
          <view>
            <van-icon name="chat-o" color="#929292;" size="40rpx"
                      @click="replyCommentOpen(item.seaReplyId,item.userName)"/>
          </view>
        </view>
        <view class="comment-content">
          {{ item.replyContent }}
        </view>
      </view>
    </view>
    <empty-component msg="这里空空如也" height="60" v-else/>
    <!--回复评论-->
    <uni-popup ref="publicationReplyRef">
      <view class="publication-container">
        <view class="textarea-model">
          <view class="publication-title" @click="submitPublicationComment">
            回复
          </view>
          <textarea placeholder="发表我的见解..." v-model="replyInput" maxlength="100"
                    confirm-type="send" @confirm="submitPublicationComment"/>
        </view>
      </view>
    </uni-popup>
    <!--回复（回复）-->
    <uni-popup ref="replyCommentRef">
      <view class="publication-container">
        <view class="textarea-model">
          <view class="publication-title" @click="submitReplyComment">
            回复:{{ reciprocityName }}
          </view>
          <textarea placeholder="发表我的见解..." v-model="reciprocityInput" maxlength="100"
                    confirm-type="send" @confirm="submitReplyComment"/>
        </view>
      </view>
    </uni-popup>
    <!--悬浮-->
    <view class="floating">
      <view class="floating-input" @click="publicationReplyOpen">
        <van-icon name="edit" size="50rpx" color="rgb(110,110,110)"/>
        <view class="floating-text">回复: {{ replyUserNameMsg ? replyUserNameMsg : env.user }}...</view>
      </view>
    </view>
  </view>

</template>

<script>


import {blogReply} from "@/api/public";
import {deletedReply, publicationReply} from "@/api/function";
import env from "@/utils/env";
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";
import {getToken} from "@/utils/utils";
import {conversionTime} from "@/utils/date";

export default {
  components: {EmptyComponent},
  computed: {
    env() {
      return env
    }
  },
  onLoad(option) {
    this.seaCommentId = option.seaCommentId
    this.replyUserNameMsg = option.userName
    this.handleReply();
    if (this.replyUserNameMsg) {
      uni.setNavigationBarTitle({title: '回复:' + option.userName});
    }
  },
  data() {
    return {
      //回复评论内容
      replyInput: '',
      //评论ID
      seaCommentId: undefined,
      //回复数据表
      replyData: [],
      //回复评论
      replyUserNameMsg: '',
      //回复ID
      reciprocityId: undefined,
      reciprocityInput: '',
      reciprocityName: ''

    };
  }, methods: {
    conversionTime,
    /**
     * 获取回复评论数据
     * @returns {Promise<void>}
     */
    handleReply: async function () {
      try {
        let promise = await blogReply(this.seaCommentId);
        if (promise) {
          promise.forEach(l => {
            if (l.reciprocityId) {
              let find = promise.find(item => item.seaReplyId = l.reciprocityId);
              l.replyName = find.userName
              if (!l.replyName) {
                l.replyName = env.user
              }
            }
          })
          this.replyData = promise

        }
      } catch (e) {
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 4000
        });
      }
    },
    /**
     * 提交 回复评论
     * @returns {Promise<void>}
     */
    submitReplyComment: async function () {
      if (!this.reciprocityInput.trim()) {
        uni.showToast({
          title: '回复内容不能为空',
          icon: 'none',
          duration: 2000
        })
        return
      }
      try {
        uni.showLoading({
          title: '正在回复 ing~',
          mask: true
        });
        await publicationReply({
          replyContent: this.reciprocityInput,
          seaCommentId: this.seaCommentId,
          reciprocityId: this.reciprocityId
        });
        uni.hideLoading()
        await this.handleReply();
        uni.$emit('blogGetBlogComment')
        uni.showToast({
          title: '回复成功',
          icon: 'none',
          duration: 2000
        })
        this.$refs.replyCommentRef.close();
        this.reciprocityInput = ''

      } catch (e) {
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 4000
        });
      }
    },
    /**
     * 提交 回复评论
     * @returns {Promise<void>}
     */
    submitPublicationComment: async function () {
      if (!this.replyInput.trim()) {
        uni.showToast({
          title: '回复内容不能为空',
          icon: 'none',
          duration: 2000
        })
        return
      }
      try {
        uni.showLoading({
          title: '正在回复 ing~',
          mask: true
        });
        await publicationReply({
          replyContent: this.replyInput,
          seaCommentId: this.seaCommentId,
        });
        uni.hideLoading()
        await this.handleReply();
        uni.$emit('blogGetBlogComment')
        uni.showToast({
          title: '发表成功',
          icon: 'none',
          duration: 2000
        })
        this.$refs.publicationReplyRef.close();
        this.replyInput = ''

      } catch (e) {
        console.log(e)
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 4000
        });
      }
    },

    /**
     * u回复
     */
    publicationReplyOpen: function () {
      if (!getToken()) {
        uni.reLaunch({
          url: '/pages/master/master?currentPage=1'
        })
        return
      }
      this.$refs.replyCommentRef.close();
      this.$refs.publicationReplyRef.open('bottom')
    },
    /**
     * u回复
     */
    replyCommentOpen: function (id, userName) {
      if (!getToken()) {
        uni.reLaunch({
          url: '/pages/master/master?currentPage=1'
        })
        return
      }
      this.reciprocityId = id
      this.reciprocityName = userName;
      this.$refs.publicationReplyRef.close()
      this.$refs.replyCommentRef.open('bottom')
    },
    /**
     * 长按事件
     * @param id
     * @param permissions
     * @param index
     */
    onLongPress(id, permissions, index) {
      const _this = this
      if (permissions) {
        uni.showModal({
          title: '提示',
          content: '确定删除这条评论？',
          success: async function (res) {
            if (res.confirm) {
              uni.showLoading({
                title: '正在删除 ing~',
                mask: true
              });
              try {
                await deletedReply({
                  seaReplyId: id
                })
                await _this.handleReply()
                uni.$emit('blogGetBlogComment')
                uni.hideLoading();
                uni.showToast({
                  title: '删除成功',
                  icon: 'none',
                  duration: 2000
                })

              } catch (e) {
                console.log(e)
                uni.showToast({
                  title: '删除评论失败~',
                  icon: 'none',
                  duration: 4000
                });
              }
            }
          }
        });
      }

    },

  }
}
</script>

<style lang="scss">
.comments-container {
  color: white;
  margin-top: 50rpx
}

.comments-title {
  font-size: 40rpx;
  font-weight: 550;
  margin-bottom: 30rpx;
}

.floating {
  padding: 15rpx 40rpx;
  position: fixed;
  bottom: 0;
  left: 0;
  height: 140rpx;
  background-color: rgb(30, 30, 30);
  width: 748rpx;
  display: flex;
  z-index: 99;
}

.floating-input {
  padding: 0 20rpx;
  height: 80rpx;
  width: 640rpx;
  border-radius: 15rpx;
  background-color: rgb(17, 17, 17);
  display: flex;
  align-items: center
}

.floating-text {
  padding-left: 15rpx;
  font-size: 26rpx;
  color: rgb(110, 110, 110)
}

.publication-container {
  position: fixed;
  bottom: 0;
  border-top-left-radius: 60rpx;
  border-top-right-radius: 60rpx;
  background-color: rgb(30, 30, 30);
  height: 70vh;
  width: 750rpx;
  color: white;
}

.comment-model {
  background-color: #1e1e1e;
  border-radius: 8rpx;
  padding: 30rpx;

}

.head-model {
  display: flex;
  align-items: flex-start;
  justify-content: space-between
}

.avatar-location {
  display: flex;
  align-items: center;
}

textarea {
  margin-top: 30rpx;
  width: 100%;
  height: 500rpx;
  word-break: break-all;
}

.publication-title {
  display: flex;
  justify-content: center;;
  align-items: center;
  font-size: 30rpx
}

.textarea-model {
  padding: 40rpx
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  overflow-x: hidden;
  border-radius: 100%
}

.avatar image {
  width: 100%;
  height: 100%
}

.info-model {
  padding-left: 20rpx
}

.info-name {
  color: rgb(69, 113, 148);
  font-size: 30rpx
}

.comment-content {
  margin-top: 20rpx;
  padding-left: 100rpx;
  font-size: 30rpx
}

.info-label {
  font-size: 23rpx;
  color: #929292;
  padding-top: 5rpx
}
</style>
