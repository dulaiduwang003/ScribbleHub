<template>
  <!--  @touchmove.stop.prevent="() => {}"-->
  <view class="master-container">
    <!--首页-->
    <view v-for="item of 30" :key="item" class="snowflake"></view>
    <view>
      <swiper :autoplay="false" @change="changeSwiper" :current="swiperIndex" class="master-container">
        <!--首页-->
        <swiper-item>
          <home-component ref="homeRef" :marquee="marquee" :blog-data="blogData" :classify-marquee="classifyMarquee"/>
        </swiper-item>
        <!--登录-->
        <swiper-item v-if="!isLogin">
          <login-component/>
        </swiper-item>
        <!--个人中心-->
        <swiper-item v-else>
          <personal-center-component/>
        </swiper-item>
      </swiper>
      <loading-component ref="loadingRef" :degree="0.6"/>
      <!--底部导航栏-->
      <menu-component/>
    </view>
  </view>
</template>

<script>
import MenuComponent from '@/pages/master/components/menuComponent.vue'
import HomeComponent from "@/pages/master/components/homeComponent.vue";
import LoginComponent from "@/pages/master/components/loginComponent.vue";
import PersonalCenterComponent from "@/pages/master/components/personalCenterComponent.vue";
import {getToken} from "@/utils/utils";
import LoadingComponent from "@/wxcomponents/components/LoadingComponent.vue";
import {blogPagination, classifyMarquee, recommendedBlogList} from "@/api/public";


export default {
  components: {
    LoadingComponent,
    PersonalCenterComponent,
    LoginComponent,
    HomeComponent,
    MenuComponent
  },
  onLoad(option) {
    //通过参数匹配路由
    if (option.currentPage) {
      this.swiperIndex = option.currentPage
    }
    this.init();

  },
  data() {
    return {
      // 0 首页 1 个人追踪新
      swiperIndex: 0,
      // 是否登录？
      isLogin: false,
      //轮播图
      marquee: [],
      //分页数据
      blogData: [],
      //当前页
      currentPage: 0,
      //专题轮播
      classifyMarquee: [],
    }
  },
  created() {
    this.handleIsLogin();
  },
  methods: {
    init: async function () {
      //打开遮罩
      this.$refs.loadingRef.handlePopupOpen();
      //加载推荐博客轮播图
      await this.handleMarquee()
      //加载推荐专题轮播图
      await this.handleClassifyMarquee()
      // 文章
      await this.handleBlogPagination()
      //关闭遮罩 延迟1000关闭
      setTimeout(() => {
        this.$refs.loadingRef.handlePopupClose();
      }, 1000)
    },
    /**
     * 推荐文章轮播图
     * @returns {Promise<void>}
     */
    handleMarquee: async function () {
      try {
        let promise = await recommendedBlogList();
        if (promise) {
          this.marquee = promise;
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: e
        });
      }
    },
    /**
     * 推荐专题
     * @returns {Promise<void>}
     */
    handleClassifyMarquee: async function () {
      try {
        let promise = await classifyMarquee();
        if (promise) {
          this.classifyMarquee = promise;
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: '获取专题数据失败'
        });
      }
    },
    /**
     * 滑动追加数据
     * @returns {Promise<void>}
     */
    handleBlogPagination: async function () {
      let homeRef = this.$refs.homeRef;
      if (!homeRef.isLoading) {
        homeRef.isLoading = true
        try {
          this.currentPage = this.currentPage + 1
          let promise = await blogPagination(this.currentPage);
          if (promise.list && promise.list.length > 0) {
            this.blogData = this.blogData.concat(promise.list)
          } else {
            this.currentPage = this.currentPage - 1
          }
          const _this = this
          setTimeout(function () {
            homeRef.isLoading = false
          }, 500);
        } catch (e) {
          console.log(e)
          uni.showToast({
            icon: 'none',
            duration: 6000,
            title: '获取最新数据失败'
          });
        }
      }

    },
    /**
     * 用户是否登录
     */
    handleIsLogin: function () {
      this.isLogin = getToken()
      return this.isLogin
    },
    /**
     * 手动切换
     * @param e
     */
    changeSwiper: function (e) {
      uni.vibrateShort();
      this.swiperIndex = e.detail.current;
    }
  }
}
</script>

<style lang="scss">
.snowflake {
  --size: 1vw;
  width: var(--size);
  height: var(--size);
  background: url('/static/assets/snowflake.png') no-repeat; //雪花图片 也可以画圆
  background-size: 100% 100%;
  position: fixed;
  top: -5vh; //出事高度在屏幕外 效果更真实
  z-index: 5; //背景图层不遮挡上面元素
}

.master-container {
  animation: fadeIn 1s ease-in-out forwards;
}

@keyframes snowfall {
  100% {
    transform: translate3d(var(--end), 100vh, 0);
  }
}

@for $i from 0 through 66 {
  .snowflake:nth-child(#{$i}) {
    //每个雪花的大小
    --size: #{random(6) * 0.5}vw;
    //雪花移动目标点 -70后是负数 这样雪花会向左下方飘落
    --end: #{random(20) - 70}vw;
    //雪花初始位置
    left: #{random(150)}vw;
    //雪花从顶到底移动的动画 动画时间可以调整雪花速度
    animation: snowfall #{5 + random(8)}s linear infinite;
    animation-delay: -#{random(10)}s;
  }
}

page {
  background-color: black;
}

.master-container {
  height: 100vh;
}

</style>
