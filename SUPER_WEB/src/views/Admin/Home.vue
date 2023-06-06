<template>
    <div class="questions">
        <div class="tab-box">
            <div style="display: flex ;background-color: #7d80ff;height: 100px;margin-bottom: 40px;align-items: center;border-radius: 3px;box-shadow: 0 2px 6px #acb5f6;">
                <div style="padding-left: 40px;color: white;">
                    <div style="font-size: 35px;font-weight: 600">￥{{ amount }}</div>
                    <div style="font-size: 15px;margin-top: 5px;padding-left: 5px">总收入</div>
                </div>
            </div>
           <div style="display: flex;">
               <el-table :data="dataTables" style="width: 100%;font-size: 13px" stripe height="300px" :row-style="{height:'80px'}">
                   <el-table-column prop="id" label="订单号" width="300"/>
                   <el-table-column prop="productName" label="商品名称" width="200"/>
                   <el-table-column prop="productPrice" label="价格" width="140"/>
                   <el-table-column prop="createdTime" label="订单创建时间" width="180"/>
                   <el-table-column prop="state" label="支付状态" width="100"/>
                   <el-table-column prop="reasonFailure" label="取消原因" width="150"/>
               </el-table>
           </div>
            <div style="display: flex;justify-content: right;padding-top: 20px">
                <el-pagination layout="prev, pager, next" :total="total" :page-size="5"  @current-change="initData"/>
            </div>
        </div>
    </div>
</template>

<script>
import {ref, onMounted} from "vue";
import store from "@/store";
import {getOrderPage} from "../../../api/BSideApi";


export default {
    name: "ordersView",
    computed: {
        store() {
            return store
        }
    },

    setup() {

        const dataTables = ref([])
        const current = ref([1])
        const total = ref(0)
        const amount = ref(0)

        onMounted(() => {
            initData(current.value)

        })

        async function initData(current) {
            try {
                let res = await getOrderPage(current);

                if (res.records.length) {
                    let consume = 0.00;
                    res.records.forEach(r => {
                        if (r.state === 0) {
                            r.state = "待支付"
                        }
                        if (r.state === 1) {
                            r.state = "已完成"
                        }
                        if (r.state === 2) {
                            r.state = "已取消"
                        }
                        consume = r.totalAmount
                        r.productPrice = r.productPrice + '元'
                    });
                    dataTables.value = res.records
                    current.value = res.current
                    total.value = res.total
                    amount.value = consume

                }

            } catch (e) {
                console.log(e)
            }
        }


        return {
            initData,
            amount,
            total,
            current,
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
