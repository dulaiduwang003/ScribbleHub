<template>
  <view class="hot-container">
    <van-tabs :active="0" animated>
      <van-tab title="前端">
        <view class="tab-model" v-if="typeof data.front !== 'undefined' && data.front.length>0">
          <view :class="index===0||index===1||index===2?'tab-row-active ellipsis':'tab-row-default ellipsis'"
                v-for="(item,index) in data.front" :key="index" @click="this.$parent.toBlogDetail(item.seaBlogId)">
            <text class="mark"> {{ index + 1 }}</text>
            .{{ item.title }}
          </view>
        </view>
        <view class="no-data" v-else>
          暂无数据
        </view>
      </van-tab>
      <van-tab title="后端">
        <view class="tab-model" v-if="typeof data.backEnd !== 'undefined' &&data.backEnd.length>0">
          <view :class="index===0||index===1||index===2?'tab-row-active ellipsis':'tab-row-default ellipsis'"
                v-for="(item,index) in data.backEnd" :key="index" @click="this.$parent.toBlogDetail(item.seaBlogId)">
            <text class="mark"> {{ index + 1 }}</text>
            . {{ item.title }}
          </view>
        </view>
        <view class="no-data" v-else>
          暂无数据
        </view>
      </van-tab>
      <van-tab title="中间件">
        <view class="tab-model" v-if="typeof data.middleware !== 'undefined' && data.middleware.length>0">
          <view :class="index===0||index===1||index===2?'tab-row-active ellipsis':'tab-row-default ellipsis'"
                v-for="(item,index) in data.middleware" :key="index" @click="this.$parent.toBlogDetail(item.seaBlogId)">
            <text class="mark"> {{ index + 1 }}</text>
            . {{ item.title }}
          </view>
        </view>
        <view class="no-data" v-else>
          暂无数据
        </view>
      </van-tab>
      <van-tab title="其他">
        <view class="tab-model" v-if="typeof data.other !== 'undefined' && data.other.length>0">
          <view :class="index===0||index===1||index===2?'tab-row-active ellipsis':'tab-row-default ellipsis'"
                v-for="(item,index) in data.other" :key="index" @click="this.$parent.toBlogDetail(item.seaBlogId)">
            <text class="mark"> {{ index + 1 }}</text>
            . {{ item.title }}
          </view>
        </view>
        <view class="no-data" v-else>
          暂无数据
        </view>
      </van-tab>
    </van-tabs>

  </view>
</template>

<script>
export default {
  props: {
    data: {
      type: Object,
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
.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.hot-container{
  animation: fadeIn 0.5s ease-in-out forwards;
}
.tab-row-active {
  width: 300rpx;
  padding: 20rpx;
  color: rgb(238, 179, 118);
}

.tab-model {
  padding: 4%;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  color: #9d9d9d;
  font-size: 25rpx
}

.mark {
  font-size: 27rpx
}

.no-data {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300rpx;
  color: #707070
}

.tab-row-default {
  width: 300rpx;
  padding: 20rpx
}
</style>
