import { storageSession } from "/@/utils/storage";

const TokenKey = "Admin-Token";

// 获取token
export function getToken() {
  // 此处与TokenKey相同，此写法解决初始化时Cookies中不存在TokenKey报错
  return storageSession.getItem("Admin-Token");
}

// 设置token
// 后端需要将用户信息和token以及过期时间都返回给前端，过期时间主要用于刷新token
export function setToken(token: string) {
  return storageSession.setItem(TokenKey, token);
}

// 删除token
export function removeToken() {
  storageSession.clear();
}
