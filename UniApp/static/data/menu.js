"use strict";

export default {
    menu: [
        {
            icon: '📖',
            title: '发布文章',
            path: '/pages/choreography/view/insertBlogArticleView',
            type: 'ADMIN',
            introduce: '写一篇文章'
        },
        {
            icon: '📚',
            title: '管理文章',
            path: '/pages/choreography/view/pageBlogArticleView',
            type: 'ADMIN',
            introduce: '管理文章'
        },
        {
            icon: '🫓',
            title: '添加专题',
            path: '/pages/choreography/view/insertTopicsView',
            type: 'ADMIN',
            introduce: '添加博客专题'
        },
        {
            icon: '🤖',
            title: '参数配置',
            path: '/pages/choreography/view/dispositionNerveView',
            type: 'ADMIN',
            introduce: '我的服务器配置'
        },
        {
            icon: '🦐',
            title: '管理专题',
            path: '/pages/choreography/view/pageBlogClassifyView',
            type: 'ADMIN',
            introduce: '管理我的专题'
        }, {
            icon: '🧁',
            title: '绘图管理',
            path: '/pages/choreography/view/pageDrawingView',
            type: 'ADMIN',
            introduce: '管理绘图内容'
        },
        {
            icon: '⏏️',
            title: '退出登录',
            path: '',
            type: 'USER',
            introduce: '退出小程序'
        },
        {
            icon: '🎉',
            title: '关于',
            path: '/pages/choreography/view/concerningView',
            type: 'USER',
            introduce: '关于小程序'
        }
    ]
}
