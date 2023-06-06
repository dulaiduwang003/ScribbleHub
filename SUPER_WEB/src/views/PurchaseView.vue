<template>
    <div class="panel-container">
        <div class="body" v-if="mainPageVisible">
            <div class="article">选择合适的购买计划</div>
            <div class="introduce">竭尽全力为您提供优质的服务</div>
            <ViewState class="state" v-if="load" LoadText="正在加载，请稍后..."/>
            <ViewState class="state" v-else-if="empty" Type="empty" ErrorText="暂无任何创作的数据"/>
            <ViewState class="state" v-else-if="error" @ClickTheButton="init" Type="error"
                       ErrorText="加载错误，请重试"
                       IsShowBottom ButtonText="重新加载"/>
            <div class="wrapper" v-else>
                <el-row :gutter="20">
                    <el-col v-for="(item,index) in productList" :key="index" :xs="12" :sm="8" :md="8">
                        <div class="item" @click="payChoose(item.id,item.frequency)">
                            <div class="wrapper-title">{{ item.frequency }} Ai币</div>
                            <div class="quantity">￥{{ item.price }}</div>
                            <div class="card-introduce">
                                <div class="function-box" v-for="(item2,index2) in introduce" :key="index2">
                                    <el-icon color="#7d80ff" size="20px">
                                        <CircleCheckFilled/>
                                    </el-icon>
                                    <div style="padding-left: 10px">
                                        <div>{{ item2 }}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <el-dialog v-model="payVisible" :title="productFrequency+' Ai币'" width="30%" v-if="productFrequency">
                <div class="pay-title">
                    选择支付方式后点击“跳转至收银台”打开支付页面支付
                    支付后自动秒到账
                </div>
                <div style="text-align: center">
                    <el-radio label="0" v-model="payType">
                        <div class="pay">
                            <img class="alipay-img" alt="支付宝支付"
                                 src="../assets/alipay.svg">
                            <div>支付宝支付</div>
                        </div>
                    </el-radio>
                    <el-radio label="1" v-model="payType">
                        <div class="pay">
                            <img class="wxpay-img" alt="微信支付"
                                 src="../assets/wxpay.svg">
                            <div>微信支付</div>
                        </div>
                    </el-radio>
                </div>
                <template #footer>
             <span>
                  <el-button @click="payVisible=false">不了, 谢谢</el-button>
                  <el-button type="primary" @click="alipayPay()">跳转至收银台</el-button>
              </span>
                </template>
            </el-dialog>
        </div>
        <!--        支付宝支付-->
        <cash-register v-if="!mainPageVisible" :outcome="outcome" :showCover="showCover" :showSucceed="showSucceed"/>
    </div>
</template>
<script>
import {ref, onMounted} from "vue";
import {AlipayPayment, AlipayStatus, GetProducts, GetUserInfo} from "../../api/BSideApi";
import {ElLoading, ElNotification} from "element-plus";
import CashRegister from "@/components/CashRegister.vue";
import {CircleCheckFilled} from "@element-plus/icons-vue";
import {useStore} from "vuex";
import router from "@/router";
import ViewState from "@/components/ViewState.vue";

export default {
    name: "PurchaseView",
    components: {ViewState, CircleCheckFilled, CashRegister},
    methods: {
        router() {
            return router
        }
    },
    setup() {
        let store = useStore()
        const introduce = ref(["多端次数同步", "全功能使用", "全平台客户端", "绘画自动保存"])
        const payVisible = ref(false)
        const productList = ref([])
        const payType = ref("0")
        const productFrequency = ref('')
        const productId = ref({})
        const mainPageVisible = ref(true)
        const showCover = ref(false)
        const showSucceed = ref(false)

        let load = ref(false)
        let empty = ref(false)
        let error = ref(false)
        //生成结果
        const outcome = ref({})
        async function init() {
            try {
                load.value = true;
                const res = await GetProducts()
                if (res.length) {
                    productList.value = res
                } else {
                    empty.value = true
                }
                load.value = false
                error.value = false
            } catch (e) {
                load.value = false
                error.value = true
            }
        }

        async function getUser() {
            try {
                let res = await GetUserInfo()
                store.commit("setUserinfo", res);
            } catch (e) {
                ElNotification({
                    title: '出现错误',
                    message: e,
                    type: 'error',
                })
            }
        }

        async function payChoose(id, frequency) {
            productId.value = id
            productFrequency.value = frequency
            payVisible.value = true
        }

        async function alipayPay() {
            try {
                ElLoading.service({
                    fullscreen: true,
                    text: '正在构建订单...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)',
                });
                //构建支付宝订单
                outcome.value = await AlipayPayment({
                    "id": productId.value
                });
                payVisible.value = false
                mainPageVisible.value = false
                //3秒检查一下订单是否支付成功
                let timerId = setInterval(async function () {
                    let res = await AlipayStatus({
                        orderNo: outcome.value.id
                    });
                    if (res === "PAID") {
                        ElNotification({
                            title: '成功',
                            message: '支付成功，可在我的订单中查看该订单记录',
                            type: 'success',
                        })
                        showSucceed.value = true
                        await getUser()
                        clearInterval(timerId);
                    } else if (res === "IS_CLOSED") {
                        showCover.value = true
                        ElNotification({
                            title: '订单已关闭',
                            message: '长时间未支付，订单已关闭',
                            type: 'error',
                        })
                        clearInterval(timerId);
                    }
                }, 5000);
            } catch (e) {
                ElNotification({
                    title: '错误',
                    message: e,
                    type: 'error',
                })
                mainPageVisible.value = true
            }finally {
                ElLoading.service().close();
            }
        }

        onMounted(() => {
            init()
        });
        return {
            load,
            error,
            empty,
            showSucceed,
            showCover,
            outcome,
            mainPageVisible,
            alipayPay,
            payChoose,
            init,
            introduce,
            productList,
            payVisible,
            payType,
            productFrequency
        }
    }
}
</script>

<style scoped>
.body {

    animation: explainAnimation 0.3s;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    flex-direction: column;
    flex: 1;
    align-items: center;
    padding: 60px 20px 0;
    display: flex;
    overflow: auto;
}

.pay-title {
    text-align: center;
    padding-left: 30px;
    padding-right: 30px;
    font-size: 13px;
    padding-bottom: 30px;
}

.pay {
    display: flex;
    justify-items: center;
    align-items: center
}

.panel-container {
    overflow: auto;
    overflow-y: scroll;
    height: 100%;
}

.alipay-img {
    width: 20px;
    height: 20px;
    padding-left: 5px;
    padding-right: 10px
}

.wxpay-img {
    width: 25px;
    height: 25px;
    padding-left: 5px;
    padding-right: 10px
}

.wrapper-title {
    background-color: #7d80ff;
    border-radius: 10px 10px 0 0;
    color: white;
    padding-top: 20px;
    padding-bottom: 20px;
    font-size: 15px;
    font-weight: 600;
}

@keyframes explainAnimation {
    from {
        transform: scale(0);
    }

    to {
        transform: scale(1);
    }
}


.article {
    text-align: left;
    margin: 0;
    font-size: 26px;
    font-weight: 600;
}

.wrapper {
    padding: 40px 0;
    max-width: 1000px;
    width: 100%;
    box-sizing: border-box;
}

.item {
    text-align: center;
    background-color: white;
    cursor: pointer;
    width: 100%;
    height: 400px;
    border-radius: 8px;
    font-size: 15px;
    color: #303030;
    margin-bottom: 20px;
}

.introduce {
    font-size: 14px;
    margin-top: 12px
}

.card-introduce {
    color: rgb(108, 117, 125);
    margin-top: 50px;
    text-align: left;
    padding-left: 80px;
    font-size: 14px;
}

.function-box {
    align-items: center;
    display: flex;
    padding-bottom: 15px
}


.quantity {
    padding-top: 50px;
    color: rgb(108, 117, 125);
    font-size: 38px;
    font-weight: 500;
}
</style>
