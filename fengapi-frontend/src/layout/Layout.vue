<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px">
        <MenuAside class="menu-aside"></MenuAside>
      </el-aside>
      <el-container>
        <el-header>
          <PageHeader></PageHeader>
        </el-header>
        <el-main>
            <RouterView></RouterView>
        </el-main>
        <el-footer>Footer</el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import MenuAside from '@/components/MenuAside.vue'
import MenuLogo from '@/components/MenuLogo.vue'
import PageHeader from '@/components/PageHeader.vue'

export default defineComponent({
  components: { PageHeader, MenuLogo, MenuAside }
})
</script>

<style scoped>
.common-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>