import request from '@/config/axios'
import type {
  InterfaceInfoAddRequest,
  InterfaceInfoQueryRequest, InterfaceInfoTestRequest,
  InterfaceInfoType,
  InterfaceInfoUpdateRequest
} from "./types";
import type { IdRequest, PageType } from '@/api/common/types'

export const getInterfaceInfoListApi = (
  params: InterfaceInfoQueryRequest
): Promise<IResponse<PageType<InterfaceInfoType>>> => {
  return request.get({ url: '/interfaceInfo/searchList', params })
}
export const getInterfaceInfoApi = (params: IdRequest): Promise<IResponse<InterfaceInfoType>> => {
  return request.get({ url: '/interfaceInfo/get', params })
}
export const addInterfaceInfoApi = (data: InterfaceInfoAddRequest): Promise<IResponse<number>> => {
  return request.post({ url: '/interfaceInfo/add', data })
}
export const updateInterfaceInfoApi = (data: InterfaceInfoUpdateRequest): Promise<IResponse<boolean>> => {
  return request.post({ url: '/interfaceInfo/update', data })
}
export const onlineInterfaceInfoApi = (data: IdRequest): Promise<IResponse<boolean>> => {
  return request.post({ url: '/interfaceInfo/online', data })
}
export const offlineInterfaceInfoApi = (data: IdRequest): Promise<IResponse<boolean>> => {
  return request.post({ url: '/interfaceInfo/offline', data })
}
export const testInterfaceInfoApi = (data: InterfaceInfoTestRequest): Promise<IResponse<boolean>> => {
  return request.post({ url: '/interfaceInfo/test', data })
}