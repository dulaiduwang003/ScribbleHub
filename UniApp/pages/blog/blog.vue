<template>
  <view class="container">
    <!--加载-->
    <loading-component ref="loading" :degree="1"/>
    <!--数据页-->
    <scroll-view class="main-scroll" @scroll="scrollToView($event)" scroll-y :scroll-into-view="index">
      <!--文章内容-->
      <blog-content-component id="content" :blogData="blogData"/>
      <!--评论区-->
      <comment-component id="comment" ref="commentRef" :commentData="commentData" :isLogin="isLogin"/>
    </scroll-view>
    <!--悬浮-->
    <view class="floating">
      <view class="floating-input" @click="this.$refs.commentRef.handlePublicationOpen">
        <van-icon name="edit" size="50rpx" color="rgb(110,110,110)"/>
        <view class="floating-text">{{ isLogin ? '写评论...' : '请先登录' }}</view>
      </view>
      <view class="comment-btn">
        <van-icon :name="icon?'chat-o':'back-top'" :info="commentData.length" size="60rpx" color="white"
                  @click="handleTopIcon"/>
        <van-icon name="bulb-o" size="60rpx" color="white" @click="handleAlert"/>
        <van-icon :name="isFlower?'good-job':'good-job-o'" size="60rpx" :color="isFlower?'#d52e2e':'white'"
                  @click="handleFlowers"/>
      </view>
    </view>

  </view>
</template>

<script>
import BlogContentComponent from "@/pages/blog/components/blogContentComponent.vue";
import {blogArticle, blogComment} from "@/api/public";
import CommentComponent from "@/pages/blog/components/commentComponent.vue";
import {getFlower, getToken, setFlower} from "@/utils/utils";
import LoadingComponent from "@/wxcomponents/components/LoadingComponent.vue";


let lock = true
export default {
  onUnload(){
    uni.$off("blogGetBlogComment")
  },
  components: {LoadingComponent, CommentComponent, BlogContentComponent},
  data() {
    return {
      seaBlogId: undefined,
      blogData: {},
      index: 'content',
      icon: true,
      commentData: [],
      isLogin: false,
      isFlower: false
    };
  }, onLoad(option) {
    const _this = this
    //从URL中获取博客ID
    _this.seaBlogId = option.seaBlogId
    //花花
    let flower = getFlower() || [];
    setFlower(flower);
    _this.isFlower = flower.includes(_this.seaBlogId.toString());
    //初始化页面数据
    _this.init()
    //监听事件
    uni.$on('blogGetBlogComment', function () {
      _this.getBlogComment()
    })
  }, created() {
    this.isLogin = getToken()
  },
  methods: {
    onUnload() {
      // 页面销毁时执行的代码
      lock = true
    },
    /**
     * 初始化方法
     */
    init() {
      this.getBlogArticle()
      this.getBlogComment()
    },
    /**
     * 内容 -> 评论
     */
    handleTopIcon: function () {
      lock = false
      if (this.icon) {
        this.icon = false
        this.index = 'comment'
      } else {
        this.icon = true
        this.index = 'content'
      }
      setTimeout(() => {
        lock = true
      }, 600)
    },
    /**
     * 获取文章
     * @returns {Promise<void>}
     */
    getBlogArticle: async function () {
      const _this = this
      let loading = _this.$refs.loading;
      try {
        loading.handlePopupOpen()
        const promise = await blogArticle(this.seaBlogId);
        if (promise) {
          const label = promise.label.split(',');
          this.blogData = {...promise, label};
          uni.setNavigationBarTitle({title: promise.title});
        }
        setTimeout(() => {
          loading.handlePopupClose()
        }, 500)
      } catch (e) {
        uni.showToast({
          title: '文章貌似不见了~',
          icon: 'none',
          duration: 4000
        })
        setTimeout(() => {
         uni.navigateBack()
        }, 2000)
      }
    },
    /**
     * 获取博客评论
     */
    getBlogComment: async function () {
      try {
        let promise = await blogComment(this.seaBlogId);
        if (promise) {
          this.commentData = promise
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          title: '获取博客评论失败',
          icon: 'none',
          duration: 4000
        })
      }
    },
    /**
     * 滴滴
     */
    handleAlert: function () {
      uni.showToast({
        title: '滴滴滴滴滴~',
        icon: 'none',
        duration: 4000
      })
    },
    /**
     * 送花
     */
    handleFlowers: function () {
      const showToast = (title) => {
        uni.showToast({
          title: title,
          icon: 'none',
          duration: 4000
        })
      }
      if (this.isFlower) {
        showToast('哎哟 服了你这个老六~')
        let flowerArray = getFlower();
        setFlower(flowerArray.filter(str => !str.includes(this.seaBlogId.toString())))
        this.isFlower = false
      } else {
        showToast('哎哟 不错哟~')
        let flower = getFlower();
        flower.push(this.seaBlogId.toString())
        setFlower(flower)
        this.isFlower = true
      }
    },
    /**
     * 联动
     * @param e
     */
    scrollToView(e) {
      if (lock) {
        // 获取滚动位置
        const _this = this
        const scrollTop = e.detail.scrollTop
        // 获取节点位置
        const query = uni.createSelectorQuery().in(this)
        query.select('#content').boundingClientRect()
        query.select('#comment').boundingClientRect()
        query.exec(res => {
          let node = true
          const top1 = res[0].top
          const top2 = res[1].top
          // 判断是否滚动到了节点1
          if (scrollTop >= top1) {
            node = true
          }
          // 判断是否滚动到了节点2
          if (scrollTop >= top2) {
            node = false
          }
          _this.icon = node
          _this.index = ''
        })
      }
    }


  }
}
</script>

<style lang="scss">

.container {
  animation: fadeIn 1s ease-in-out forwards;
  padding: 20rpx;

}

.main-scroll {
  height: 86vh;

}

.floating {
  padding: 15rpx 40rpx;
  position: fixed;
  bottom: 0;
  left: 0;
  height: 140rpx;
  background-color: rgb(30, 30, 30);
  width: 748rpx;
  display: flex;
  z-index: 99;
}

.floating-input {
  padding: 0 20rpx;
  height: 80rpx;
  width: 300rpx;
  border-radius: 15rpx;
  background-color: rgb(17, 17, 17);
  display: flex;
  align-items: center
}

.floating-text {
  padding-left: 15rpx;
  font-size: 26rpx;
  color: rgb(110, 110, 110)
}

.comment-btn {
  width: 290rpx;
  height: 70rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20rpx;
  padding-left: 50rpx
}

</style>
