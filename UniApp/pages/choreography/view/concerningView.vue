<template>
  <view class="container">
    <view class="spacing">
      <image src="/static/assets/github.svg"/>
      <view class="title">
        从琴房到教室一共一百零八步 也不是那么幸运每次都能遇见你
      </view>
      <view class="link">
        github @dulaiduwang003
      </view>
      <view class="label_container">
        <view class="java">
          Java
        </view>
        <view class="net">
          C#
        </view>
        <view class="vue">
          Vue
        </view>
        <view class="uni">
          UniApp
        </view>
      </view>
    </view>

  </view>
</template>

<script>
export default {
  data() {
    return {};
  }
}
</script>

<style lang="scss">
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  text-align: center;
  color: white;
  font-size: 50rpx;
  font-weight: 550;

}

.label_container {
  margin-top: 130rpx;
  display: flex;
  justify-content: space-between;
  font-size: 23rpx;
  padding: 20rpx;
  padding-left: 140rpx;
  padding-right: 140rpx
}

.java {
  padding: 8rpx 20rpx;
  border-radius: 15rpx;
  background-color: #d76d16
}

.net {
  padding: 10rpx 30rpx;
  border-radius: 15rpx;
  background-color: #1677d7
}

.vue {
  padding: 10rpx 30rpx;
  border-radius: 15rpx;
  background-color: #f14e7f
}

.uni {
  padding: 10rpx 30rpx;
  border-radius: 15rpx;
  background-color: #22842b
}

.spacing {
  padding-top: 80rpx;
  padding-bottom: 25vh;
}

.title {
  padding-left: 130rpx;
  padding-right: 130rpx;
  font-size: 32rpx;
  padding-bottom: 40rpx;
  padding-top: 50rpx
}

.link {
  font-size: 27rpx;
  color: #5a5a5a;
  font-weight: 500
}

image {
  width: 200rpx;
  height: 200rpx;
}
</style>
