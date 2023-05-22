<template>
  <el-card>
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
    <el-button type="primary" @click="interfaceAdd" style="margin-bottom: 10px">添加</el-button>
    <el-table :data="interfaceData" style="width: 100%" stripe>
      <el-table-column prop="name" label="Name" width="80" />
      <el-table-column prop="method" label="请求类型" width="80" />
      <el-table-column prop="userId" label="创建人id" width="80" />
      <el-table-column label="接口状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'danger' : 'success'" disable-transitions
            >{{ scope.row.status === 0 ? '关闭' : '开启' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="接口地址" />
      <el-table-column prop="description" label="简介" />
      <el-table-column prop="updateTime" label="更新时间" />
      <el-table-column fixed="right" label="操作" width="230">
        <template #default="{ row }">
          <el-button size="default" @click="action(row, 'Detail')">详情</el-button>
          <el-button size="default" @click="action(row, 'Edit')">编辑</el-button>
          <el-button size="default" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { InterfaceInfoQueryRequest, InterfaceInfoType } from '@/api/interfaceInfo/types'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
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
const action = (row: InterfaceInfoType, type: string) => {
  router.push(`/interfaceInfo/interface${type}?id=${row.id}`)
}
const handleDelete = (row: InterfaceInfoType) => {
  ElMessage({
    message: '不让删！！！',
    type: 'error'
  })
}
const interfaceAdd = () => {
  router.push('/interfaceInfo/interfaceAdd')
}
</script>

<style scoped></style>