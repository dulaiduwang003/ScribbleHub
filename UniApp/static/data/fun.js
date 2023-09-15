"use strict";

export default {
    menu: [
        {
            icon: '/static/assets/super.svg',
            title: 'Super',
            path: '/pages/super/view/gptView',
            introduce: '根据您的需求提供个性化的建议和回答'
        },
        {
            icon: '/static/assets/bing.svg',
            title: 'Bing',
            path: '/pages/super/view/bingView',
            introduce: '根据您的需求回复实时的建议与回答'
        },
        {
            icon: '/static/assets/code.svg',
            title: 'Visualize',
            path: '/pages/super/view/drawingImageView',
            introduce: '抽象艺术到写实插图,图生图满足各种创作需求'
        },
        {
            icon: '/static/assets/drawing.svg',
            title: 'Drawing',
            path: '/pages/super/view/drawingDescriptionView',
            type: 'ADMIN',
            introduce: '让您的艺术世界变得更加丰富多彩'
        }
    ]
}
