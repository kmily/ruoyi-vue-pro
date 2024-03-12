<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="用户ID" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入用户ID" />
                    </el-form-item>
                    <el-form-item label="用户类型" prop="userType">
                      <el-select v-model="formData.userType" placeholder="请选择用户类型">
                            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="支付订单编号" prop="payOrderId">
                      <el-input v-model="formData.payOrderId" placeholder="请输入支付订单编号" />
                    </el-form-item>
                    <el-form-item label="支付成功的支付渠道" prop="payChannelCode">
                      <el-input v-model="formData.payChannelCode" placeholder="请输入支付成功的支付渠道" />
                    </el-form-item>
                    <el-form-item label="订单支付时间" prop="payTime">
                      <el-date-picker clearable v-model="formData.payTime" type="date" value-format="timestamp" placeholder="选择订单支付时间" />
                    </el-form-item>
                    <el-form-item label="退款订单编号" prop="payRefundId">
                      <el-input v-model="formData.payRefundId" placeholder="请输入退款订单编号" />
                    </el-form-item>
                    <el-form-item label="退款金额，单位分" prop="refundPrice">
                      <el-input v-model="formData.refundPrice" placeholder="请输入退款金额，单位分" />
                    </el-form-item>
                    <el-form-item label="退款时间" prop="refundTime">
                      <el-date-picker clearable v-model="formData.refundTime" type="date" value-format="timestamp" placeholder="选择退款时间" />
                    </el-form-item>
                    <el-form-item label="是否已支付" prop="payStatus">
                      <el-radio-group v-model="formData.payStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="提现金额" prop="withdrawalPrice">
                      <el-input v-model="formData.withdrawalPrice" placeholder="请输入提现金额" />
                    </el-form-item>
                    <el-form-item label="提现信息" prop="withdrawalInfo">
                      <el-input v-model="formData.withdrawalInfo" placeholder="请输入提现信息" />
                    </el-form-item>
                    <el-form-item label="服务费" prop="serviceFee">
                      <el-input v-model="formData.serviceFee" placeholder="请输入服务费" />
                    </el-form-item>
                    <el-form-item label="费率" prop="serviceFeeRate">
                      <el-input v-model="formData.serviceFeeRate" placeholder="请输入费率" />
                    </el-form-item>
                    <el-form-item label="支付金额" prop="paymentAmount">
                      <el-input v-model="formData.paymentAmount" placeholder="请输入支付金额" />
                    </el-form-item>
                    <el-form-item label="审批状态" prop="auditStatus">
                      <el-radio-group v-model="formData.auditStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="审核人" prop="auditUserId">
                      <el-input v-model="formData.auditUserId" placeholder="请输入审核人" />
                    </el-form-item>
                    <el-form-item label="审核信息" prop="auditMsg">
                      <el-input v-model="formData.auditMsg" placeholder="请输入审核信息" />
                    </el-form-item>
                    <el-form-item label="提现手续费收款钱包" prop="serviceFeeUserId">
                      <el-input v-model="formData.serviceFeeUserId" placeholder="请输入提现手续费收款钱包" />
                    </el-form-item>
                    <el-form-item label="提现手续费收款人类型" prop="serviceFeeUserType">
                      <el-select v-model="formData.serviceFeeUserType" placeholder="请选择提现手续费收款人类型">
                            <el-option label="请选择字典生成" value="" />
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
  import * as WithdrawalApi from '@/api/steam/withdrawal';
      export default {
    name: "WithdrawalForm",
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
                            userType: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            payRefundId: undefined,
                            refundPrice: undefined,
                            refundTime: undefined,
                            payStatus: undefined,
                            withdrawalPrice: undefined,
                            withdrawalInfo: undefined,
                            serviceFee: undefined,
                            serviceFeeRate: undefined,
                            paymentAmount: undefined,
                            auditStatus: undefined,
                            auditUserId: undefined,
                            auditMsg: undefined,
                            serviceFeeUserId: undefined,
                            serviceFeeUserType: undefined,
        },
        // 表单校验
        formRules: {
                        userId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
                        userType: [{ required: true, message: '用户类型不能为空', trigger: 'change' }],
                        payStatus: [{ required: true, message: '是否已支付不能为空', trigger: 'blur' }],
                        serviceFee: [{ required: true, message: '服务费不能为空', trigger: 'blur' }],
                        serviceFeeRate: [{ required: true, message: '费率不能为空', trigger: 'blur' }],
                        auditStatus: [{ required: true, message: '审批状态不能为空', trigger: 'blur' }],
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
            const res = await WithdrawalApi.getWithdrawal(id);
            this.formData = res.data;
            this.title = "修改提现";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增提现";
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
            await WithdrawalApi.updateWithdrawal(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await WithdrawalApi.createWithdrawal(data);
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
                            userType: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            payRefundId: undefined,
                            refundPrice: undefined,
                            refundTime: undefined,
                            payStatus: undefined,
                            withdrawalPrice: undefined,
                            withdrawalInfo: undefined,
                            serviceFee: undefined,
                            serviceFeeRate: undefined,
                            paymentAmount: undefined,
                            auditStatus: undefined,
                            auditUserId: undefined,
                            auditMsg: undefined,
                            serviceFeeUserId: undefined,
                            serviceFeeUserType: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
