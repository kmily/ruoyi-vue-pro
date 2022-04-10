import { http } from "/@/utils/http";

// 查询在线用户列表
export const getSeesionList = (query: any) => {
  return http.request("get", "/system/user-session/page", query);
};
// 强退用户
export const forceLogout = (tokenId: string) => {
  return http.request("delete", "/system/user-session/delete?id=" + tokenId);
};
