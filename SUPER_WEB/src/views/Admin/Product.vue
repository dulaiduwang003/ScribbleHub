<template>
    <div class="u-questions">
        <div class="tab-box">
            <div style="display: flex ;margin-bottom: 40px;align-items: center;">
                <el-button type="primary" style="background-color: rgb(104,110,254)" @click="dialogVisible=true">
                    新增商品
                </el-button>
            </div>
            <div style="display: flex;">
                <el-table :data="dataTables" style="width: 100%" stripe height="350px" :row-style="{height:'80px'}">
                    <el-table-column prop="name" label="商品名称" width="300"/>
                    <el-table-column prop="price" label="价格" width="200"/>
                    <el-table-column prop="frequency" label="次数" width="220"/>
                    <el-table-column prop="createdTime" label="创建时间" width="270"/>
                    <el-table-column fixed="right" label="操作" width="80">
                        <template #default="scope">
                            <div style="text-align: center">
                                <el-button link type="primary" size="small" @click="delProduct(scope.row.id)">下架
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div style="display: flex;justify-content: right;padding-top: 20px">
                <el-pagination layout="prev, pager, next" :total="total" :page-size="5" @current-change="initData"/>
            </div>
        </div>
        <el-dialog
                v-model="dialogVisible"
                title="新增商品"
                width="30%"
        >
            <div>
                <el-form label-width="130px" label-position="left" :inline="true" v-model="form">
                    <el-form-item label="商品名称">
                        <el-input placeholder="请输入商品名称" v-model="form.name"/>
                    </el-form-item>
                    <el-form-item label="次数">
                        <el-input placeholder="请输入次数" v-model="form.frequency"/>
                    </el-form-item>
                    <el-form-item placeholder="请输入价格" label="价格">
                        <el-input-number :precision="2" :step="0.1" :max="10000000" v-model="form.price"/>
                    </el-form-item>
                </el-form>

            </div>
            <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">点错了</el-button>
        <el-button type="primary" @click="addProduct">
          新增
        </el-button>
      </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import {onMounted, ref} from "vue";
import store from "@/store";
import {CudProduct, getProductPage} from "../../../api/BSideApi";
import {ElMessageBox, ElNotification} from "element-plus";


export default {
    name: "ProductView",
    computed: {
        store() {
            return store
        }
    },

    setup() {
        const form = ref({
            id: undefined,
            name: undefined,
            frequency: undefined,
            price: '',
            type: ''
        })
        const dataTables = ref([])
        const current = ref(0)
        const total = ref(0)
        const amount = ref(0)
        const dialogVisible = ref(false)


        onMounted(() => {
            initData(current.value)
        })

        async function addProduct() {
            try {

                let value = form.value;

                if (value.name === '') {
                    ElNotification({
                        title: '校验',
                        message: '名称格式错误',
                        type: 'warn',
                    })
                    return
                }
                if (value.price === 0.00) {
                    ElNotification({
                        title: '校验',
                        message: '价格格式错误',
                        type: 'warn',
                    })
                    return
                }
                if (value.frequency === 0) {
                    ElNotification({
                        title: '校验',
                        message: '次数格式错误',
                        type: 'warn',
                    })
                    return
                }

                value.type = 'INSERT'
                await CudProduct(value);
                ElNotification({
                    title: '操作成功',
                    message: '新增成功',
                    type: 'success',
                })
                initData(current.value)
                dialogVisible.value = false
            } catch (e) {
                ElNotification({
                    title: '错误',
                    message: e,
                    type: 'error',
                })
            }
        }

        async function delProduct(id) {
           try {
               await ElMessageBox({
                   title: '提示',
                   message: "确定下架？",
                   confirmButtonText: '确定',
                   cancelButtonText: '再想想',
                   showCancelButton: true,
                   type: 'warning',
               });
               let value = form.value;
               value.type = 'DELETED'
               value.id = id
               await CudProduct(value);
               ElNotification({
                   title: '操作成功',
                   message: '下架成功',
                   type: 'success',
               })
               initData(current.value)
               dialogVisible.value = false
           }catch (e) {

           }
        }

        async function initData(pageNum) {
            try {
                let res = await getProductPage(pageNum);
                if (res.records.length) {
                    dataTables.value = res.records
                    current.value = res.current
                    total.value = res.total
                }
            } catch (e) {
                console.log(e)
            }
        }

        return {
            addProduct,
            form,
            dialogVisible,
            amount,
            total,
            dataTables,
            initData,
            delProduct
        };
    }

}
</script>

<style scoped>
.p-questions {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    animation: explainAnimation 0.3s;
}

@keyframes explainAnimation {
    from {
        transform: scale(0);
    }

    to {
        transform: scale(1);
    }
}

.tab-box {
    background-color: white;
    width: 93%;
    height: 90%;
    border-radius: 15px;
    padding: 20px
}


</style>
