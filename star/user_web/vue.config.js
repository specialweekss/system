const { defineConfig } = require('@vue/cli-service')
// 获取本机电脑IP
const os = require('os');
const {readFileSync} = require("node:fs");


module.exports = defineConfig({

        publicPath: "./",
        assetsDir: "static",
        outputDir: 'dist',
    devServer: {
        port: 8081,
        https: {
            key: readFileSync('../key/key.pem'),

            cert: readFileSync('../key/cert.pem')
        }
    },
  transpileDependencies: true
})