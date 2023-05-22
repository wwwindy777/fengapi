<template>
  <el-form
    :model="form"
    label-width="100px"
    label-position="left"
    require-asterisk-position="right"
  >
    <el-row :gutter="20">
      <el-col :span="5">
        <el-form-item label="路由Id" required>
          <el-input v-model="form.routeId" />
        </el-form-item>
      </el-col>
      <el-col :span="10">
        <el-form-item label="uri" required>
          <el-input v-model="form.uri" />
        </el-form-item>
      </el-col>
      <el-col :span="5">
        <el-form-item label="order" required>
          <el-input-number v-model="form.routeOrder" :min="0" :max="10" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="10" v-for="(predicate, index) in form.predicates" :key="index">
        <el-form-item :label="'Predicate' + index">
          <el-space :fill="true" size="small">
            <el-input v-model="predicate.name" placeholder="请输入name" />
            <div v-for="(value, key) in predicate.args" :key="key">
              <el-input v-model="predicate.args[key]" :placeholder="key" />
            </div>
            <el-button @click.prevent="removeAttribute(predicate, 'predicate')" type="danger"
              >Delete
            </el-button>
          </el-space>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="10" v-for="(filter, index) in form.filters" :key="index">
        <el-form-item :label="'Filter' + index">
          <el-space :fill="true" size="small">
            <el-input v-model="filter.name" placeholder="请输入name" />
            <div v-for="(value, key) in filter.args" :key="key">
              <el-input v-model="filter.args[key]" :placeholder="key" />
            </div>
            <el-button @click.prevent="removeAttribute(filter, 'filter')" type="danger"
              >Delete
            </el-button>
          </el-space>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item>
      <el-button @click="addAttribute('predicate')">添加断言</el-button>
      <el-button @click="addAttribute('filter')">添加过滤器</el-button>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">保存</el-button>
      <el-button @click="onBack">返回</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type {
  GatewayRouteAddRequest,
  GatewayRouteAddVO,
  RouteAttribute
} from '@/api/interfaceRoute/types'
import { addInterfaceRouteApi, updateInterfaceRouteApi } from '@/api/interfaceRoute'

interface Props {
  interfaceRouteData: GatewayRouteAddVO
}

const props = defineProps<Props>()
const router = useRouter()
const form = ref<GatewayRouteAddRequest>({
  uri: 'http://',
  predicates: [],
  filters: [
    {
      name: 'RequestRateLimiter',
      args: {
        'key-resolver': '#{@ipKeyResolver}',
        'redis-rate-limiter.burstCapacity': '1',
        'redis-rate-limiter.replenishRate': '1',
        'redis-rate-limiter.requestedTokens': '1'
      }
    }
  ],
  routeOrder: 0
})

const unwatch = watch(props, () => {
  if (props.interfaceRouteData) {
    const { ...formData } = props.interfaceRouteData
    form.value.filters = formData.filters ?? []
    form.value.predicates = formData.predicates
    form.value.uri = formData.uri
    form.value.routeId = formData.id
    form.value.routeOrder = formData.order
    unwatch()
  }
})
const sourceArgs = {
  _genkey_0: undefined,
  _genkey_1: undefined,
  _genkey_2: undefined
}
const addAttribute = (item: string) => {
  if (item == 'predicate') {
    form.value.predicates?.push({
      name: '',
      args: { ...sourceArgs }
    })
  } else if (item == 'filter') {
    form.value.filters?.push({
      name: '',
      args: { ...sourceArgs }
    })
  }
}
const removeAttribute = (item: RouteAttribute, itemType: string) => {
  if (itemType == 'predicate') {
    const index = form.value.predicates?.indexOf(item)
    if (index !== -1) {
      form.value.predicates?.splice(index!, 1)
    }
  } else if (itemType == 'filter') {
    const index = form.value.filters?.indexOf(item)
    if (index !== -1) {
      form.value.filters?.splice(index!, 1)
    }
  }
}
const onSubmit = async () => {
  const id = props.interfaceRouteData.dbId
  if (id >= 0) {
    const res = await updateInterfaceRouteApi({
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
  const res = await addInterfaceRouteApi(form.value)
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