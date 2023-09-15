<template>
  <view class="container" v-if="drawingData.length>0">
    <van-swipe-cell right-width="80" v-for="(item,index) in drawingData " :key="index">
      <view class="image">
        <image :src="env.baseUrl+item.imageUrl" mode="widthFix" @click="toDrawingDetail(item.seaImageId)"/>
      </view>
      <view slot="right">
        <view :class="item.isPublic==='1'?'right-slot-selected':'right-slot-default'" @click="setPublicDrawing(item.seaImageId)">
          {{ item.isPublic === '1' ? '取消公开' : '公开' }}
        </view>
      </view>
    </van-swipe-cell>
  </view>
  <empty-component :height="90" v-else/>
</template>

<script>

import {getAllDrawings, setPublicDrawing} from "@/api/admin";
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
      drawingData: []
    };
  }, created() {
    this.handleInitData()
  }, methods: {
    /**
     * 绘图详情
     * @param e
     */
    toDrawingDetail: function (e) {
      uni.navigateTo({
        url: '/pages/super/view/drawingDetailedView?seaImageId=' + e
      })
    },
    /**
     * 修改绘图状态
     * @param id
     * @returns {Promise<void>}
     */
    setPublicDrawing: async function (id) {
      try {
        uni.showLoading({
          title: '正在操作中 ~',
          mask: true
        });
        await setPublicDrawing({
          seaImageId: id
        });
        await this.handleInitData();
        uni.hideLoading()
      } catch (e) {
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: e
        });
      }

    },
    /**
     * 初始化信息
     */
    handleInitData: async function () {
      try {
        let newVar = await getAllDrawings();
        if (newVar) {
          this.drawingData = newVar
        }
      } catch (e) {
        uni.showToast({
          title: "获取数据失败",
          icon: 'none',
          duration: 2000
        })
      }
    }
  }
}
</script>

<style lang="scss">

page {
  background-color: black;
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


.right-slot-default {
  margin-top: 10rpx;
  border-radius: 10rpx;
  font-size: 30rpx;
  height: 650rpx;
  color: white;
  background-color: #9b1111;
  width: 140rpx;
  display: flex;
  justify-content: center;
  align-items: center
}

.right-slot-selected {
  margin-top: 10rpx;
  border-radius: 10rpx;
  font-size: 30rpx;
  height: 640rpx;
  color: white;
  background-color: #6432a5;
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

  height: 600rpx;
  border-radius: 20rpx;

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
