<template>
  <el-menu
    active-text-color="#ffd04b"
    background-color="#545c64"
    class="el-menu-vertical-demo menu-aside"
    default-active="/interfaceInfo/interfaceView"
    text-color="#fff"
    :router="true"
  >
    <MenuLogo></MenuLogo>
    <el-menu-item index="/apiUser/userView">
      <el-icon>
        <icon-menu />
      </el-icon>
      <span>开发者中心</span>
    </el-menu-item>
    <el-sub-menu index="1">
      <template #title>
        <el-icon>
          <icon-menu />
        </el-icon>
        <span>接口管理</span>
      </template>
      <el-menu-item index="/interfaceInfo/interfaceView">接口信息</el-menu-item>
      <el-menu-item index="/interfaceRoute/interfaceRouteView">接口路由</el-menu-item>
    </el-sub-menu>

    <el-menu-item index="4">
      <el-icon>
        <setting />
      </el-icon>
      <span>个人</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup lang="ts">
import { Menu as IconMenu, Setting } from '@element-plus/icons-vue'
</script>

<style scoped>
.menu-aside {
  height: 100%;
}
</style>