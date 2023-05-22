<template>
  <el-form :model="form" label-width="120px">
    <el-form-item label="用户名">
      <el-input v-model="form.userAccount" placeholder="请输入用户名" clearable/>
    </el-form-item>
    <el-form-item label="密码">
      <el-input v-model="form.userPassword" placeholder="请输入密码" show-password clearable/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">登陆</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginUserStore = useLoginUserStore()
// do not use same name with ref
const form = reactive({
  userAccount: 'mulan',
  userPassword: '12345678'
})

const onSubmit = async () => {
  await loginUserStore.doLogin(form)
  if (loginUserStore.user) {
    await router.replace('/')
  }
}
</script>

<style scoped></style>