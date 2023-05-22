export type InterfaceInfoType = {
  createTime?: string;
  description?: string;
  id?: number;
  isDelete?: number;
  method?: number;
  name?: string;
  requestExample?: string;
  requestHeader?: string;
  requestParams?: string;
  responseHeader?: string;
  status?: number;
  updateTime?: string;
  url?: string;
  userId?: number;
}

export type InterfaceInfoQueryRequest = {
  pageSize?: number;
  currentPage?: number;
  description?: string;
  id?: number;
  method?: number;
  name?: string;
  requestHeader?: string;
  responseHeader?: string;
  status?: number;
  url?: string;
  userId?: number;
}
export type InterfaceInfoAddRequest = {
  description: string
  method: number
  name: string
  requestExample: string
  requestHeader?: string
  requestParams?: string
  responseHeader?: string
  url: string
}

export type InterfaceInfoUpdateRequest ={
  description?: string
  id?: number
  method?: number
  name?: string
  requestExample?: string
  requestHeader?: string
  requestParams?: string
  responseHeader?: string
  status?: number
  url?: string
}
export type InterfaceInfoTestRequest = {
  id:number
  requestExample:string
}