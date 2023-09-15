<template>
  <view class="topic-container">
    <!--    头部-->
    <view class="head-container">
      <view class="wave_animation">
        <image :class="'wave '+ isType" src="/static/images/individual/attach.jpeg"></image>
      </view>
      <view class="wave_title">
        {{ title }}
      </view>
    </view>
    <!--    内容-->
    <view class="content-container" v-if="blogData.length>0">
      <view class="blog-model" v-for="(item,index) in blogData " :key="index" @click="toBlogDetail(item.seaBlogId)">
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
    <empty-component v-else msg="该主题下空空如也" :height="60"/>
    <loading-component ref="loading" :degree="0.7"/>
  </view>
</template>

<script>

import {blogBasedOnType} from "@/api/public";
import env from "@/utils/env";
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";
import LoadingComponent from "@/wxcomponents/components/LoadingComponent.vue";
import {formatDate} from "@/utils/date";


export default {
  components: {LoadingComponent, EmptyComponent},
  computed: {
    env() {
      return env
    }
  },
  data() {
    return {
      isType: 'type-',
      title: '',
      blogData: []
    };
  },
  onLoad(option) {
    let loading = this.$refs.loading;
    loading.handlePopupOpen();
    this.isType += option.isType;
    switch (option.isType) {
      case '0':
        this.title = '前端';
        break;
      case '1':
        this.title = '后端';
        break;
      case '2':
        this.title = '中间件';
        break;
      case '3':
        this.title = '其他';
        break;
    }
    this.getBlogBasedOnType(option.isType)
    setTimeout(() => {
      loading.handlePopupClose()
    }, 500)
  }, methods: {
    formatDate,
    /**
     * 获取数据
     */
    getBlogBasedOnType: async function (type) {
      try {
        let promise = await blogBasedOnType(type);
        if (promise) {
          this.blogData = promise
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: '获取数据失败'
        });
      }
    },
    /**
     * 跳转至详细文章
     */
    toBlogDetail: function (seaBlogId) {
      uni.navigateTo({
        url: '/pages/blog/blog?seaBlogId=' + seaBlogId
      })

    }
  }
}
</script>

<style lang="scss">

.topic-container {
  animation: fadeIn 0.8s ease-in-out forwards;
}
.content-container {
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

.wave_animation {

  width: 720rpx;
  height: 200px;
  animation: wave 10s ease-in-out infinite;
}

.wave {
  width: 100%;
  height: 200px;
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

.bottle {
  padding-bottom: 5vh;
}
</style>

