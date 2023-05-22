<template>
  <el-card>
    <el-form :inline="true" :model="queryInterfaceRouteForm">
      <el-form-item>
        <el-input
          maxlength="10"
          v-model="queryInterfaceRouteForm.routeId"
          placeholder="输入路由名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="interfaceRouteAdd" style="margin-bottom: 10px"
      >添加</el-button
    >
    <el-table :data="interfaceRouteData" style="width: 100%" stripe>
      <el-table-column prop="id" label="RouteId" width="120" />
      <el-table-column prop="uri" label="uri" width="180" />
      <el-table-column label="断言">
        <template #default="{ row }">
          {{ JSON.stringify(row.predicates) }}
        </template>
      </el-table-column>
      <el-table-column label="过滤器">
        <template #default="{ row }">
          {{ JSON.stringify(row.filters) }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="230">
        <template #default="{ row }">
          <el-button size="default" @click="action(row)">编辑</el-button>
          <el-button size="default" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { GatewayRouteQueryRequest, GatewayRoute } from '@/api/interfaceRoute/types'
import { getInterfaceRouteListApi } from '@/api/interfaceRoute'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const interfaceRouteData = ref<GatewayRoute[]>()
onMounted(async () => {
  const res = await getInterfaceRouteListApi({})
  if (res.data) {
    interfaceRouteData.value = res.data.records
  }
})
const queryInterfaceRouteForm = ref<GatewayRouteQueryRequest>({})
const onSubmit = async () => {
  const res = await getInterfaceRouteListApi({
    routeId:
      queryInterfaceRouteForm.value.routeId === ''
        ? undefined
        : queryInterfaceRouteForm.value.routeId
  })
  if (res.data) {
    interfaceRouteData.value = res.data.records
  }
}
const action = (row: GatewayRoute) => {
  router.push(`/interfaceRoute/interfaceRouteEdit?id=${row.id}`)
}
const handleDelete = (row: GatewayRoute) => {
  ElMessage({
    message: '不让删！！！',
    type: 'error'
  })
}
const interfaceRouteAdd = () => {
  router.push('/interfaceRoute/interfaceRouteAdd')
}
</script>

<style scoped></style>