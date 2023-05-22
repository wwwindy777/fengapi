export type UserLoginType = {
  userAccount: string
  userPassword: string
}

export type UserType = {
  accessKey?: string;
  createTime?: string;
  gender: number;
  id: number;
  isDelete?: number;
  secretKey?: string;
  updateTime?: string;
  userAccount: string;
  userAvatar?: string;
  userName?: string;
  userPassword?: string;
  userRole: string;
}