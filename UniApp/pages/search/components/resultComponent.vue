<template>

  <view class="result-container">
    <view class="result-container" v-if="blogData.length>0">
      <view class="blog-model" v-for="(item,index) in blogData " :key="index"
            @click="this.$parent.toBlogDetail(item.seaBlogId)">
        <view class="blog-title">
          {{ item.title }}
        </view>
        <view class="blog-info">
          <view class="blog-avatar">
            <image :src="item.avatar?env.baseUrl+item.avatar: '/static/images/individual/defaultAvatar.jpg'"/>
          </view>
          <view class="blog-author">
            {{ item.userName ? item.userName :  env.author }}
          </view>
        </view>
        <view class="blog-content">
          <view class="blog-summary">
            {{ item.summary }}
          </view>
          <view>
            <image class="blog-cover"
                   :src="env.baseUrl+item.uri"/>
          </view>
        </view>
        <view class="volume_container">
          <view>
            创建于 {{ formatDate(item.createdTime) }}
          </view>
          <view class="reading-spacing">
            阅读量 {{ item.reading > 1000 ? '1000+' : item.reading }}
          </view>
        </view>
      </view>
    </view>
    <empty-component v-else msg="没有找到任何文章" :height="60"/>
    <loading-component ref="loading" :degree="0.7"/>
  </view>
</template>

<script>

import env from "@/utils/env";
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";
import LoadingComponent from "@/wxcomponents/components/LoadingComponent.vue";
import {formatDate} from "@/utils/date";


export default {
  props: {
    blogData: {
      type: Array,
      default: () => {
      }
    }
  },
  components: {LoadingComponent, EmptyComponent},
  computed: {
    env() {
      return env
    }
  },
  methods: {
    formatDate,

  }
}
</script>

<style lang="scss" scoped>

.result-container {
  padding: 30rpx
}

.head-container {
  width: 740rpx;
  overflow-x: hidden;
  position: relative
}

.wave_title {
  font-size: 40rpx;
  color: white;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 2;
  height: 400rpx;
  width: 740rpx;
  display: flex;
  justify-content: center;
  align-items: center
}

.wave {
  width: 100%;
  height: 200px;
  background-image: url("/static/images/individual/twilight.jpeg");
  background-size: cover;

}

.type-0 {
  filter: grayscale(100%) brightness(50%) sepia(100%) hue-rotate(200deg);
  transform: perspective(500rpx) rotateY(-120deg) rotateX(20deg) skew(-25deg, -22deg);
}

.type-1 {
  filter: hue-rotate(180deg);
  transform: perspective(300rpx) rotateY(-60deg) rotateX(10deg) skew(-20deg, -25deg);
}

.type-2 {
  filter: hue-rotate(270deg);
  transform: perspective(400rpx) rotateY(-21deg) rotateX(20deg) skew(-30deg, -25deg);
}

.type-3 {
  filter: hue-rotate(330deg);
  transform: perspective(500rpx) rotateY(-160deg) rotateX(23deg) skew(-50deg, -15deg);
}

@keyframes wave {
  0% {
    transform: translate3d(0, 0, 0);
  }
  50% {
    transform: translate3d(10%, 10%, 0);
  }
  100% {
    transform: translate3d(0, 0, 0);
  }
}

.blog-model {
  background-color: #171717;
  border-radius: 25rpx;;
  padding: 20rpx;
  color: white;
  margin-bottom: 30rpx;
}

.blog-title {
  font-size: 28rpx;
  font-weight: 550;
  padding-bottom: 20rpx;
  color: #a2a2a2;
}

.blog-info {
  display: flex;
  align-items: center;
  font-weight: 550;
  font-size: 25rpx;
}

.volume_container {
  font-size: 18rpx;
  color: #636363;
  padding-top: 20rpx;
  display: flex;
  align-items: center
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

.blog-summary {
  color: #787878;
  font-size: 23rpx;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 20rpx
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

.blog-author {
  font-size: 25rpx;
  color: #515051
}

.result-container {
  animation: fadeIn 0.5s ease-in-out forwards;
}

.bottle {
  padding-bottom: 5vh;
}
</style>

