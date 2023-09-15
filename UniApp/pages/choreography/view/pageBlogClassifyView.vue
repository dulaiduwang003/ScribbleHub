<template>
  <view class="container" v-if="classifyData.length>0">
    <van-swipe-cell right-width="80" v-for="(item,index) in classifyData " :key="index">
      <view class="blog-model">
        <view class="image">
          <image :src="env.baseUrl+item.cover"/>
        </view>
        <view class="classify-title">
          {{ item.classifyName }}
        </view>
        <view class="volume_container">
          <view>
            创建于{{ formatDate(item.createdTime) }}
          </view>
        </view>
      </view>
      <view slot="right">
        <view class="right-slot" @click="handleDeleted(item.seaClassifyId)">
          删除
        </view>
      </view>
    </van-swipe-cell>
  </view>
  <empty-component :height="90" v-else/>
</template>

<script>

import {deletedClassify, getAllClassify} from "@/api/admin";
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
      classifyData: []
    };
  }, created() {
    this.handleInitData()
  }, methods: {
    /**
     * 初始化信息
     */
    handleInitData: async function () {
      try {
        let newVar = await getAllClassify();
        if (newVar) {
          this.classifyData = newVar
        }
      } catch (e) {
        uni.showToast({
          title: "获取数据失败",
          icon: 'none',
          duration: 2000
        })
      }
    },
    /**
     * 删除专栏
     * @param id
     * @returns {Promise<void>}
     */
    handleDeleted: async function (id) {
      try {
        await deletedClassify({
          seaClassifyId: id
        })
        await this.handleInitData()
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
  position: relative;

  background-color: #26262f;
  border-radius: 25rpx;;
  padding: 20rpx;
  color: white;
  margin-bottom: 30rpx;

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


.right-slot {
  border-radius: 10rpx;
  font-size: 30rpx;
  height: 430rpx;
  color: white;
  background-color: #9b1111;
  width: 140rpx;
  display: flex;
  justify-content: center;
  align-items: center
}


.volume_container {
  padding-left: 20rpx;
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

.image {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding-top: 10rpx;
}

.image image {
  width: 600rpx;
  height: 330rpx;
  border-radius: 20rpx;
  filter: brightness(50%);
}

.classify-title {
  position: absolute;
  z-index: 2;
  color: white;
  font-size: 35rpx;
  font-weight: 550;
  top: 0;
  left: 0;
  height: 420rpx;
  width: 670rpx;
  display: flex;
  justify-content: center;
  align-items: center

}

.time-image {
  height: 280rpx;
  width: 180rpx;
  border-radius: 24rpx
}
</style>
