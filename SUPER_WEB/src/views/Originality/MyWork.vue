<template>
    <el-scrollbar height="500px">
        <div class="image-container">
            <div v-for="(item,index) in images" :key="index" style="text-align: left">
                <el-image class="image" :src="item.url" fit="contain"
                          @click="seeDetails(index)"/>
            </div>
        </div>
        <el-dialog v-model="creationDetails" title="创作详情" style="width: 550px" v-if="imageDetails">
            <el-image class="image-detail" :src="imageDetails.url" fit="contain"/>
            <div style="text-align: left;margin-bottom: 30px;font-size: 12px;margin-top: 10px">创作于
                {{ imageDetails.createdTime }}
            </div>
            <div>提示词</div>
            <div style="margin-top: 10px;font-size: 13px;display: flex;color: white">
                <div style="background-color: #7d80ff;padding: 4px 8px;border-radius: 5px;margin-right: 10px"
                     v-for="(item,index) in promptWords"
                     :key="index">
                    {{ item }}
                </div>
            </div>
            <div style="margin-top:10px;text-align: right;">
                <el-button color="#626aef" @click="download(imageDetails.url)">下载作品</el-button>
                <el-button type="danger" @click="deleteImg(imageDetails.id)">删除作品</el-button>
            </div>
        </el-dialog>

    </el-scrollbar>
</template>

<script>

import {ref} from "vue";
import {DeleteCreation} from "../../../api/BSideApi";
import {ElLoading, ElNotification} from "element-plus";

export default {
    name: "PromptList",
    props: {
        images: Array
    },
    emits: ["flushed"],
    setup(props, parent) {
        const imageDetails = ref({})
        const promptWords = ref([])
        const creationDetails = ref(false)

        function seeDetails(id) {
            let image = props.images[id];
            imageDetails.value = image
            if (image.prompt.includes(",")) {
                promptWords.value = image.prompt.split(",");
            } else {
                promptWords.value = image.prompt.split("，");
            }

            creationDetails.value = true
        }

        async function download(imageUrl) {
            const response = await fetch(imageUrl);
            const blob = await response.blob();
            const url = URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = "我的作品.jpg";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        }


        async function deleteImg(id) {

            try {
                ElLoading.service({
                    fullscreen: true,
                    text: '正在删除...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)',
                });
                await DeleteCreation({
                    id: id
                });
                await parent.emit('flushed')
                ElLoading.service().close();
                ElNotification({
                    title: '操作',
                    message: '删除成功',
                    type: 'success',
                })
                creationDetails.value = false
            } catch (e) {
                ElNotification({
                    message: e,
                    type: 'error',
                });
            }

        }

        return {
            promptWords,
            creationDetails,
            seeDetails,
            imageDetails,
            download,
            deleteImg
        }
    }
}
</script>

<style scoped>
.image-container {
    padding-left: 60px;
    display: flex;
    flex-wrap: wrap;

}

.image {
    margin: 20px;
    display: block;
    width: 200px;
    height: 200px;
    border-radius: 10px;
}

.image-detail {

    width: 512px;
    height: 512px;
}

</style>
