<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="通过申请获取的AppKey" prop="appkey">
                      <el-input v-model="formData.appkey" placeholder="请输入通过申请获取的AppKey" />
                    </el-form-item>
                    <el-form-item label="时间戳" prop="timestamp">
                      <el-date-picker clearable v-model="formData.timestamp" type="date" value-format="timestamp" placeholder="选择时间戳" />
                    </el-form-item>
                    <el-form-item label="API输入参数签名结果" prop="sign">
                      <el-input v-model="formData.sign" placeholder="请输入API输入参数签名结果" />
                    </el-form-item>
                    <el-form-item label="明细类型，1=订单明细，2=资金流水" prop="dataType">
                      <el-input v-model="formData.dataType" placeholder="请输入明细类型，1=订单明细，2=资金流水" />
                    </el-form-item>
                    <el-form-item label="开始时间" prop="startTime">
                      <el-date-picker clearable v-model="formData.startTime" type="date" value-format="timestamp" placeholder="选择开始时间" />
                    </el-form-item>
                    <el-form-item label="结束时间" prop="endTime">
                      <el-date-picker clearable v-model="formData.endTime" type="date" value-format="timestamp" placeholder="选择结束时间" />
                    </el-form-item>
                    <el-form-item label="申请标识" prop="applyCode">
                      <el-input v-model="formData.applyCode" placeholder="请输入申请标识" />
                    </el-form-item>
                    <el-form-item label="查询明细结果返回的url" prop="data">
                      <el-input v-model="formData.data" placeholder="请输入查询明细结果返回的url" />
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
  import * as YouyouDetailsApi from '@/api/steam/youyoudetails';
      export default {
    name: "YouyouDetailsForm",
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
                            appkey: undefined,
                            timestamp: undefined,
                            sign: undefined,
                            dataType: undefined,
                            startTime: undefined,
                            endTime: undefined,
                            applyCode: undefined,
                            data: undefined,
        },
        // 表单校验
        formRules: {
                        appkey: [{ required: true, message: '通过申请获取的AppKey不能为空', trigger: 'blur' }],
                        dataType: [{ required: true, message: '明细类型，1=订单明细，2=资金流水不能为空', trigger: 'blur' }],
                        applyCode: [{ required: true, message: '申请标识不能为空', trigger: 'blur' }],
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
            const res = await YouyouDetailsApi.getYouyouDetails(id);
            this.formData = res.data;
            this.title = "修改用户查询明细";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增用户查询明细";
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
            await YouyouDetailsApi.updateYouyouDetails(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await YouyouDetailsApi.createYouyouDetails(data);
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
                            appkey: undefined,
                            timestamp: undefined,
                            sign: undefined,
                            dataType: undefined,
                            startTime: undefined,
                            endTime: undefined,
                            applyCode: undefined,
                            data: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>