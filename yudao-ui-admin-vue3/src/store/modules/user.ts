import { defineStore } from "pinia";
import { store } from "/@/store";
import { userType } from "./types";
import { router } from "/@/router";
import { storageSession } from "/@/utils/storage";
import { login } from "/@/api/login";
import { getToken, setToken, removeToken } from "/@/utils/auth";
import { useMultiTagsStoreHook } from "/@/store/modules/multiTags";

export const useUserStore = defineStore({
  id: "pure-user",
  state: (): userType => ({
    token: getToken(),
    name: ""
  }),
  actions: {
    SET_TOKEN(token) {
      this.token = token;
    },
    SET_NAME(name) {
      this.name = name;
    },
    // 登入
    async loginByUsername(data) {
      return new Promise<void>((resolve, reject) => {
        login(data)
          .then((res: any) => {
            if (res && res?.code === 0) {
              setToken(res?.data?.token);
              // this.token = res.data.token;
              resolve(res);
            } else {
              reject(res?.msg);
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    // 登出 清空缓存
    logOut() {
      this.token = "";
      this.name = "";
      removeToken();
      storageSession.clear();
      useMultiTagsStoreHook().handleTags("equal", [
        {
          path: "/welcome",
          parentPath: "/",
          meta: {
            title: "menus.hshome",
            icon: "home-filled",
            i18n: true
          }
        }
      ]);
      router.push("/login");
    }
  }
});

export function useUserStoreHook() {
  return useUserStore(store);
}
