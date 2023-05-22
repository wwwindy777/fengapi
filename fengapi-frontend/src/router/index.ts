import LoginView from '@/views/Login/LoginView.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { useTitle } from '@/hooks/useTitle'
import { useLoginUserStore } from '@/stores/loginUser'
import Layout from '@/layout/Layout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/interfaceInfo/interfaceView',
      component: Layout,
      name: 'Root',
      meta: {
        hidden: true
      },
      children:[]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/apiUser',
      component: Layout,
      name: 'apiUser',
      meta: {
        title: '开发者'
      },
      children: [
        {
          path: 'userView',
          component: () => import('@/views/UserView/ApiUserPage.vue'),
          name: 'userView',
          meta: {
            title: '用户主页'
          }
        },
      ]
    },
    {
      path: '/interfaceInfo',
      component: Layout,
      name: 'interfaceInfo',
      meta: {
        title: '接口管理'
      },
      children: [
        {
          path: 'interfaceView',
          component: () => import('@/views/InterfaceView/InterfacePage.vue'),
          name: 'interfaceView',
          meta: {
            title: '接口信息'
          }
        },
        {
          path: 'interfaceDetail',
          component: () => import('@/views/InterfaceView/InterfaceDetail.vue'),
          name: 'interfaceDetail',
          meta: {
            title: '接口详情'
          }
        },
        {
          path: 'interfaceEdit',
          component: () => import('@/views/InterfaceView/InterfaceEdit.vue'),
          name: 'interfaceEdit',
          meta: {
            title: '更新接口'
          }
        },
        {
          path: 'interfaceAdd',
          component: () => import('@/views/InterfaceView/InterfaceAdd.vue'),
          name: 'interfaceAdd',
          meta: {
            title: '添加接口'
          }
        }
      ]
    },
    {
      path: '/interfaceRoute',
      component: Layout,
      name: 'interfaceRoute',
      meta: {
        title: '接口路由'
      },
      children: [
        {
          path: 'interfaceRouteView',
          component: () => import('@/views/InterfaceRouteView/InterfaceRoutePage.vue'),
          name: 'interfaceRouteView',
          meta: {
            title: '路由信息'
          }
        },
        {
          path: 'interfaceRouteEdit',
          component: () => import('@/views/InterfaceRouteView/InterfaceRouteEdit.vue'),
          name: 'interfaceRouteEdit',
          meta: {
            title: '路由接口'
          }
        },
        {
          path: 'interfaceRouteAdd',
          component: () => import('@/views/InterfaceRouteView/InterfaceRouteAdd.vue'),
          name: 'interfaceRouteAdd',
          meta: {
            title: '添加路由'
          }
        }
      ]
    }
  ]
})
const whiteList = ['/login'] // 不重定向白名单
router.beforeEach(async (to, from, next) => {
  //全局守卫
  const loginUserStore = useLoginUserStore()
  if (!loginUserStore.user && whiteList.indexOf(to.path) == -1) {
    next(`/login?redirect=${to.path}`)
  } else {
    next()
  }
})

router.afterEach((to) => {
  useTitle(to?.meta?.title as string)
})

export default router
