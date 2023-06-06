<template>
    <div class="questions">
        <div style="background-color: white;
    width: 93%;
    height: 90%;
    border-radius: 15px;
    padding: 20px">
            <el-table  style="width: 100%;height: 350px">
                <el-table-column prop="email" label="用户邮箱"  />
                <el-table-column prop="state" label="是否绑定小程序"  />
                <el-table-column prop="city" label="理由"/>
                <el-table-column fixed="right" label="操作" width="120">
                    <template #default>
                        <el-button link type="primary" size="small">编辑</el-button>
                        <el-button link type="primary" size="small">删除</el-button>
                    </template>

                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
import {ref} from "vue";
import store from "@/store";



export default {
    name: "UserApply",
    computed: {
        store() {
            return store
        }
    },

    setup() {

        const dataTables = ref([])

        const total = ref(0)
        const amount = ref(0)


        return {

            amount,
            total,

            dataTables
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
