import request from '@/config/axios'
import type { IdRequest, PageType } from "@/api/common/types";
import type {
  GatewayRoute,
  GatewayRouteAddRequest, GatewayRouteAddVO,
  GatewayRouteQueryRequest,
  GatewayRouteUpdateRequest
} from "@/api/interfaceRoute/types";

export const getInterfaceRouteListApi = (
  params: GatewayRouteQueryRequest
): Promise<IResponse<PageType<GatewayRoute>>> => {
  return request.get({ url: '/gateway/route/searchList', params })
}
export const getInterfaceRouteApi = (params: IdRequest): Promise<IResponse<GatewayRouteAddVO>> => {
  return request.get({ url: '/gateway/route/get', params })
}
export const addInterfaceRouteApi = (data: GatewayRouteAddRequest): Promise<IResponse<number>> => {
  return request.post({ url: '/gateway/route/add', data })
}
export const updateInterfaceRouteApi = (data: GatewayRouteUpdateRequest): Promise<IResponse<boolean>> => {
  return request.post({ url: '/gateway/route/update', data })
}

