import { watch, ref } from 'vue'
import { isString } from '@/utils/is'
export const useTitle = (newTitle?: string) => {
  const title = ref(
    newTitle ? `API平台 - ${newTitle as string}` : 'API平台'
  )

  watch(
    title,
    (n, o) => {
      if (isString(n) && n !== o && document) {
        document.title = n
      }
    },
    { immediate: true }
  )

  return title
}
