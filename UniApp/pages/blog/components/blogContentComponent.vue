<template>
  <view>
    <view class="head-classify-container" @click="toClassify(blogData.seaClassifyId)">
      <view>
        <view class="classify-title">收录于专题</view>
        <view class="classify-val">#{{ blogData.classifyName }}</view>
      </view>
      <view class="articles">
        {{ blogData.articles }} 篇
      </view>
    </view>
    <mp-html :copy-link="true" :tagStyle="md.tagStyle" :container-style="md.tagStyle" :markdown="true" :lazy-load="true"
             :selectable="true" :content="blogData.content"/>
    <van-divider/>
    <view>
      <view class="extra-info-container">
        <view class="extra-label">标签:</view>
        <view class="label-model" >
          <view class="label-value" v-for="(item,index) in blogData.label" :key="index">{{ item }}</view>
        </view>
      </view>
      <view class="extra-date">
        最后更新时间: {{ formatDate(blogData.createdTime) }}
      </view>
    </view>
  </view>
</template>

<script>

import mpHtml from "@/wxcomponents/mp-html/mp-html.vue";
import md from "@/static/css/md";
import {formatDate} from "@/utils/date";

export default {
  methods: {
    formatDate,
    /**
     * 跳转至专题
     */
    toClassify: function (id) {
      uni.navigateTo({
        url: '/pages/classify/classify?seaClassifyId=' + id
      })

    },
  },
  props: {
    blogData: {
      type: Object,
      default: () => {
      }
    }
  },
  computed: {
    md() {
      return md
    }
  },
  components: {
    mpHtml,
  }

}
</script>

<style lang="scss">
.head-classify-container {
  display: flex;
  align-items: center;
  background-color: rgb(30, 30, 30);
  border-radius: 20rpx;
  padding: 4%;
  margin-bottom: 5%;
  justify-content: space-between;

}

.classify-title {
  color: rgb(125, 125, 125);
  margin-bottom: 4%;
}

.classify-val {
  color: rgb(105, 130, 180);
  font-size: 26rpx;
}

.extra-info-container {
  display: flex;
  align-items: flex-start;
  color: white
}

.extra-label {
  color: #b4b2b6;
  font-size: 27rpx;
  width: 100rpx;
}

.extra-date {
  display: flex;
  align-items: center;
  margin-top: 30rpx;
  color: #b4b2b6;
  font-size: 27rpx
}

.label-model {

  display: flex;
  flex-wrap: wrap;
  align-items: center
}

.label-value {
  padding: 8rpx 20rpx;
  font-size: 24rpx;
  background-color: #332858;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 15rpx;
  margin-right: 10rpx;
  margin-bottom: 10rpx;
}

.articles {
  text-align: right;
  color: rgb(125, 125, 125);
}
</style>
