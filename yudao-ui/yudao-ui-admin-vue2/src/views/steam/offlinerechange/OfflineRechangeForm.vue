<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="用户ID" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入用户ID" />
                    </el-form-item>
                    <el-form-item label="手机号" prop="userId">
                      <el-input v-model="formData.mobile" placeholder="请输入手机号" />
                    </el-form-item>

                    <el-form-item label="用户类型" prop="userType">
                      <el-radio-group v-model="formData.userType">
                            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                                      :key="dict.value" :label="parseInt(dict.value)"
>{{dict.label}}</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="充值金额" prop="amount">
                      <el-input v-model="formData.amount" placeholder="请输入充值金额" />
                    </el-form-item>
                    <el-form-item label="备注">
                      <Editor v-model="formData.comment" :min-height="192"/>
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
  import * as OfflineRechangeApi from '@/api/steam/offlinerechange';
  import Editor from '@/components/Editor';
      export default {
    name: "OfflineRechangeForm",
    components: {
          Editor,
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
                            userId: undefined,
                            mobile: undefined,
                            userType: undefined,
                            amount: undefined,
                            comment: undefined,
                            id: undefined,
        },
        // 表单校验
        formRules: {
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
            const res = await OfflineRechangeApi.getOfflineRechange(id);
            this.formData = res.data;
            this.title = "修改线下人工充值";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增线下人工充值";
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
            await OfflineRechangeApi.updateOfflineRechange(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await OfflineRechangeApi.createOfflineRechange(data);
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
                            userId: undefined,
                            mobile: undefined,
                            userType: undefined,
                            amount: undefined,
                            comment: undefined,
                            id: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
