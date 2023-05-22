export type PageType<T> = {
  countId: string
  current: number
  maxLimit: number
  optimizeCountSql: boolean
  orders: Array<Order>
  pages: number
  records: Array<T>
  searchCount: boolean
  size: number
  total: number
}
type Order = {
  asc: boolean
  column: string
}
export type IdRequest = {
  id: number | string
}
export type PageRequest = {
  pageSize?: number
  currentPage?: number
}
