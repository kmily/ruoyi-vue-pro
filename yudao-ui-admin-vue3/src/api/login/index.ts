import { http } from "/@/utils/http";

interface userType extends Promise<any> {
  svg?: string;
  code?: number;
  info?: object;
}

// 获取验证码
export const getCodeImg = (): userType => {
  return http.request("get", "/system/captcha/get-image");
};

// 登录
export const login = (data: object) => {
  return http.request("post", "/system/login", { data });
};

// 使用租户名，获得租户编号
export const getTenantIdByName = (name: string) => {
  return http.request("get", "/system/tenant/get-id-by-name", {
    params: { name: name }
  });
};
