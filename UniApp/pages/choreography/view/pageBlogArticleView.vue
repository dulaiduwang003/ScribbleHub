<template>
  <view class="container" v-if="blogData.length>0">
    <van-swipe-cell right-width="80" left-width="80" v-for="(item,index) in blogData " :key="index">
      <view slot="left">
        <view :class="item.isRecommend===1?'left-slot-selected' :'left-slot-default'"
              @click="handleRecommend(item.seaBlogId)">
          {{ item.isRecommend === 1 ? '取消推荐' : '推荐' }}
        </view>
      </view>
      <view class="blog-model">
        <view class="blog-title">
          {{ item.title }}
        </view>
        <view class="blog-content">
          <view class="blog-summary">
            {{ item.summary }}
          </view>
          <view>
            <image class="blog-cover"
                   :src="env.baseUrl+item.cover"/>
          </view>
        </view>
        <view class="volume_container">
          <view>
            创建于{{ formatDate(item.createdTime) }}
          </view>
        </view>
      </view>
      <view slot="right">
        <view class="right-slot" @click="handleBlodDeleted(item.seaBlogId)">
          删除
        </view>
      </view>
    </van-swipe-cell>
  </view>
  <empty-component :height="90" v-else/>
</template>

<script>

import {deletedBlogArticle, getAllBlogPosts, setRecommend} from "@/api/admin";
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";
import env from "@/utils/env";

export default {
  computed: {
    env() {
      return env
    }
  },
  components: {EmptyComponent},
  data() {
    return {
      blogData: []
    };
  }, created() {
    this.handleInitData()
  }, methods: {
    /**
     * 初始化信息
     */
    handleInitData: async function () {
      try {
        let newVar = await getAllBlogPosts();
        if (newVar) {
          this.blogData = newVar
        }
      } catch (e) {
        console.log(e)
      }
    },
    /**
     * （删除）
     * @param id
     * @returns {Promise<void>}
     */
    handleBlodDeleted: async function (id) {
      uni.showLoading({
        title: '加载中'
      })
      try {
        await deletedBlogArticle({
          seaBlogId: id
        })
        await this.handleInitData();
        uni.hideLoading()
      } catch (e) {
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 2000
        })
      }
    },
    /**
     * 设置推荐
     * @param id
     * @returns {Promise<void>}
     */
    handleRecommend: async function (id) {
      uni.showLoading({
        title: '加载中'
      })
      try {
        await setRecommend({
          seaBlogId: id
        });
        await this.handleInitData();
        uni.hideLoading()
      } catch (e) {
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 2000
        })
      }
    },

    /**
     * 转化年月日
     * @param timestamp
     * @returns {string}
     */
    formatDate(timestamp) {
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = ('0' + (date.getMonth() + 1)).slice(-2)
      const day = ('0' + date.getDate()).slice(-2)
      const hour = ('0' + date.getHours()).slice(-2)
      const minute = ('0' + date.getMinutes()).slice(-2)
      const second = ('0' + date.getSeconds()).slice(-2)
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style lang="scss">

page {
  background-color: black;
}

.blog-model {
  background-color: #26262f;
  border-radius: 25rpx;;
  padding: 20rpx;
  color: white;
  margin-bottom: 30rpx;
  height: 240rpx;
}

.blog-title {
  font-size: 28rpx;
  font-weight: 550;
  padding-bottom: 20rpx
}

.blog-info {
  display: flex;
  align-items: center;
  font-weight: 550
}

.blog-avatar {
  border-radius: 100%;
  height: 50rpx;
  width: 50rpx;
  overflow-x: hidden;
  margin-right: 20rpx
}

.blog-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 10rpx
}

.container {
  padding: 40rpx
}

.left-slot-default {
  border-radius: 10rpx;
  font-size: 30rpx;
  height: 280rpx;
  color: white;
  background-color: #6b2424;
  width: 140rpx;
  display: flex;
  justify-content: center;
  align-items: center
}

.left-slot-selected {
  border-radius: 10rpx;
  font-size: 30rpx;
  height: 280rpx;
  color: white;
  background-color: #6b2452;
  width: 140rpx;
  display: flex;
  justify-content: center;
  align-items: center
}

.right-slot {
  border-radius: 10rpx;
  font-size: 30rpx;
  height: 280rpx;
  color: white;
  background-color: #9b1111;
  width: 140rpx;
  display: flex;
  justify-content: center;
  align-items: center
}

.blog-summary {
  font-size: 23rpx;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 20rpx;
  word-break: break-all;
}

.blog-cover {
  width: 200rpx;
  height: 120rpx;
  border-radius: 20rpx
}

.reading-spacing {
  padding-left: 30rpx
}

.carousel-model {
  position: relative;
}

.blog-avatar image {
  width: 100%;
  height: 100%
}

.volume_container {
  font-size: 18rpx;
  color: #636363;
  padding-top: 20rpx;
  display: flex;
  align-items: center
}

.time-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.time-image {
  height: 280rpx;
  width: 180rpx;
  border-radius: 24rpx
}
</style>
