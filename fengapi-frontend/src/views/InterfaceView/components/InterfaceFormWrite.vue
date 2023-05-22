<template>
  <el-form :model="form" label-width="120px">
    <el-form-item label="接口名">
      <el-input v-model="form.name" />
    </el-form-item>
    <el-form-item label="请求类型">
      <el-radio-group v-model="form.method">
        <el-radio :label="0">get</el-radio>
        <el-radio :label="1">post</el-radio>
        <el-radio :label="2">get/post</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="url">
      <el-input v-model="form.url" />
    </el-form-item>
    <el-form-item label="描述">
      <el-input
        v-model="form.description"
        maxlength="100"
        placeholder="接口描述"
        show-word-limit
        type="text"
      />
    </el-form-item>
    <el-form-item label="请求头">
      <el-input v-model="form.requestHeader" />
    </el-form-item>
    <el-form-item label="响应头">
      <el-input v-model="form.responseHeader" />
    </el-form-item>
    <el-form-item label="请求参数">
      <el-input v-model="form.requestParams" />
    </el-form-item>
    <el-form-item label="请求示例">
      <el-input
        v-model="form.requestExample"
        :autosize="{ minRows: 2 }"
        type="textarea"
        placeholder="请输入请求示例"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
      <el-button @click="onBack">返回</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { InterfaceInfoAddRequest, InterfaceInfoType } from '@/api/interfaceInfo/types'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addInterfaceInfoApi, updateInterfaceInfoApi } from "@/api/interfaceInfo";

interface Props {
  interfaceData: InterfaceInfoType
}

const props = defineProps<Props>()
const router = useRouter()
const route = useRoute()
const form = ref<InterfaceInfoAddRequest>({
  name: '',
  description: '',
  method: 0,
  requestExample: '{"params":{"param1"="xxx","param2="xxx"},"body":{"data1"="zzz","data2"="zzz"}}',
  url: 'http://'
})
watch(props, () => {
  if (props.interfaceData) {
    const formData = form.value
    formData.name = props.interfaceData.name!
    formData.method = props.interfaceData.method!
    formData.description = props.interfaceData.description ?? ''
    formData.requestExample =
      props.interfaceData.requestExample ??
      '{"params":{"param1"="xxx","param2="xxx"},"body":{"data1"="zzz","data2"="zzz"}}'
    formData.url = props.interfaceData.url!
    formData.requestHeader = props.interfaceData.requestHeader ?? ''
    formData.requestParams = props.interfaceData.requestParams ?? ''
    formData.responseHeader = props.interfaceData.responseHeader ?? ''
  }
})
const onSubmit = async () => {
  const id = Number(route.query.id)
  if (id > 0) {
    const res = await updateInterfaceInfoApi({
      id: id,
      ...form.value
    })
    if (res?.data) {
      ElMessage({
        message: '修改成功',
        type: 'success'
      })
      router.back()
    }
    return
  }
  const res = await addInterfaceInfoApi(form.value)
  if (res?.data > 0) {
    ElMessage({
      message: '添加成功',
      type: 'success'
    })
    router.back()
  }
}
const onBack = () => {
  router.back()
}
</script>

<style scoped></style>