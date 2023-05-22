const { generateService } = require('@umijs/openapi')
const {openApi} = require('./config.cjs')

async function run() {
    for (let index = 0; index < openApi.length; index++) {
        await generateService(openApi[index])
    }
}

run()
