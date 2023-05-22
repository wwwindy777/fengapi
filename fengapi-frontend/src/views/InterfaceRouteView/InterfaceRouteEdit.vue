<template>
  <el-card>
    <InterfaceRouteWrite :interface-route-data="interfaceRouteData"></InterfaceRouteWrite>
  </el-card>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { onBeforeMount, ref } from 'vue'
import type { GatewayRoute, GatewayRouteAddVO } from '@/api/interfaceRoute/types'
import { getInterfaceRouteApi } from '@/api/interfaceRoute'
import InterfaceRouteWrite from '@/views/InterfaceRouteView/components/InterfaceRouteWrite.vue'

const route = useRoute()
const interfaceRouteId = String(route.query.id)
const interfaceRouteData = ref<GatewayRouteAddVO>()
onBeforeMount(async () => {
  const res = await getInterfaceRouteApi({ id: interfaceRouteId })
  if (res.data) {
    interfaceRouteData.value = res.data
  }
})
</script>

<style scoped></style>