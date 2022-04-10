import { http } from "/@/utils/http";

interface postType extends Promise<any> {
  data?: object;
  code?: number;
  msg?: string;
}

// 获取岗位管理列表
export const getPostList = (data?: object): postType => {
  return http.request("get", "/system/post/page", { data });
};
