<template>
  <el-form :inline="true" :model="queryInterfaceForm">
    <el-form-item>
      <el-input maxlength="10" v-model="queryInterfaceForm.name" placeholder="输入接口名称" />
    </el-form-item>
    <el-form-item label="请求类型">
      <el-select clearable v-model="queryInterfaceForm.method" placeholder="选择请求方式">
        <el-option label="get" value="0" />
        <el-option label="post" value="1" />
        <el-option label="get/post" value="2" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="interfaceData" style="width: 100%" stripe>
    <el-table-column prop="name" label="Name" width="80" />
    <el-table-column prop="method" label="请求类型" width="80" />
    <el-table-column prop="description" label="简介" />
    <el-table-column prop="updateTime" label="更新时间" />
  </el-table>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { InterfaceInfoQueryRequest, InterfaceInfoType } from '@/api/interfaceInfo/types'
import { useRouter } from 'vue-router'
import { getInterfaceInfoListApi } from '@/api/interfaceInfo'

const router = useRouter()
const interfaceData = ref<InterfaceInfoType[]>()
onMounted(async () => {
  const res = await getInterfaceInfoListApi({})
  if (res.data) {
    interfaceData.value = res.data.records
  }
})
const queryInterfaceForm = ref<InterfaceInfoQueryRequest>({})
const onSubmit = async () => {
  const res = await getInterfaceInfoListApi({
    name: queryInterfaceForm.value.name === '' ? undefined : queryInterfaceForm.value.name,
    method: queryInterfaceForm.value.method
  })
  if (res.data) {
    interfaceData.value = res.data.records
  }
}
</script>

<style scoped></style>