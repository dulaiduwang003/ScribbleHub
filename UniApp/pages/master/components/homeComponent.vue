<template>
  <view class="home-container">
    <scroll-view scroll-y class="home-scroll" lower-threshold="100" @scrolltolower="onScrollToLower()">
      <!--轮播图-->
      <view class="time-container">
        <view class="head-calendar">
          <image class="time-image" src="/static/images/individual/snow.jpg"/>
          <view class="calendar-content">
            <view class="calendar-flex">
              <view class="calendar-name">to day</view>
              <view class="calendar-day">{{ getDay() }}</view>
            </view>
          </view>
        </view>
        <swiper class="recommend-carousel" circular :indicator-dots="false" :autoplay="true" interval="3000">
          <swiper-item v-for="(item,index) in marquee" :key="index">
            <view class="carousel-model" @click="toBlogDetail(item.seaBlogId)">
              <image class="carousel-image" :src="env.baseUrl+item.cover"/>
              <!--浮动于轮播图上方-->
              <view class="image-text-container">
                <view>
                  <view class="image-column">
                    {{ item.classifyName }}
                  </view>
                  <view class="image-introduce">
                    {{ item.title }}
                  </view>
                </view>
              </view>
            </view>
          </swiper-item>
        </swiper>
      </view>
      <!--分类栏目-->
      <view class="column-container">
        <van-grid clickable icon-size="50rpx" square :column-num="5" round :border="false">
          <van-grid-item :icon="item.icon" icon-color="rgb(238,179,118)"
                         v-for="(item,index) in category" :key="index" :text="item.name"
                         @click="toTopic(item.isType)"/>
          <van-grid-item icon="search" icon-color="rgb(238,179,118)" text="搜索"
                         value="商品搜索"
                         @click="toSearch()"/>
        </van-grid>
      </view>
      <!--专栏-->
      <view class="column-container column-spacing">
        <view>

        </view>
        <view class="column-title">
          CODE LAB
        </view>
        <swiper class="special-swiper" circular :indicator-dots="false" :interval="3000" :duration="500"
                previous-margin="0" next-margin="20">
          <swiper-item v-for="(item,index) in classifyMarquee" :key="index" @click="toClassify(item.seaClassifyId)">
            <view class="column-swiper_item">
              <image :src="env.baseUrl+item.cover"/>
              <view class="column-swiper_item_text">
                <view class="special-text">
                  {{ item.isType === 0 ? '前端' : item.isType === 1 ? '后端' : item.isType === 2 ? '中间件' : '其他' }}
                </view>
                <view class="special-name">
                  {{ item.classifyName }}
                </view>
                <view class="special-statistics">
                  共 {{ item.articles > 100 ? '100+' : item.articles }} 篇文章
                </view>
              </view>
            </view>

          </swiper-item>
        </swiper>
      </view>
      <!--BLOG-->
      <view class="column-container column-spacing bottle">
        <view class="column-title">
          MOST READ
        </view>
        <view class="blog-model" v-for="(item,index) in blogData " :key="index" @click="toBlogDetail(item.seaBlogId)">
          <view class="blog-title">
            {{ item.title }}
          </view>
          <view class="blog-info">
            <view class="blog-avatar">
              <image :src="item.avatar?env.baseUrl+item.avatar: '/static/images/individual/defaultAvatar.jpg'"/>
            </view>
            <view class="blog-author">
              {{ item.userName ? item.userName : env.author }}
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
              发布于 {{ conversionTime(item.createdTime) }}
            </view>
            <view class="reading-spacing">
              阅读量 {{ item.reading > 1000 ? '1000+' : item.reading }}
            </view>
          </view>
        </view>
        <loading-data-component v-if="isLoading"/>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import EmptyComponent from "@/wxcomponents/components/EmptyComponent.vue";
import env from "@/utils/env";
import {conversionTime} from "@/utils/date";
import category from "@/static/data/category";
import LoadingDataComponent from "@/wxcomponents/components/LoadingDataComponent.vue";

export default {
  props: {
    marquee: {
      type: Array,
      default: () => {
      }
    },
    blogData: {
      type: Array,
      default: () => {
      }
    },
    classifyMarquee: {
      type: Array,
      default: () => {
      }
    }
  },
  data() {
    return {
      isLoading: false
    }
  },
  computed: {
    category() {
      return category.list
    },
    env() {
      return env
    }
  },
  components: {LoadingDataComponent, EmptyComponent},

  methods: {
    conversionTime,

    /**
     * 跳转至详细文章
     */
    toBlogDetail: function (seaBlogId) {
      uni.navigateTo({
        url: '/pages/blog/blog?seaBlogId=' + seaBlogId
      })

    },

    /**
     * 跳转至主题
     */
    toTopic: function (type) {
      uni.navigateTo({
        url: '/pages/topic/topic?isType=' + type
      })
    },
    /**
     * 跳转至搜索页
     */
    toSearch: function () {
      uni.navigateTo({
        url: '/pages/search/search'
      })
    },
    /**
     * 跳转至专题
     */
    toClassify: function (id) {
      uni.navigateTo({
        url: '/pages/classify/classify?seaClassifyId=' + id
      })

    },
    /**
     * 触底刷新分页数据
     */
    onScrollToLower() {
      // 每次触底时加载下一页数据
      this.$parent.handleBlogPagination();
    }
    ,
    getDay() {
      const date = new Date()
      return ('0' + date.getDate()).slice(-2)
    }
  }
}
</script>

<style lang="scss">

.slide-animation {
  animation: slideEase 0.5s ease-in-out forwards;
}

@keyframes slideEase {
  0% {
    transform: translateX(-100px);
  }
  100% {
    transform: translateX(0);
  }
}

.recommend-carousel {
  height: 280rpx;
  width: 510rpx;
}

.volume_container {
  font-size: 18rpx;
  color: #636363;
  padding-top: 20rpx;
  display: flex;
  align-items: center
}

.column-container {
  background-color: rgb(20, 20, 20);
  padding: 20rpx;
  border-radius: 30rpx;
  margin-top: 40rpx

}

van-grid-item {
  /* 定义样式 全局生效 */
  --grid-item-content-background-color: rgb(20, 20, 20);
  --grid-item-content-active-color: rgb(58, 57, 57);
  --grid-item-text-color: rgb(255, 255, 255);
}

.special-swiper {
  height: 320rpx
}

.image-text-container {
  color: white;
  position: absolute;
  z-index: 2;
  top: 0;
  left: 0;
  height: 280rpx;
  width: 510rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center
}

.column-title {
  font-size: 35rpx;
  font-weight: 800;
  color: #ffffff;
  padding-bottom: 20rpx;
}

.column-spacing {
  padding-top: 30rpx;
  padding-bottom: 30rpx;

}

.column-swiper_item_text {
  position: absolute;
  z-index: 2;
  font-size: 40rpx;
  top: 0;
  left: 0;
  width: 93%;
  padding: 20rpx;
  color: white;


}

.image-column {
  font-size: 30rpx;
  font-weight: 550;

}

.image-introduce {
  font-size: 25rpx;

  padding-top: 20rpx
}

.carousel-image {
  border-radius: 25rpx;
  height: 280rpx;
  width: 510rpx;
  filter: brightness(70%);

}

.column-swiper_item {
  width: 98%;
  position: relative;

}

.home-scroll {
  height: 100vh;
}

.home-container {
  padding: 20rpx;
  animation: fadeIn 0.5s ease-in-out forwards;
}

.special-text {
  font-size: 35rpx;

}

.special-name {
  text-align: center;
  padding-top: 50rpx;
  font-size: 35rpx;
  font-weight: 700;
}

.special-statistics {
  text-align: right;
  padding-top: 5.8vh;
  font-size: 20rpx
}

.column-swiper_item image {
  width: 100%;
  height: 300rpx;
  filter: brightness(0.7);
  border-radius: 25rpx;
}

.blog-model {
  background-color: #0e0e0e;
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
  font-size: 23rpx;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 20rpx;
  color: #787878;
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
  color: #515051;
  font-size: 25rpx;
}

.head-calendar {
  position: relative
}

.calendar-content {
  align-items: center;
  height: 280rpx;
  width: 180rpx;
  top: 0;
  left: 0;
  z-index: 2;
  position: absolute;
  display: flex;
  justify-content: center
}

.calendar-flex {
  color: white;
  text-align: center;
  font-weight: 650
}

.calendar-name {
  font-size: 40rpx;
}

.calendar-day {
  font-size: 55rpx;
  padding-top: 20rpx
}

.bottle {
  padding-bottom: 5vh;
}
</style>
