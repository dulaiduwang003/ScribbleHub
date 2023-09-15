<template>

  <view class="classify-container">
    <view class="head-container">
      <image :src="cover?env.baseUrl+cover:'/static/images/individual/twilight.jpeg'"/>
      <view class="title">
        {{ classifyName }}
      </view>
    </view>
    <!--    内容-->
    <view class="content-container" v-if="blogData.length>0">
      <view class="blog-model" v-for="(item,index) in blogData " :key="index"
            @click="toBlogDetail(item.seaBlogId)">
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
            创建于 {{ formatDate(item.createdTime) }}
          </view>
          <view class="reading-spacing">
            阅读量 {{ item.reading > 1000 ? '1000+' : item.reading }}
          </view>
        </view>
      </view>
    </view>
    <empty-component v-else msg="该专栏下空空如也" :height="60"/>
    <loading-component ref="loading" :degree="0.7"/>
  </view>
</template>

<script>

import {articlesBasedOnFeaturedId} from "@/api/public";
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
      seaClassifyId: undefined,
      classifyName: '',
      blogData: [],
      cover: undefined
    };
  },
  onLoad(option) {
    this.seaClassifyId = option.seaClassifyId;
    this.getArticlesBasedOnFeaturedId(this.seaClassifyId)
  }, methods: {
    formatDate,
    /**
     * 获取数据
     */
    getArticlesBasedOnFeaturedId: async function (id) {
      let loading = this.$refs.loading;
      try {
        loading.handlePopupOpen();
        let promise = await articlesBasedOnFeaturedId(id);
        if (promise) {
          this.blogData = promise.list
          this.classifyName = promise.classifyName
          this.cover = promise.cover
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          title: e,
          icon: 'none',
          duration: 2000
        })
      } finally {
        setTimeout(() => {
          loading.handlePopupClose()
        }, 500)
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

page {
  background-color: black;
}

.content-container {
  padding: 30rpx
}

.head-container {
  box-shadow: 0 -50px 30px -10px #000000 inset;
}

.head-container image {
  width: 750rpx;
  height: 400rpx;
  position: relative;
  z-index: -3
}

.title {
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

.classify-container {
  animation: fadeIn 0.8s ease-in-out forwards;
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

