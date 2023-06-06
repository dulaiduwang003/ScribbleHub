<template>
    <div class="u-questions">
        <div class="tab-box">
            <div style="display: flex ;background-color: #7d80ff;height: 100px;margin-bottom: 40px;align-items: center;border-radius: 3px;box-shadow: 0 2px 6px #acb5f6;">
                <div style="padding-left: 40px;color: white;">
                    <div style="font-size: 35px;font-weight: 600">{{ amount }} 人</div>
                    <div style="font-size: 15px;margin-top: 5px;">总用户量</div>
                </div>
            </div>
            <div style="display: flex;">
                <el-table :data="dataTables" style="width: 100%" stripe height="300px" :row-style="{height:'80px'}">
                    <el-table-column prop="email" label="邮箱账户" width="400"/>
                    <el-table-column prop="password" label="密码" width="200"/>
                    <el-table-column prop="frequency" label="次数" width="240"/>
                    <el-table-column prop="createdTime" label="注册时间" width="180"/>
                </el-table>
            </div>
            <div style="display: flex;justify-content: right;padding-top: 20px">
                <el-pagination layout="prev, pager, next" :total="total" :page-size="5" @current-change="initData"/>
            </div>
        </div>
    </div>
</template>

<script>
import {onMounted, ref} from "vue";
import store from "@/store";
import {getUserPage} from "../../../api/BSideApi";


export default {
    name: "UserPageView",
    computed: {
        store() {
            return store
        }
    },

    setup() {

        const dataTables = ref([])
        const current = ref(0)
        const total = ref(0)
        const amount = ref(0)

        onMounted(() => {
            initData(current.value)
        })

        async function initData(pageNum) {
            try {
                let res = await getUserPage(pageNum);
                if (res.records.length) {
                    dataTables.value = res.records
                    current.value = res.current
                    total.value = res.total
                    amount.value = dataTables.value[0].number
                }

            } catch (e) {

            }
        }


        return {
            initData,
            current,
            amount,
            total,
            dataTables
        };
    }

}
</script>

<style scoped>
.u-questions {
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
