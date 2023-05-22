module.exports = {
     openApi: [
        {
            requestLibPath: "import request from '@/config/axios'", // 想怎么引入封装请求方法
            schemaPath: 'http://localhost:8080/v3/api-docs', // openAPI规范地址
            projectName: 'fengapi', // 生成到哪个目录内
            serversPath: './src/openapi', // 生成代码到哪个目录
        },
    ],
}
