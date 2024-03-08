<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="端口" prop="port">
                      <el-input v-model="formData.port" placeholder="请输入端口" />
                    </el-form-item>
                    <el-form-item label="地址池id" prop="addressId">
                      <el-input v-model="formData.addressId" placeholder="请输入地址池id" />
                    </el-form-item>
                    <el-form-item label="ip地址" prop="ipAddress">
                      <el-input v-model="formData.ipAddress" placeholder="请输入ip地址" />
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
  import * as BindIpaddressApi from '@/api/steam/bindipaddress';
      export default {
    name: "BindIpaddressForm",
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
                            port: undefined,
                            addressId: undefined,
                            ipAddress: undefined,
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
            const res = await BindIpaddressApi.getBindIpaddress(id);
            this.formData = res.data;
            this.title = "修改绑定用户IP地址池";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增绑定用户IP地址池";
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
            await BindIpaddressApi.updateBindIpaddress(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await BindIpaddressApi.createBindIpaddress(data);
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
                            port: undefined,
                            addressId: undefined,
                            ipAddress: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>