<template>
    <div class="questions">
        <div class="tab-box" >
            <el-tabs v-model="activeName" v-if="store.getters.userinfo.role==='ADMIN'">
                <el-tab-pane label="订单数据" name="first">
                    <Home/>
                </el-tab-pane>
                <el-tab-pane label="服务器配置" name="second">
                    <ServerConfig/>
                </el-tab-pane>
                <el-tab-pane label="运营配置️" name="third">
                    <OperationConfig/>
                </el-tab-pane>
                <el-tab-pane label="用户管理" name="fourth">
                    <UserPage/>
                </el-tab-pane>
                <el-tab-pane label="商品管理" name="fifth">
                    <Product/>
                </el-tab-pane>
<!--                <el-tab-pane label="用户申请" name="sixth">-->
<!--                    <UserApply/>-->
<!--                </el-tab-pane>-->
            </el-tabs>
            <ViewState class="state" v-else Type="error"
                       ErrorText="当前页面不见了" />
        </div>

    </div>
</template>

<script>
import {ref} from "vue";
import store from "@/store";
import Home from "@/views/Admin/Home.vue";
import ServerConfig from "@/views/Admin/ServerConfig.vue";
import OperationConfig from "@/views/Admin/OperationConfig.vue";

import Product from "@/views/Admin/Product.vue";
import UserPage from "@/views/Admin/UserPage.vue";
import ViewState from "@/components/ViewState.vue";
export default {
    name: "PromptList",
    computed: {
        store() {
            return store
        }
    },
    components: {ViewState, OperationConfig, Home,ServerConfig,Product,UserPage},
    setup() {
        const activeName = ref('first')

        return {
            activeName,
        };
    }

}
</script>

<style scoped>
.questions {
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
