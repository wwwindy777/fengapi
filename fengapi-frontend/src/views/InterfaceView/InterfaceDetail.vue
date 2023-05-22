<template>
  <el-card>
    <el-descriptions title="接口详情" :column="3" border>
      <el-descriptions-item
        label="接口名"
        label-align="right"
        align="center"
        label-class-name="my-label"
        class-name="my-content"
        width="100px"
        >{{ interfaceData.name }}
      </el-descriptions-item>
      <el-descriptions-item label="接口地址" label-align="right" align="center"
        >{{ interfaceData.url }}
      </el-descriptions-item>
      <el-descriptions-item label="请求类型" label-align="right" align="center"
        >{{ RequestMethod[interfaceData.method] }}
      </el-descriptions-item>
      <el-descriptions-item label="接口状态" label-align="right" align="center">
        <el-tag size="small">{{ interfaceData.status === 0 ? '关闭' : '开启' }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间" label-align="right" align="center"
        >{{ interfaceData.createTime }}
      </el-descriptions-item>
      <el-descriptions-item label="更新时间" label-align="right" align="center"
        >{{ interfaceData.updateTime }}
      </el-descriptions-item>
      <el-descriptions-item label="请求参数" label-align="right" align="center" span="3"
        >{{ interfaceData.requestParams }}
      </el-descriptions-item>
      <el-descriptions-item label="请求头" label-align="right" align="center" span="3"
        >{{ interfaceData.requestHeader }}
      </el-descriptions-item>
      <el-descriptions-item label="响应头" label-align="right" align="center" span="3"
        >{{ interfaceData.responseHeader }}
      </el-descriptions-item>
      <el-descriptions-item label="请求示例" label-align="right" align="center" span="3"
        >{{ interfaceData.requestExample }}
      </el-descriptions-item>
      <el-descriptions-item label="描述" label-align="right" align="center" span="3"
        >{{ interfaceData.description }}
      </el-descriptions-item>
    </el-descriptions>
    <div style="margin-top: 20px">
      <el-button type="primary" @click="checkInterface">测试调用</el-button>
      <el-button v-if="interfaceData.status === 0" type="success" @click="onlineInterface"
        >上线接口
      </el-button>
      <el-button v-if="interfaceData.status === 1" type="warning" @click="offlineInterface"
        >下线接口
      </el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import type { InterfaceInfoType } from '@/api/interfaceInfo/types'

import { RequestMethod } from '@/api/interfaceInfo/enums'
import { ElMessage } from 'element-plus'
import {
  getInterfaceInfoApi,
  offlineInterfaceInfoApi,
  onlineInterfaceInfoApi,
  testInterfaceInfoApi
} from '@/api/interfaceInfo'

const router = useRouter()
const route = useRoute()
const interfaceId = Number(route.query.id)
const interfaceData = ref<InterfaceInfoType>({})
onMounted(async () => {
  const res = await getInterfaceInfoApi({ id: interfaceId })
  if (res?.data) {
    interfaceData.value = res.data
  }
})

const checkInterface = async () => {
  const res = await testInterfaceInfoApi({
    id: interfaceId,
    requestExample: interfaceData.value.requestExample ?? ''
  })
  if (res) {
    ElMessage({
      message: `调用成功,结果为：${res.data}`,
      type: 'success'
    })
  }
}
const onlineInterface = async () => {
  const res = await onlineInterfaceInfoApi({ id: interfaceId })
  if (res?.data) {
    ElMessage({
      message: '上线成功',
      type: 'success'
    })
    router.back()
  }
}
const offlineInterface = async () => {
  const res = await offlineInterfaceInfoApi({ id: interfaceId })
  if (res?.data) {
    ElMessage({
      message: '下线成功',
      type: 'success'
    })
    router.back()
  }
}
</script>

<style scoped></style>