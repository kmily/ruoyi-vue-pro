<template xmlns="">
  <div class="container clean-bg">
      <div class="content register-content">
        <div class="register-top">
          <div class="title">注册账号</div>
          <div>
            <el-button  size="mini" type="text"  @click="gotoLoginHandle">已有账号,立即登录</el-button>
          </div>
        </div>
        <div class="field register-field">
          <el-form  ref="loginForm" :model="loginForm" :rules="LoginRules" class="login-form">
              <!-- 短信验证码登录 -->
            <el-form-item prop="mobile">
              <el-input v-model="loginForm.mobile" type="text" auto-complete="off" placeholder="请输入手机号">
                <svg-icon slot="prefix" icon-class="phone" class="el-input__icon input-icon"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="mobileCode" class="mobile-code" style="display:flex;">
              <el-input v-model="loginForm.mobileCode" type="text" auto-complete="off" placeholder="短信验证码"
                @keyup.enter.native="handleLogin" >
                <!-- <svg-icon slot="prefix" icon-class="message" class="el-input__icon input-icon"/> -->
                <i class="el-icon-message el-input__icon" slot="prefix"></i>
                <!-- <span slot="append" v-if="mobileCodeTimer <= 0" class="getMobileCode"
                  @click="getSmsCode" style="cursor: pointer" >获取验证码</span>
                <span slot="append" v-if="mobileCodeTimer > 0" class="getMobileCode" >{{ mobileCodeTimer }}秒后可重新获取</span> -->
              </el-input>
              <el-button  size="mini" v-if="mobileCodeTimer <= 0" class="getMobileCode"
                  @click="getSmsCode" type="primary">获取验证码</el-button>
              <el-button  size="mini" v-if="mobileCodeTimer > 0" class="getMobileCode"
                  type="primary">{{ mobileCodeTimer }}秒后可重新获取</el-button>
            </el-form-item>
            <el-form-item prop="password" >
              <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="输入新密码"
                @keyup.enter.native="handleLogin" >
                <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="repassword" >
              <el-input v-model="loginForm.repassword" type="password" auto-complete="off" placeholder="再次输入密码"
                @keyup.enter.native="handleLogin" >
                <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
              </el-input>
            </el-form-item>

          <!-- 下方的登录按钮 -->
          <el-form-item style="width: 100%;margin-top:22px">
            <el-button
              :loading="loading"
              size="medium"
              type="primary"
              style="width:100%; "
              @click.native.prevent="getCode"
            >
              <span v-if="!loading">注  册</span>
              <span v-else>登 录 中...</span>
            </el-button>
            
            <div style="float: right;">
              <el-checkbox label=""></el-checkbox>
              <span>&nbsp;&nbsp;阅读并同意</span>
              <el-button  size="mini" type="text">《用户服务协议》</el-button>
            </div>
          </el-form-item>

        
            </el-form>
        </div>
      </div>
  </div>
</template>

<script>
import { sendSmsCode, socialAuthRedirect } from "@/api/login";
import { getTenantIdByName } from "@/api/system/tenant";
import { SystemUserSocialTypeEnum } from "@/utils/constants";
import { getCaptchaEnable, getTenantEnable } from "@/utils/ruoyi";
import {
  getPassword,
  getRememberMe,
  getTenantName,
  getUsername,
  removePassword,
  removeRememberMe,
  removeTenantName,
  removeUsername,
  setPassword,
  setRememberMe,
  setTenantId,
  setTenantName,
  setUsername,
} from "@/utils/auth";

import Verify from "@/components/Verifition/Verify";
import { resetUserPwd } from "@/api/system/user";

export default {
  name: "Login",
  components: {
    Verify,
  },
  data() {
    return {
      codeUrl: "",
      captchaEnable: true,
      tenantEnable: true,
      mobileCodeTimer: 0,
      loginForm: {
        username: "admin",
        password: "",
        captchaVerification: "",
        mobile: "",
        mobileCode: "",
        rememberMe: false,
        tenantName: "测试租户",
      },
      scene: 21,

      LoginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" },
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" },
        ],
        mobile: [
          { required: true, trigger: "blur", message: "手机号不能为空" },
          {
            validator: function (rule, value, callback) {
              if (
                /^(?:(?:\+|00)86)?1(?:3[\d]|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8[\d]|9[189])\d{8}$/.test(
                  value
                ) === false
              ) {
                callback(new Error("手机号格式错误"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
      },
      loading: false,
      redirect: undefined,
      // 枚举
      SysUserSocialTypeEnum: SystemUserSocialTypeEnum,
    };
  },
  created() {
    // 租户开关
    this.tenantEnable = getTenantEnable();
    if (this.tenantEnable) {
      // getTenantIdByName(this.loginForm.tenantName).then((res) => {
      //   // 设置租户
      //   const tenantId = 1;//res.data;
      //   if (tenantId && tenantId >= 0) {
      //     setTenantId(tenantId);
      //   }
      // });
      let tenantId = 1;
      setTenantId(tenantId);
    }
  },
  methods: {
    handleLogin(captchaParams) {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          // 设置 Cookie
          if (this.loginForm.rememberMe) {
            setUsername(this.loginForm.username);
            setPassword(this.loginForm.password);
            setRememberMe(this.loginForm.rememberMe);
            setTenantName(this.loginForm.tenantName);
          } else {
            removeUsername();
            removePassword();
            removeRememberMe();
            removeTenantName();
          }
          this.loginForm.captchaVerification =
            captchaParams.captchaVerification;
          // 发起登陆
          // console.log("发起登录", this.loginForm);
          this.$store
            .dispatch(
              this.loginForm.loginType === "sms" ? "SmsLogin" : "Login",
              this.loginForm
            )
            .then(() => {
              debugger;
              this.$router.push({ path: this.redirect || "/" }).catch(() => {});
            })
            .catch(() => {
              this.loading = false;
            });
        }
      });
    },
    /** ========== 以下为升级短信登录 ========== */
    getSmsCode() {
      if (this.mobileCodeTimer > 0) return;
      this.$refs.loginForm.validate((valid) => {
        if (!valid) return;
        sendSmsCode(
          this.loginForm.mobile,
          this.scene,
          this.loginForm.uuid,
          this.loginForm.code
        ).then((res) => {
          this.$modal.msgSuccess("获取验证码成功");
          this.mobileCodeTimer = 60;
          let msgTimer = setInterval(() => {
            this.mobileCodeTimer = this.mobileCodeTimer - 1;
            if (this.mobileCodeTimer <= 0) {
              clearInterval(msgTimer);
            }
          }, 1000);
        });
      });
    },

    gotoLoginHandle(){
      console.log('返回登录');
      window.location.href = 'login'
    }
  },
};
</script>
<style lang="scss" scoped>
  @import "~@/assets/styles/login.scss";
  
  .clean-bg{
    background-image: none;

    .register-content{
      width: 410px;
      height: 500px;
      padding-top: 20px;

      .register-top{
        display: flex;
        justify-content: space-between;
        padding: 30px;

        .title{
          font-size: 18px;
          font-weight: 600;
        }
      }

      .register-field{
        left: 0;
        width: 100%;
        margin-top: 30px;
        :deep(.el-button--primary){
          background-color: #1890ff;
          border-color: #1890ff;
        }
        :deep(.el-button--text){
          color: #1890ff;
        }
      }
    }

  }

</style>
