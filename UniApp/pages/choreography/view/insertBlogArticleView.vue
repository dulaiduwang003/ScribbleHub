<template>
  <view>
    <view class="editor_toolbox" v-show="toolShow">
      <span class="iconfont" data-method="undo" @tap="edit">&#xec0a;</span>
      <span class="iconfont" data-method="redo" @tap="edit">&#xec09;</span>
      <span class="iconfont" data-method="insertImg" @tap="edit">&#xec80;</span>
      <span class="iconfont" data-method="insertVideo" @tap="edit">&#xeca5;</span>
      <span class="iconfont" data-method="insertLink" @tap="edit">&#xec7f;</span>
      <span class="iconfont" data-method="insertText" @tap="edit">&#xeb99;</span>
      <span class="iconfont" @tap="this.isModelShow=true">&#xec8b;</span>
      <span class="iconfont" @tap="clear">&#xec7b;</span>
      <span class="iconfont" @tap="save()">&#xec76;</span>
    </view>
    <view class="mp_container">
      <mp-html ref="article" container-style="padding:20px" :markdown="true" :content="form.content"
               :editable="editable" @remove="remove"/>
    </view>
    <uni-popup ref="popup" type="bottom">
      <view class="submit-container">
        <view class="form-container">
          <view class="form-title">
            文章标题
          </view>
          <view class="padding_left_5">
            <van-field
                maxlength="20"
                placeholder="请输入文章标题"
                border="true"
                @change="onChangeTitle"
                :error-message="titleErrMsg"
            />
          </view>
        </view>
        <view class="form-container">
          <view class="form-title">
            文章标签
          </view>
          <view class="padding_left_5">
            <van-field
                maxlength="100"
                placeholder="请输入文章标签(英文逗号隔开)"
                border="true"
                @change="onChangeLabel"
                :error-message="labelErrMsg"
            />
          </view>
        </view>
        <view class="form-container">
          <view class="form-title">
            专题类型
          </view>

          <view class="upload_spacing">
            <picker :range="classifyType" range-key="classifyName" @change="onChangeClassifyType($event)">
              <view>{{ classifyType[index].classifyName }}</view>
            </picker>
          </view>
        </view>
        <view class="form-container">
          <view class="form-title">
            文章封面
          </view>
          <view class="upload_spacing">
            <van-uploader
                :deletable="false"
                :file-list="fileList"
                max-count="1"
                @after-read="imageCacheCallback"
                upload-text="选择图片"
            />
          </view>
        </view>

        <view class="save_btn">
          <van-button round type="default" size="large" color="#7232dd" @click="submit">保存文章</van-button>
        </view>
      </view>
    </uni-popup>
    <modal v-if="isModelShow" title="请输入CODE类型" confirm-text="确定" cancel-text="取消"
           @cancel="isModelShow=false"
           @confirm="code">
      <input placeholder="language" v-model="language" maxlength="50"/>
    </modal>
  </view>
</template>

<script>
// 上传图片方法
import {getToken} from "@/utils/utils";
import env from "@/utils/env";
import mpHtml from '@/wxcomponents/mp-html/mp-html.vue'
import {deleteBlogFile, getClassifyInfo} from "@/api/admin";


/**
 * 标签插入事件
 * @param src
 * @param type
 * @returns {Promise<unknown>}
 */
function upload(src, type) {

  return new Promise((resolve, reject) => {
    const baseUrl =env.baseUrl;
    wx.uploadFile({
      url: baseUrl + '/admin/blog/upload/resource',
      filePath: src,
      name: 'file',
      header: {
        'token': getToken()
      },
      success(res) {
        let data = JSON.parse(res.data);
        const {seaResourceId, uri} = data.data
        //追加
        seaResourceIdList.push(seaResourceId)
        resolve(env.baseUrl + uri)
      }, fail(res) {
		console.log(res)
        uni.showToast({
          title: '上传失败,请重试',
          icon: 'none',
          duration: 4000
        })
        uni.hideLoading()
      }
    })
  })
}

//博客图片集
let seaResourceIdList = []
//是否完成
let isSucceed = false

// 删除图片方法
function remove(src) {
  // 删除线上资源
  let indexOf = src.indexOf("/upload");
  try {
    deleteBlogFile({
      uri: src.substring(indexOf),
      seaResourceIdList: seaResourceIdList,
      isUrl: true
    })
    seaResourceIdList = []
  } catch (e) {

    uni.showToast({
      title: '删除资源失败~',
      icon: 'none',
      duration: 4000
    })
  }


  // 实际使用时，删除线上资源
}

export default {
  data() {
    return {
      isModelShow: false,
      toolShow: true,
      content: '',
      modal: null,
      editable: true,
      index: 0,
      classifyType: [],
      //文件列表
      fileList: [],
      //表单
      form: {
        //封面
        file: '',
        title: '',
        label: '',
        isRecommend: 0,
        seaClassifyId: undefined,
        content: '',
      },
      titleErrMsg: '',
      labelErrMsg: '',
      language: 'java'
    }
  },
  components: {
    mpHtml,

  },
  created() {
    this.initClassifyInfo()
  },
  onUnload() {
    if (!isSucceed) {
      if (seaResourceIdList.length > 0) {
        try {
          deleteBlogFile({
            seaResourceIdList: seaResourceIdList,
            isUrl: false
          })
        } catch (e) {
          console.log(e)
          uni.showToast({
            title: '删除资源失败~',
            icon: 'none',
            duration: 4000
          })
        }
      }
    }
    isSucceed=false
    seaResourceIdList=[]
  },
  onReady() {
    /**
     * @description 获取链接的方法
     * @param {String} type 链接的类型（img/video/audio/link）
     * @param {String} value 旧值
     * @returns {Promise} 返回线上地址
     *   type 源地址
     */
    this.$refs.article.getSrc = (type, value) => {
      return new Promise((resolve, reject) => {
        if (type === 'img' || type === 'video') {
          uni.showActionSheet({
            itemList: ['本地选取', '远程链接'],
            success: res => {
              if (res.tapIndex === 0) {
                // 本地选取
                if (type === 'img') {
                  uni.chooseImage({
                    count: value === undefined ? 9 :
                        1, // 2.2.0 版本起插入图片时支持多张（修改图片链接时仅限一张）
                    success: res => {
                      // #ifdef MP-WEIXIN
                      if (res.tempFilePaths.length === 1 && wx.editImage) {
                        // 单张图片时进行编辑
                        wx.editImage({
                          src: res.tempFilePaths[
                              0],
                          complete: res2 => {
                            uni.showLoading({
                              title: '上传中'
                            })
                            upload(res2.tempFilePath || res.tempFilePaths[0], type)
                                .then(res => {
                                  uni.hideLoading()
                                  resolve(res)
                                })
                          }
                        })
                      } else {
                        // #endif
                        uni.showLoading({
                          title: '上传中'
                        });
                        (async () => {
                          const arr = []
                          for (let item of res.tempFilePaths) {
                            // 依次上传
                            const src = await upload(item, type)
                            arr.push(src)
                          }
                          return arr
                        })().then(res => {
                          uni.hideLoading()
                          resolve(res)
                        })
                        // #ifdef MP-WEIXIN
                      }
                      // #endif
                    },
                    fail: reject
                  })
                } else {
                  uni.chooseVideo({
                    success: res => {
                      uni.showLoading({
                        title: '上传中'
                      })
                      upload(res.tempFilePath, type).then(
                          res => {
                            uni.hideLoading()
                            resolve(res)
                          })
                    },
                    fail: reject
                  })
                }
              } else {
                // 远程链接
                this.callback = {
                  resolve,
                  reject
                }
                this.$set(this, 'modal', {
                  title: (type === 'img' ? '图片' : '视频') + '链接',
                  value
                })
              }
            }
          })
        } else {
          this.callback = {
            resolve,
            reject
          }
          let title
          if (type === 'audio') {
            title = '音频链接'
          } else if (type === 'link') {
            title = '链接地址'
          }
          this.$set(this, 'modal', {
            title,
            value
          })
        }
      })
    }
  },
  methods: {
    /**
     * 绑定
     * @param e
     */
    onChangeTitle: function (e) {
      this.form.title = e.detail
      this.titleErrMsg = ''
    },
    onChangeLabel: function (e) {
      this.form.label = e.detail
      this.labelErrMsg = ''
    },
    //获取专栏信息
    initClassifyInfo: async function () {
      try {
        const res = await getClassifyInfo();
        if (res) {
          this.classifyType = res
        }
      } catch (e) {
        console.log(e)
        uni.showToast({
          title: '获取专栏信息失败，请返回界面重试',
          icon: 'none',
          duration: 2000
        })
      }

    },
    onChangeClassifyType: function (e) {
      this.index = e.detail.value
    },
    // 删除图片/视频/音频标签事件
    remove(e) {
      // 删除线上资源
      remove(e.src)
    },
    //配置文件图片
    imageCacheCallback: function (e) {
      const {file} = e.detail;
      this.fileList.push({...file, url: file.url});
      this.form.file = file
    },
    // 调用编辑器接口
    edit(e) {
      this.$refs.article[e.currentTarget.dataset.method]()
    },
    // 清空编辑器内容
    clear() {
      uni.showModal({
        title: '确认',
        content: '确定清空内容吗？',
        success: res => {
          if (res.confirm)
            this.$refs.article.clear()
        }
      })
    },
    save() {
      this.$refs.popup.open('bottom')
    },
    code() {
      if (!this.language.trim()) {
        uni.showToast({
          title: 'CODE类型不能为空',
          icon: 'none',
          duration: 2000
        })
        return
      }
      const language = this.language
      this.$refs.article.setContent(`<pre><code class="language-${language}">请输入代码</code></pre>`, true);
      this.isModelShow = false
    },
    submit() {
      try {
        this.form.seaClassifyId = this.classifyType[this.index].seaClassifyId
      } catch (e) {
        uni.showToast({
          title: '请先添加专题',
          icon: 'none',
          duration: 2000
        })
        return
      }
      this.form.content = this.$refs.article.getContent()
      let summary = this.$refs.article.getText();
      let {file, title, isRecommend, seaClassifyId, content, label} = this.form;
      if (!title.trim()) {
        this.titleErrMsg = '文章标题不能为空'
        return
      }
      if (!label.trim()) {
        this.labelErrMsg = '文章标签不能为空'
        return
      }
      if (!file) {
        uni.showToast({
          title: '文章封面不能为空',
          icon: 'none',
          duration: 2000
        })
        return
      }
      if (!content) {
        uni.showToast({
          title: '文章内容不能为空',
          icon: 'none',
          duration: 2000
        })
        return
      }
      uni.showLoading({
        title: '正在保存文章 ing~',
        mask: true
      });
      const baseUrl =env.baseUrl;
      const _this = this
      let s = ""
      if (seaResourceIdList.length > 0) {
        s = seaResourceIdList.join(',');
      }
      wx.uploadFile({
        url: baseUrl + '/admin/blog/insert/article',
        filePath: file.url,
        name: 'file',
        header: {
          'token': getToken()
        },
        formData: {
          'title': title,
          'content': content,
          'seaClassifyId': seaClassifyId,
          'isRecommend': isRecommend,
          'label': label,
          'fileResourceIds': s,
          'summary': summary
        },
        success() {
          uni.hideLoading()
          _this.$refs.popup.close()
          uni.showToast({
            icon: 'none',
            duration: 3000,
            title: `发布成功 转为预览模式`
          });
          _this.editable = false
          _this.toolShow = false
          isSucceed = true

        },
        fail(res) {
          console.log(res)
          uni.hideLoading()
          uni.showToast({
            title: '发布文章失败,请重试',
            icon: 'none',
            duration: 4000
          })
        }
      })

    }
  }
}
</script>

<style>
.van-field__label {
  font-size: 27rpx; /* 更改为所需的字体大小 */
}

.editor_toolbox {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 999;
  background-color: #EDEDED;
  display: flex;
  padding: 5px;
  box-sizing: border-box;
  line-height: 1.6;
}

@font-face {
  font-family: 'iconfont';
  src: url('/static/font/iconfont.woff2?t=1674698988086') format('woff2'),
  url('/static/font/iconfont.woff?t=1674698988086') format('woff'),
  url('/static/font/iconfont.ttf?t=1674698988086') format('truetype');
}

.form-container {
  margin-top: 30rpx;
  display: flex;
  align-items: center;
  color: #525252;
  font-size: 25rpx;
  padding-bottom: 60rpx;
  padding-left: 30rpx;
}

.blog-title {
  padding: 30rpx;
  margin-top: 40rpx
}

.label-title {
  padding: 30rpx;
  margin-top: 10rpx
}

.form-title {
  padding-left: 30rpx;
  width: 170rpx
}

.submit-container {
  position: fixed;
  bottom: 0;
  border-top-left-radius: 60rpx;
  border-top-right-radius: 60rpx;
  background-color: white;
  height: 80vh;
  width: 750rpx;
}

.iconfont {
  flex: 1;
  text-align: center;
  font-family: "iconfont" !important;
  font-size: 22px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}


.icon-undo:before {
  content: "&#xeb99";
}

.icon-redo:before {
  content: "\e606";
}

.icon-img:before {
  content: "\e6e2";
}

.icon-video:before {
  content: "\e798";
}

.icon-code:before {
  content: "\e637";
}

.icon-link:before {
  content: "\e60d";
}

.icon-text:before {
  content: "\e6ce";
}

.icon-clear:before {
  content: "\e637";
}

.icon-save:before {
  content: "\e501";
}

.avatar-wrapper {
  background-color: rgb(255, 255, 255);

}

/* 模态框 */
.modal {
  position: fixed;
  top: 50%;
  left: 16px;
  right: 16px;
  background-color: #fff;
  border-radius: 12px;
  transform: translateY(-50%);
}

.modal_title {
  padding: 32px 24px 16px;
  font-size: 17px;
  font-weight: 700;
  text-align: center;
}

.modal_input {
  display: block;
  padding: 5px;
  margin: 0 24px 32px 24px;
  font-size: 14px;
  border: 1px solid #dfe2e5;
}

.modal_foot {
  display: flex;
  line-height: 56px;
  font-weight: 700;
  border-top: 1px solid rgba(0, 0, 0, .1);
}

.modal_button {
  flex: 1;
  text-align: center;
}

.radio-style {
  display: flex;
  align-items: center
}

label {
  transform: scale(0.9);
  margin-right: 10rpx
}

/* 蒙版 */
.mask {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: black;
  opacity: 0.5;
}

.mp_container {
  padding-top: 50px;
}

page {
  background: #ffffff;
}

.upload_spacing {
  padding-left: 55rpx
}

.save_btn {
  padding: 0 40rpx;
  margin-top: 20rpx
}

.padding_left_5 {
  padding-left: 30rpx
}
</style>
