<template>
  <view class="container">
    <view class="title">
      添加专题
    </view>
    <view class="form">

      <view class="form-container">
        <view class="form-title">
          专题名称
        </view>

        <view class="padding_left_10">
          <van-field
              :value="form.classifyName"
              placeholder="请输入专题名称"
              border="true"
              :error-message="classifyNameErrMsg"
              @change="onChangeClassifyName"
              maxlength="20"
          />
        </view>
      </view>
      <view class="form-container">
        <view class="form-title">
          专题类型
        </view>
        <view class="padding_left_30">
          <picker :range="classifyType" range-key="title" @change="onChangeClassifyType($event)">
            <view>{{ classifyType[index].title }}</view>
          </picker>
        </view>
      </view>
      <view class="form-container">
        <view class="form-title">
          专题封面
        </view>

        <view class="padding_left_30">
          <van-uploader
              :file-list="fileList"
              max-count="1"
              @after-read="imageCacheCallback"
              upload-text="选择图片"
              deletable="false"
          />
        </view>
      </view>
      <van-button round type="default" size="large" color="#7232dd" @click="handleSubmit"> 添加</van-button>
    </view>
  </view>
</template>

<script>
import {getToken} from "@/utils/utils";
import env from "@/utils/env";

export default {
  data() {
    return {
      //文件列表
      fileList: [],
      //表单
      form: {
        classifyName: '',
        file: undefined,
        isType: 0
      },
      //类型
      classifyType: [
        {
          title: '前端',
          value: 0,
        },
        {
          title: '后端',
          value: 1,
        }, {
          title: '中间件',
          value: 2,
        }, {
          title: '其他',
          value: 3,
        }
      ],
      //类型坐标
      index: 0,
      classifyNameErrMsg: '',
    };
  }, methods: {

    // 提交表单
    handleSubmit: function () {
      const {classifyName, isType, file} = this.form;
      if (!classifyName.trim()) {
        this.classifyNameErrMsg = '专题不能为空'
        return
      }

      if (!file) {
        uni.showToast({
          title: '专栏封面不能为空',
          icon: 'none',
          duration: 2000
        })
        return
      }
      uni.showLoading({
        title: '正在保存专栏 ing~',
        mask: true
      });
      const baseUrl = env.baseUrl;
      wx.uploadFile({
        url: baseUrl + '/admin/blog/insert/classify',
        filePath: file.url,
        name: 'file',
        header: {
          'token': getToken()
        },
        formData: {
          'classifyName': classifyName,
          'isType': isType
        },
        success() {
          uni.hideLoading()
          uni.showToast({
            title: '上传专栏成功',
            icon: 'none',
            duration: 500
          })
          setTimeout(function() {
            uni.navigateBack({
              delta: 1 // 返回的页面数
            })
          }, 500);
        },
        fail(res) {
          console.log(res)
          uni.hideLoading()
          uni.showToast({
            title: '操作失败,请重试',
            icon: 'none',
            duration: 2000
          })
        }
      })
    },
    //配置文件图片
    imageCacheCallback: function (e) {
      const {file} = e.detail;
      this.fileList.push({...file, url: file.url});
      this.form.file = file
    },
    /**
     * 选择类型
     * @param e
     */
    onChangeClassifyType: function (e) {
      this.index = e.detail.value
      this.form.isType = this.classifyType[this.index].value
    },
    /**
     * 绑定
     * @param e
     */
    onChangeClassifyName: function (e) {
      this.form.classifyName = e.detail
      this.classifyNameErrMsg = ''
    }

  }
}
</script>

<style>
page {
  background-color: white;
}
.van-field__label {
  font-size: 27rpx; /* 更改为所需的字体大小 */
}

.padding_left_10{
  padding-left: 10rpx
}
.padding_left_30{
  padding-left: 40rpx
}
.container {
  color: black;
  padding: 40rpx
}

.title {
  font-size: 50rpx;
  font-weight: 550;
}

.form-container {
  margin-top: 30rpx;
  display: flex;
  align-items: center;
  color: #525252;
  font-size: 27rpx;
  padding-bottom: 30rpx
}

.form-title {
  padding-left: 30rpx;
  width: 170rpx
}

.form {
  margin-top: 50rpx
}
</style>
