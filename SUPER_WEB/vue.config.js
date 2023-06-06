const {defineConfig} = require('@vue/cli-service')

const TerserPlugin = require('terser-webpack-plugin');
module.exports = defineConfig({
    publicPath: process.env.NODE_ENV === "production" ? "./" : "/",
    transpileDependencies: true,
    lintOnSave: false,
    configureWebpack: {
        devtool: false,
        optimization: {
            minimize: true,
            minimizer: [
                new TerserPlugin({
                    extractComments: false, // 是否提取注释
                    terserOptions: {
                        format: {
                            comments: false, // 去除注释
                        },
                        compress: {
                            drop_console: true, // 去除console.log语句
                            warnings: false, // 去除警告信息
                            passes: 10000, // 设置混淆级别
                        },
                    },
                }),
            ],
        },
    },
})
