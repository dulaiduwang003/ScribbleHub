<template>
  <view class="comments-container">
    <!--评论-->
    <view>
      <view class="comments-title">评论</view>
      <view v-if="commentData.length>0">
        <view class="comment-model" v-for="(item,index) in commentData" :key="index"
              @longpress="onLongPress(item.seaCommentId,item.isDeleted,index)">
          <view class="head-model">
            <view class="avatar-location">
              <view class="avatar">
                <image :src="item.avatar?env.baseUrl+item.avatar:'/static/images/individual/defaultAvatar.jpg'"/>
              </view>
              <view class="info-model">
                <view class="info-name">{{ item.userName ? item.userName : env.user }}</view>
                <view class="info-label">{{ conversionTime(item.createdTime) }}</view>
              </view>
            </view>
            <view>
              <van-icon name="chat-o" color="#929292;" size="40rpx" @click="toReply(item.seaCommentId,item.userName)"/>
            </view>
          </view>
          <view class="comment-content">
            {{ item.commentContent }}
          </view>
          <view class="reply_btn" v-if="item.isSmall"
                @click="toReply(item.seaCommentId,item.userName)">
            查看回复
          </view>
        </view>
      </view>
      <empty-component msg="评论区空空如也" height="40" v-else/>
    </view>
    <!--发表评论弹窗-->
    <uni-popup ref="publicationCommentRef">
      <view class="publication-container">
        <view class="textarea-model">
          <view class="publication-title" @click="submitPublication($event)">
            {{ isLogin ? '发表我的想法' : '请先登录' }}
          </view>
          <textarea placeholder="发表我的见解..." v-model="commentInput" @confirm="submitPublication($event)"
                    confirm-type="send" maxlength="100"/>
        </view>
      </view>
    </uni-popup>
  </view>

</template>

<script>

import {deletedComment, publicationComment} from "@/api/function";
import env from "@/utils/env";
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";
import {conversionTime} from "@/utils/date";

export default {
  components: {EmptyComponent},
  computed: {
    env() {
      return env
    }
  },
  props: {
    commentData: {
      type: Array,
      default: () => {
      }
    },
    isLogin: {
      type: String,
      default: () => {
      }
    }
  },
  data() {
    return {
      commentInput: ''
    };
  }
  , methods: {
    conversionTime,
    /**
     * 长按事件
     * @param id
     * @param permissions
     * @param index
     */
    onLongPress: function (id, permissions, index) {
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
                await deletedComment({
                  seaCommentId: id
                });
                _this.$parent.commentData.splice(index, 1)
                await _this.$parent.getBlogComment()
                uni.hideLoading();
                uni.showToast({
                  title: '删除成功',
                  icon: 'none',
                  duration: 2000
                })
              } catch (e) {
                uni.showToast({
                  title: e,
                  icon: 'none',
                  duration: 4000
                });
              }

            }
          }
        });
      }

    },
    /**
     * 查看回复
     */
    toReply: function (seaCommentId, userName) {
      uni.navigateTo({
        url: '/pages/blog/view/replyView?seaCommentId=' + seaCommentId + (userName !== '' ? '&userName=' + userName : '')
      })
    },
    /**
     *  发表评论
     */
    handlePublicationOpen: function () {
      if (!this.$parent.isLogin) {
        uni.reLaunch({
          url: '/pages/master/master?currentPage=1'
        })
        return
      }
      this.$refs.publicationCommentRef.open('bottom')
    },
    /**
     *  提交评论
     */
    submitPublication: async function (e) {
      const {commentInput, $parent} = this;
      if (!commentInput) {
        uni.showToast({
          title: '评论内容不能为空',
          icon: 'none',
          duration: 2000
        })
        return;
      }
      try {
        uni.showLoading({
          title: '正在发表评论 ing~',
          mask: true
        });
        await publicationComment({
          commentContent: commentInput,
          seaBlogId: $parent.seaBlogId
        });
        if ($parent.commentData.length < 10) {
          $parent.currentPage = 0;
          await $parent.getBlogComment();
        }
        uni.hideLoading();
        uni.showToast({
          title: '发表成功',
          icon: 'none',
          duration: 4000
        });

        this.$refs.publicationCommentRef.close();
        this.commentInput = '';
      } catch (e) {
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 4000
        });
      }
    }

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

.reply_btn {
  margin-top: 20rpx;
  padding-left: 95rpx;
  font-size: 23rpx;
  color: rgb(56, 86, 109)
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
