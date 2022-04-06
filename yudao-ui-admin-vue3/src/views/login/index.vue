<template>
  <img :src="bg" class="wave" />
  <div class="login-container">
    <div class="img">
      <illustration />
    </div>
    <div class="login-box">
      <div class="login-form">
        <avatar class="avatar" />
        <h2
          v-motion
          :initial="{ opacity: 0, y: 100 }"
          :enter="{ opacity: 1, y: 0, transition: { delay: 100 } }"
        >
          芋道后台管理系统
        </h2>
        <div
          class="input-group tenant focus"
          v-motion
          :initial="{ opacity: 0, y: 100 }"
          :enter="{ opacity: 1, y: 0, transition: { delay: 200 } }"
          v-if="loginData.tenantEnable"
        >
          <div class="icon">
            <IconifyIconOffline icon="fa-home" width="14" height="14" />
          </div>
          <div>
            <h5>租户名</h5>
            <input
              type="text"
              class="input"
              v-model="loginData.loginForm.tenantName"
              @focus="onTentantFocus"
              @blur="onTentantBlur"
            />
          </div>
        </div>
        <div
          class="input-group userName focus"
          v-motion
          :initial="{ opacity: 0, y: 100 }"
          :enter="{ opacity: 1, y: 0, transition: { delay: 300 } }"
        >
          <div class="icon">
            <IconifyIconOffline icon="fa-user" width="14" height="14" />
          </div>
          <div>
            <h5>用户名</h5>
            <input
              type="text"
              class="input"
              v-model="loginData.loginForm.userName"
              @focus="onUserFocus"
              @blur="onUserBlur"
            />
          </div>
        </div>
        <div
          class="input-group password focus"
          v-motion
          :initial="{ opacity: 0, y: 100 }"
          :enter="{ opacity: 1, y: 0, transition: { delay: 400 } }"
        >
          <div class="icon">
            <IconifyIconOffline icon="fa-lock" width="14" height="14" />
          </div>
          <div>
            <h5>密码</h5>
            <input
              type="password"
              class="input"
              v-model="loginData.loginForm.password"
              @focus="onPwdFocus"
              @blur="onPwdBlur"
            />
          </div>
        </div>
        <div
          class="input-group code focus"
          v-motion
          :initial="{ opacity: 0, y: 100 }"
          :enter="{ opacity: 1, y: 0, transition: { delay: 500 } }"
        >
          <div class="icon">
            <IconifyIconOffline
              icon="fa-space-shuttle"
              width="14"
              height="14"
            />
          </div>
          <div>
            <h5>验证码</h5>
            <input
              type="text"
              class="input"
              style="width: 50%"
              maxlength="5"
              v-model="loginData.loginForm.code"
              @focus="onCodeFocus"
              @blur="onCodeBlur"
            />
            <img
              :src="loginData.codeImg"
              @click="getCode"
              class="login-code-img"
            />
          </div>
        </div>
        <button
          class="btn"
          v-motion
          :initial="{ opacity: 0, y: 10 }"
          :enter="{ opacity: 1, y: 0, transition: { delay: 400 } }"
          @click="onLogin"
        >
          登录
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { initRouter } from "/@/router/utils";
import { storageSession } from "/@/utils/storage";
import { addClass, removeClass } from "/@/utils/operate";
import bg from "/@/assets/login/bg.png";
import avatar from "/@/assets/login/avatar.svg?component";
import illustration from "/@/assets/login/illustration.svg?component";
import { getToken } from "/@/utils/auth";
import { getCodeImg, getTenantIdByName } from "/@/api/login";
import { useUserStore } from "/@/store/modules/user";

const router = useRouter();
const loginData = reactive({
  codeImg: "",
  isShowPassword: false,
  captchaEnable: true,
  tenantEnable: true,
  token: "",
  loading: {
    signIn: false
  },
  loginForm: {
    tenantName: "芋道源码",
    userName: "admin",
    password: "admin123",
    code: "",
    uuid: ""
  }
});
const onLogin = async (): Promise<void> => {
  const loginUser = {
    username: loginData.loginForm.userName.trim(),
    password: loginData.loginForm.password,
    code: loginData.loginForm.code,
    uuid: loginData.loginForm.uuid
  };
  await useUserStore().loginByUsername(loginUser);

  initRouter().then(() => {});
  router.push("/");
};
function getCode() {
  const token = getToken();
  if (!token) {
    getTenantIdByName(loginData.loginForm.tenantName).then(response => {
      // 设置租户
      storageSession.setItem("tenantId", response.data);
    });
    getCodeImg().then(response => {
      const res = response.data;
      loginData.captchaEnable = res.enable;
      if (res.enable) {
        loginData.codeImg = "data:image/gif;base64," + response.data.img;
        loginData.loginForm.uuid = response.data.uuid;
      }
    });
  } else {
    initRouter().then(() => {});
    router.push("/");
  }
}
function onTentantFocus() {
  addClass(document.querySelector(".tenant"), "focus");
}
function onTentantBlur() {
  if (
    loginData.loginForm.tenantName.length === 0 ||
    loginData.loginForm.tenantName === ""
  )
    removeClass(document.querySelector(".tenant"), "focus");
}
function onUserFocus() {
  addClass(document.querySelector(".userName"), "focus");
}

function onUserBlur() {
  if (
    loginData.loginForm.userName.length === 0 ||
    loginData.loginForm.userName === ""
  )
    removeClass(document.querySelector(".userName"), "focus");
}

function onPwdFocus() {
  addClass(document.querySelector(".password"), "focus");
}

function onPwdBlur() {
  if (
    loginData.loginForm.password.length === 0 ||
    loginData.loginForm.password === ""
  )
    removeClass(document.querySelector(".password"), "focus");
}
function onCodeFocus() {
  addClass(document.querySelector(".code"), "focus");
}

function onCodeBlur() {
  if (loginData.loginForm.code.length === 0 || loginData.loginForm.code === "")
    removeClass(document.querySelector(".code"), "focus");
}
// 页面加载时
onMounted(() => {
  onTentantBlur();
  onUserBlur();
  onPwdBlur();
  onCodeBlur();
  getCode();
});
</script>

<style scoped>
@import url("/@/style/login.css");
</style>
