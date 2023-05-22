<template>
  <div style="height: 100%;padding-top: 10px;padding-bottom: 10px">
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> Title </span>
      </template>

    </el-page-header>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
const goBack = () => {
  router.back()
}
</script>

<style scoped>
.head-avatar {
  position: absolute;
  right: 30px;
}
</style>