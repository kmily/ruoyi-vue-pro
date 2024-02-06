<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="steam名称" prop="steamName">
                      <el-input v-model="formData.steamName" placeholder="请输入steam名称" />
                    </el-form-item>
                    <el-form-item label="用户ID" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入用户ID" />
                    </el-form-item>
                    <el-form-item label="SteamId" prop="steamId">
                      <el-input v-model="formData.steamId" placeholder="请输入SteamId" />
                    </el-form-item>
                    <el-form-item label="交易链接" prop="tradeUrl">
                      <el-input v-model="formData.tradeUrl" placeholder="请输入交易链接" />
                    </el-form-item>
                    <el-form-item label="API KEY" prop="apiKey">
                      <el-input v-model="formData.apiKey" placeholder="请输入API KEY" />
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                      <el-input v-model="formData.remark" placeholder="请输入备注" />
                    </el-form-item>
                    <el-form-item label="登录名称" prop="loginName">
                      <el-input v-model="formData.loginName" placeholder="请输入登录名称" />
                    </el-form-item>
                    <el-form-item label="登录密码" prop="loginPassword">
                      <el-input v-model="formData.loginPassword" placeholder="请输入登录密码" />
                    </el-form-item>
                    <el-form-item label="登录环" prop="loginSharedSecret">
                      <el-input v-model="formData.loginSharedSecret" placeholder="请输入登录环" />
                    </el-form-item>
                    <el-form-item label="登录会话" prop="loginSession">
                      <el-input v-model="formData.loginSession" placeholder="请输入登录会话" />
                    </el-form-item>
                    <el-form-item label="登录时间" prop="loginTime">
                      <el-date-picker clearable v-model="formData.loginTime" type="date" value-format="timestamp" placeholder="选择登录时间" />
                    </el-form-item>
      </el-form>
              <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="formLoading">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as BindUserApi from '@/api/steam/binduser';
      export default {
    name: "BindUserForm",
    components: {
                    },
    data() {
      return {
        // 弹出层标题
        dialogTitle: "",
        // 是否显示弹出层
        dialogVisible: false,
        // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
        formLoading: false,
        // 表单参数
        formData: {
                            id: undefined,
                            steamName: undefined,
                            userId: undefined,
                            steamId: undefined,
                            tradeUrl: undefined,
                            apiKey: undefined,
                            remark: undefined,
                            loginName: undefined,
                            loginPassword: undefined,
                            loginSharedSecret: undefined,
                            loginSession: undefined,
                            loginTime: undefined,
        },
        // 表单校验
        formRules: {
                        userId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
                        steamId: [{ required: true, message: 'SteamId不能为空', trigger: 'blur' }],
        },
                        };
    },
    methods: {
      /** 打开弹窗 */
     async open(id) {
        this.dialogVisible = true;
        this.reset();
        // 修改时，设置数据
        if (id) {
          this.formLoading = true;
          try {
            const res = await BindUserApi.getBindUser(id);
            this.formData = res.data;
            this.title = "修改 steam用户绑定";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增 steam用户绑定";
              },
      /** 提交按钮 */
      async submitForm() {
        // 校验主表
        await this.$refs["formRef"].validate();
                  this.formLoading = true;
        try {
          const data = this.formData;
                  // 修改的提交
          if (data.id) {
            await BindUserApi.updateBindUser(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await BindUserApi.createBindUser(data);
          this.$modal.msgSuccess("新增成功");
          this.dialogVisible = false;
          this.$emit('success');
        } finally {
          this.formLoading = false;
        }
      },
                      /** 表单重置 */
      reset() {
        this.formData = {
                            id: undefined,
                            steamName: undefined,
                            userId: undefined,
                            steamId: undefined,
                            tradeUrl: undefined,
                            apiKey: undefined,
                            remark: undefined,
                            loginName: undefined,
                            loginPassword: undefined,
                            loginSharedSecret: undefined,
                            loginSession: undefined,
                            loginTime: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>