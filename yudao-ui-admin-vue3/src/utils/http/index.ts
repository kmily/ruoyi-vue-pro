import Axios, { AxiosInstance, AxiosRequestConfig } from "axios";
import {
  PureHttpError,
  RequestMethods,
  PureHttpResponse,
  PureHttpRequestConfig
} from "./types.d";
import qs from "qs";
import NProgress from "../progress";
import { loadEnv } from "@build/index";
import { storageSession } from "/@/utils/storage";
import { ElMessage, ElMessageBox } from "element-plus";
import { getToken, removeToken } from "/@/utils/auth";

// 加载环境变量 VITE_PROXY_DOMAIN（开发环境）  VITE_PROXY_DOMAIN_REAL（打包后的线上环境）
const { VITE_PROXY_DOMAIN, VITE_PROXY_DOMAIN_REAL } = loadEnv();

// 相关配置请参考：www.axios-js.com/zh-cn/docs/#axios-request-config-1
const defaultConfig: AxiosRequestConfig = {
  baseURL:
    process.env.NODE_ENV === "production"
      ? VITE_PROXY_DOMAIN_REAL
      : VITE_PROXY_DOMAIN + "/admin-api/",
  timeout: 5000,
  headers: {
    Accept: "application/json, text/plain, */*",
    "Content-Type": "application/json",
    "X-Requested-With": "XMLHttpRequest"
  },
  // 数组格式参数序列化
  paramsSerializer: params => qs.stringify(params, { indices: false })
};

const tenantEnable = import.meta.env.VITE_APP_TENANT_ENABLE;
// 是否显示重新登录
export const isRelogin = { show: false };
class PureHttp {
  constructor() {
    this.httpInterceptorsRequest();
    this.httpInterceptorsResponse();
  }
  // 初始化配置对象
  private static initConfig: PureHttpRequestConfig = {};

  // 保存当前Axios实例对象
  private static axiosInstance: AxiosInstance = Axios.create(defaultConfig);

  // 请求拦截
  private httpInterceptorsRequest(): void {
    PureHttp.axiosInstance.interceptors.request.use(
      (config: PureHttpRequestConfig) => {
        const $config = config;
        // 开启进度条动画
        NProgress.start();
        // 优先判断post/get等方法是否传入回掉，否则执行初始化设置等回掉
        if (typeof config.beforeRequestCallback === "function") {
          config.beforeRequestCallback($config);
          return $config;
        }
        if (PureHttp.initConfig.beforeRequestCallback) {
          PureHttp.initConfig.beforeRequestCallback($config);
          return $config;
        }
        const token = getToken();
        if (token) {
          config.headers["Authorization"] = "Bearer " + token;
        }
        if (tenantEnable) {
          const tenantId = storageSession.getItem("tenantId");
          if (tenantId) {
            config.headers["tenant-id"] = tenantId;
          }
        }
        return $config;
      },
      error => {
        return Promise.reject(error);
      }
    );
  }

  // 响应拦截
  private httpInterceptorsResponse(): void {
    const instance = PureHttp.axiosInstance;
    instance.interceptors.response.use(
      (response: PureHttpResponse) => {
        const $config = response.config;
        // 对响应数据做点什么
        const res = response.data;
        // 关闭进度条动画
        NProgress.done();
        // 未设置状态码则默认成功状态
        const code = res.code || 200;
        // 获取错误信息
        const msg = res.msg;
        if (code) {
          // `token` 过期或者账号已在别处登录
          if (code === 401) {
            if (isRelogin.show) {
              isRelogin.show = true;
              storageSession.clear(); // 清除浏览器全部临时缓存
              removeToken();
              ElMessageBox.confirm("你已被登出，请重新登录", "系统提示", {
                confirmButtonText: "重新登录",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(() => {
                  isRelogin.show = false;
                })
                .catch(() => {
                  isRelogin.show = false;
                });
            }
            return Promise.reject(instance.interceptors.response);
          } else if (code === 500) {
            ElMessageBox(msg);
            return Promise.reject(msg);
          } else if (code !== 200) {
            ElMessageBox(msg);
            return Promise.reject(msg);
          } else {
            // 优先判断post/get等方法是否传入回掉，否则执行初始化设置等回掉
            if (typeof $config.beforeResponseCallback === "function") {
              $config.beforeResponseCallback(response);
              return response.data;
            }
            if (PureHttp.initConfig.beforeResponseCallback) {
              PureHttp.initConfig.beforeResponseCallback(response);
              return response.data;
            }
            return response.data;
          }
        }
      },
      (error: PureHttpError) => {
        const $error = error;
        $error.isCancelRequest = Axios.isCancel($error);
        // 关闭进度条动画
        NProgress.done();
        // 所有的响应异常 区分来源为取消请求/非取消请求
        // 对响应错误做点什么
        if ($error.message.indexOf("timeout") != -1) {
          ElMessage.error("网络超时");
        } else if ($error.message == "Network Error") {
          ElMessage.error("网络连接错误");
        } else if ($error.message.includes("timeout")) {
          ElMessage.error("系统接口请求超时");
        } else if ($error.message.includes("Request failed with status code")) {
          ElMessage.error(
            "系统接口" +
              $error.message.substr($error.message.length - 3) +
              "异常"
          );
        }
        return Promise.reject($error);
      }
    );
  }

  // 通用请求工具函数
  public request<T>(
    method: RequestMethods,
    url: string,
    param?: AxiosRequestConfig,
    axiosConfig?: PureHttpRequestConfig
  ): Promise<T> {
    const config = {
      method,
      url,
      ...param,
      ...axiosConfig
    } as PureHttpRequestConfig;

    // 单独处理自定义请求/响应回掉
    return new Promise((resolve, reject) => {
      PureHttp.axiosInstance
        .request(config)
        .then((response: undefined) => {
          resolve(response);
        })
        .catch(error => {
          reject(error);
        });
    });
  }

  // 单独抽离的post工具函数
  public post<T, P>(
    url: string,
    params?: T,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>("post", url, params, config);
  }

  // 单独抽离的get工具函数
  public get<T, P>(
    url: string,
    params?: T,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>("get", url, params, config);
  }
}

export const http = new PureHttp();
