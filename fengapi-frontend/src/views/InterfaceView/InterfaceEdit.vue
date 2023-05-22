<template>
  <el-card>
    <InterfaceFormWrite :interface-data="interfaceData"></InterfaceFormWrite>
  </el-card>
</template>

<script setup lang="ts">
import InterfaceFormWrite from '@/views/InterfaceView/components/InterfaceFormWrite.vue'
import { useRoute } from 'vue-router'
import { onBeforeMount, ref } from 'vue'
import type { InterfaceInfoType } from '@/api/interfaceInfo/types'
import { getInterfaceInfoApi } from '@/api/interfaceInfo'

const route = useRoute()
const interfaceId = Number(route.query.id)
const interfaceData = ref<InterfaceInfoType>({})
onBeforeMount(async () => {
  const res = await getInterfaceInfoApi({ id: interfaceId })
  if (res.data) {
    interfaceData.value = res.data
  }
})
</script>

<style scoped></style>