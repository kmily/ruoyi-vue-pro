<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="用户ID" prop="systemUserId">
                      <el-input v-model="formData.systemUserId" placeholder="请输入用户ID" />
                    </el-form-item>
                    <el-form-item label="用户类型" prop="systemUserType">
                      <el-select v-model="formData.systemUserType" placeholder="请选择用户类型">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="isv业务系统的申请单id" prop="isvBizId">
                      <el-input v-model="formData.isvBizId" placeholder="请输入isv业务系统的申请单id" />
                    </el-form-item>
                    <el-form-item label="协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。" prop="appAuthToken">
                      <el-input v-model="formData.appAuthToken" placeholder="请输入协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。" />
                    </el-form-item>
                    <el-form-item label="支付宝分配给开发者的应用Id" prop="appId">
                      <el-input v-model="formData.appId" placeholder="请输入支付宝分配给开发者的应用Id" />
                    </el-form-item>
                    <el-form-item label="支付宝分配给商户的应用Id" prop="authAppId">
                      <el-input v-model="formData.authAppId" placeholder="请输入支付宝分配给商户的应用Id" />
                    </el-form-item>
                    <el-form-item label="被授权用户id" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入被授权用户id" />
                    </el-form-item>
                    <el-form-item label="商家支付宝账号对应的ID，2088开头" prop="merchantPid">
                      <el-input v-model="formData.merchantPid" placeholder="请输入商家支付宝账号对应的ID，2088开头" />
                    </el-form-item>
                    <el-form-item label="NONE：未签约，表示还没有签约该产品" prop="signStatus">
                      <el-radio-group v-model="formData.signStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="产品签约状态对象" prop="signStatusList">
                      <el-input v-model="formData.signStatusList" placeholder="请输入产品签约状态对象" />
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
  import * as AlipayIsvApi from '@/api/steam/alipayisv';
      export default {
    name: "AlipayIsvForm",
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
                            systemUserId: undefined,
                            systemUserType: undefined,
                            isvBizId: undefined,
                            appAuthToken: undefined,
                            appId: undefined,
                            authAppId: undefined,
                            userId: undefined,
                            merchantPid: undefined,
                            signStatus: undefined,
                            signStatusList: undefined,
        },
        // 表单校验
        formRules: {
                        systemUserId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
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
            const res = await AlipayIsvApi.getAlipayIsv(id);
            this.formData = res.data;
            this.title = "修改签约ISV用户";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增签约ISV用户";
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
            await AlipayIsvApi.updateAlipayIsv(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await AlipayIsvApi.createAlipayIsv(data);
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
                            systemUserId: undefined,
                            systemUserType: undefined,
                            isvBizId: undefined,
                            appAuthToken: undefined,
                            appId: undefined,
                            authAppId: undefined,
                            userId: undefined,
                            merchantPid: undefined,
                            signStatus: undefined,
                            signStatusList: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>