export type GatewayRoute = {
  id: string
  uri: string
  filters: Array<RouteAttribute>
  predicates: Array<RouteAttribute>
  order: number
}
export type GatewayRouteAddVO = {
  dbId: number
  id: string
  uri: string
  filters: Array<RouteAttribute>
  predicates: Array<RouteAttribute>
  order: number
}
export type GatewayRouteAddRequest = {
  routeId?: string
  uri?: string
  filters?: Array<RouteAttribute>
  predicates?: Array<RouteAttribute>
  routeOrder?: number
  status?: number
}
export type GatewayRouteUpdateRequest = {
  id?: number
  routeId?: string
  uri?: string
  filters?: Array<RouteAttribute>
  predicates?: Array<RouteAttribute>
  routeOrder?: number
  status?: number
}
export type GatewayRouteQueryRequest = {
  pageSize?: number
  currentPage?: number
  routeId?: string
}
export type RouteAttribute = {
  name: string
  args?: Args
}
export type Args = {
  _genkey_0?: string
  _genkey_1?: string
  _genkey_2?: string
  _genkey_3?: string
  _genkey_4?: string

  [index: string]: any
}
