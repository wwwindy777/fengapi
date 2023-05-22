import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import type { UserLoginType, UserType } from '@/api/login/types'
import { loginApi } from '@/api/login'

export const useLoginUserStore = defineStore('loginUser', () => {
  const user = ref<UserType>()
  const storeData = sessionStorage.getItem('api_loginUser')
  user.value = storeData == null ? undefined : JSON.parse(storeData)
  const doLogin = async (userLoginRequest: UserLoginType) => {
    const res = await loginApi(userLoginRequest)
    if (res) {
      user.value = res.data
    }
  }
  watch(user, () => {
    sessionStorage.setItem('api_loginUser', JSON.stringify(user.value))
  })
  return { user, doLogin }
})
