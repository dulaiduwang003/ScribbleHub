import {createRouter, createWebHashHistory} from 'vue-router'
import {cancelArr} from "@/utils/BSideRequest";

const routes = [
    {
        path: '/',
        name: 'Index',
        component: () => import('../views/IndexView.vue'),
        meta: {
            title: '智能对话',
            isHeadNavigation: true,
            keepAlive: true
        }
    },
    {
        path: '/create',
        name: 'Create',
        component: () => import('../views/Create/CreateView.vue'),
        meta: {
            title: '灵感创作',
            isHeadNavigation: true,
            keepAlive: false
        }
    },
    {
        path: "/create_edit",
        name: "CreateEdit",
        component: () => import('../views/Create/CreateEditView.vue'),
        meta: {
            title: '创作编辑',
            isHeadNavigation: false,
            keepAlive: false
        },
    }, {
        path: "/originality",
        name: "Originality",
        component: () => import('../views/Originality/OriginalityView.vue'),
        meta: {
            title: '创意中心',
            isHeadNavigation: true,
            keepAlive: false
        },
    },
    {
        path: "/create_detail",
        name: "CreateDetail",
        component: () => import('../views/Create/CreateDetailView.vue'),
        meta: {
            title: '创作结果',
            isHeadNavigation: false,
            keepAlive: false
        },
    }, {
        path: "/orders",
        name: "Orders",
        component: () => import('../views/OrdersView.vue'),
        meta: {
            title: '消费记录',
            isHeadNavigation: false,
            keepAlive: false
        },
    },
    {
        path: '/painting',
        name: 'Painting',
        component: () => import('../views/PaintingView.vue'),
        meta: {
            title: 'AI绘画',
            isHeadNavigation: true,
            keepAlive: true
        }
    },
    {
        path: '/Purchase',
        name: 'Purchase',
        component: () => import('../views/PurchaseView.vue'),
        meta: {
            title: '购买',
            isHeadNavigation: false,
            keepAlive: false
        }
    },
    {
        path: '/collection',
        name: 'Collection',
        component: () => import('../views/CollectionView.vue'),
        meta: {
            title: '我的收藏',
            isHeadNavigation: false,
            keepAlive: false
        }
    },
    {
        path: "/bing",
        name: "BingView",
        component: () => import('../views/BingView.vue'),
        meta: {
            title: '新必应对话',
            isHeadNavigation: true,
            keepAlive: true,

        }
    }, {
        path: '/admin',
        name: 'Admin',
        component: () => import('@/views/Admin/AdminView.vue'),
        meta: {
            title: '管理控制台',
            isHeadNavigation: false,
            keepAlive: true
        }
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})


// TODO 全局前置守卫
router.beforeEach(async (to) => {
    // TODO 页面切换中断所有请求
    cancelArr.forEach((cancel, index) => {
        cancel()
        cancelArr.splice(index, 1)
    })

    // TODO 设置浏览器Title
    document.title = (to.meta.title ? to.meta.title : '') + '-Ai'
})

export default router
