<template>

  <view class="search-container">
    <!--搜索框-->
    <view class="search" :style="{'padding-top':navBarHeight +'px'}">
      <view class="search-layout">
        <view class="search-former">
          <van-icon name="search" color="white"/>
          <input type="text" placeholder="搜索文章" v-model="keyWords" @confirm="search($event)" confirm-type="search"
                 @input="keyWordChange($event)" maxlength="50"/>
        </view>
      </view>
    </view>
    <!--内容-->
    <view class="content-container">
      <!--热门-->
      <hot-component v-if="steps===0" :data="data"/>
      <!--搜索记录-->
      <history-component v-if="steps===0 && this.searchHistory.length>0" :search-history="searchHistory"/>
      <!--搜索结果-->
      <result-component v-if="steps===1" :blog-data="blogData"/>
    </view>
    <!--    加载-->
    <loading-component ref="loading" :degree="1"/>
  </view>
</template>


<script>

import HotComponent from "@/pages/search/components/hotComponent.vue";
import HistoryComponent from "@/pages/search/components/historyComponent.vue";
import ResultComponent from "@/pages/search/components/resultComponent.vue";
import {fetchDataRandomly, searchArticle} from "@/api/public";
import {getHistory, removeHistory, setHistory} from "@/utils/utils";
import LoadingComponent from "@/wxcomponents/components/LoadingComponent.vue";

export default {
  components: {LoadingComponent, ResultComponent, HistoryComponent, HotComponent},
  data() {
    return {
      //胶囊高度
      navBarHeight: undefined,
      //当前展示那一部分 0表示热门 1结果
      steps: 0,
      data: {},
      keyWords: '',
      searchHistory: [],
      blogData: []
    }
  },
  created() {
    //获取小程序胶囊的高度
    let {
      top
    } = uni.getMenuButtonBoundingClientRect()
    //高度 =（胶囊底部高低-状态栏高度）+（胶囊顶部高度-状态栏内的高度）
    this.navBarHeight = top
  },
  onLoad() {
    let loading = this.$refs.loading;
    loading.handlePopupOpen()
    this.getFetchDataRandomly();
    if (getHistory()) {
      this.searchHistory = getHistory();
    }
    setTimeout(() => {
      loading.handlePopupClose()
    }, 500)
  },
  methods: {
    /**
     * 清除全部记录
     */
    clearHistory: function () {
      removeHistory()
      this.searchHistory = []
    },
    /**
     * 清除全部记录
     */
    clearDesignateHistory: function (index) {
      this.searchHistory.splice(index, 1)
      setHistory(this.searchHistory)
    },
    /**
     * 归位
     */
    keyWordChange: function (e) {
      if (this.keyWords.length === 0) {
        this.steps = 0
      }
    },
    /**
     * 搜索
     */
    search: async function () {
      if (this.keyWords.trim().length > 0) {
        let loading = this.$refs.loading;
        try {
          loading.handlePopupOpen()
          let newVar = await searchArticle({
            keyWord: this.keyWords
          });
          if (newVar) {
            this.blogData = newVar
          }
        } catch (e) {
          console.log(e)
          uni.showToast({
            icon: 'none',
            duration: 6000,
            title: '搜索数据失败'
          });
        } finally {
          loading.handlePopupClose()
        }
        this.steps = 1
        this.addSearchHistory(this.keyWords)
      }
    },
    /**
     * 添加搜索记录
     * @param keyword
     */
    addSearchHistory: function (keyword) {
      // 判断是否已存在该搜索记录
      const index = this.searchHistory.indexOf(keyword)
      if (index !== -1
      ) {
        // 如果已存在，则将该记录移动到最前面
        this.searchHistory.splice(index, 1)
      }
      // 将新的搜索记录添加到数组的最前面
      this.searchHistory.unshift(keyword)
      // 如果搜索记录数量超过5条，则删除最后一条记录
      if (this.searchHistory.length > 5) {
        this.searchHistory.pop()
      }
      setHistory(this.searchHistory)
    },
    /**
     * 随机读文章
     * @returns {Promise<void>}
     */
    getFetchDataRandomly: async function () {
      try {
        let promise = await fetchDataRandomly();
        if (promise) {
          this.data = promise
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
page {
  --tabs-default-color: rgb(255, 255, 255);
  --tabs-nav-background-color: rgb(16, 16, 16);
  --tabs-bottom-bar-color: rgb(238, 179, 118);
}

.search-container {
  animation: fadeIn 1s ease-in-out forwards;
}

.search {
  padding: 20rpx;

  display: flex;
  position: fixed;
  width: 100%;
  z-index: 2;
}

input {
  background-color: rgb(52, 52, 52);
  font-size: 26rpx;
  padding-left: 10rpx;
  width: 450rpx


}

.content-container {
  padding-top: 13vh
}

.code {
  padding-right: 20rpx;
}

.search-layout {
  background-color: rgb(52, 52, 52);
  padding: 10rpx 15rpx;
  width: 500rpx;
  border-radius: 20rpx;
}

.search-former {
  display: flex;
  align-items: center;
}

.btn-search {
  background-color: rgb(52, 52, 52);
  color: white;
  font-size: 23rpx;
  border-radius: 30rpx;
  height: 50rpx;
  width: 100rpx;
  display: flex;
  justify-content: center;
  align-items: center
}

input {
  color: #7e7e7e;
}
</style>
