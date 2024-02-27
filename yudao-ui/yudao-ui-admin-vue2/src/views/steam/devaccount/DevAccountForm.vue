<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="用户ID" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入用户ID" />
                    </el-form-item>
                    <el-form-item label="api用户名" prop="userName">
                      <el-input v-model="formData.userName" placeholder="请输入api用户名" />
                    </el-form-item>
                    <el-form-item label="私匙" prop="apiPrivateKey">
                      <el-input v-model="formData.apiPrivateKey" type="textarea" placeholder="请输入内容" />
                    </el-form-item>
                    <el-form-item label="公匙" prop="apiPublicKey">
                      <el-input v-model="formData.apiPublicKey" type="textarea" placeholder="请输入内容" />
                    </el-form-item>
                    <el-form-item label="状态" prop="status">
                      <el-radio-group v-model="formData.status">
                            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                                      :key="dict.value" :label="parseInt(dict.value)"
>{{dict.label}}</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="用户类型" prop="userType">
                      <el-select v-model="formData.userType" placeholder="请选择用户类型">
                            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
                      </el-select>
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
  import * as DevAccountApi from '@/api/steam/devaccount';
      export default {
    name: "DevAccountForm",
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
                            userId: undefined,
                            userName: undefined,
                            apiPrivateKey: undefined,
                            apiPublicKey: undefined,
                            steamId: undefined,
                            status: undefined,
                            userType: undefined,
        },
        // 表单校验
        formRules: {
                        userName: [{ required: true, message: 'api用户名不能为空', trigger: 'blur' }],
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
            const res = await DevAccountApi.getDevAccount(id);
            this.formData = res.data;
            this.title = "修改开放平台用户";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增开放平台用户";
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
            await DevAccountApi.updateDevAccount(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await DevAccountApi.createDevAccount(data);
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
                            userId: undefined,
                            userName: undefined,
                            apiPrivateKey: undefined,
                            apiPublicKey: undefined,
                            steamId: undefined,
                            status: undefined,
                            userType: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
