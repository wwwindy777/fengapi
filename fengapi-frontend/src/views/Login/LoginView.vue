<template>
  <div class="login-form radius">
    <el-row justify="center">
      <LoginForm v-if="isLogin"></LoginForm>
      <RegisterForm v-if="!isLogin"></RegisterForm>
    </el-row>
    <el-row justify="center">
      <el-button @click="toRegister">{{ isLogin?'去注册':'去登陆'}}</el-button>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import LoginForm from '@/views/Login/components/LoginForm.vue'
import { ref } from "vue";
import RegisterForm from "@/views/Login/components/RegisterForm.vue";
const isLogin = ref(true)
const toRegister = () => {
  isLogin.value = !isLogin.value
}
</script>

<style scoped>
.login-form {
  width: 500px;
  height: 300px;
  position: absolute;
  margin: auto;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 50px;
  background-color: #a1e6e8;
}
.radius {
  border: 1px solid var(--el-border-color);
  border-radius: 10px;
}
</style>