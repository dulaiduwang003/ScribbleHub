<template>
  <view>
    <view class="history-container">
      <!--      搜索头部-->
      <view class="history-head">
        <view class="history-items">
          <van-icon name="clock-o"/>
          <view class="history-title">搜索记录</view>
        </view>
        <view @click="this.$parent.clearHistory">
          清空
        </view>
      </view>
      <!-- 记录行-->
      <view class="history-model" v-for="(item,index) in searchHistory" :key="index">
        <view class="row-flex">
          <view class="record-ellipsis" @click="this.$parent.keyWords=item">{{ item }}</view>
          <van-icon name="cross" size="30rpx" @click="this.$parent.clearDesignateHistory(index)"/>
        </view>
        <van-divider/>
      </view>
    </view>

  </view>
</template>

<script>

export default {
  props: {
    searchHistory: {
      type: Array,
      default: () => {
      }
    }
  },
  data() {
    return {};
  }
}
</script>

<style lang="scss">
.record-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 500rpx
}

.history-container {
  color: #797979;
  width: 740rpx;
  margin-top: 70rpx
}

.history-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 50rpx;
  font-size: 30rpx
}

.history-title {
  padding-left: 10rpx
}

.history-model {
  padding: 0 50rpx;
  font-size: 24rpx;
  margin-top: 50rpx
}

.row-flex {
  display: flex;
  align-items: center;
  justify-content: space-between
}

.history-items {
  display: flex;
  align-items: center
}
</style>
