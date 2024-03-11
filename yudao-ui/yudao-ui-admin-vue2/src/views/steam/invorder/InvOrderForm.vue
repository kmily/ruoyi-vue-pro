<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="用户编号" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入用户编号" />
                    </el-form-item>
                    <el-form-item label="是否已支付：[0:未支付 1:已经支付过]" prop="payStatus">
                      <el-radio-group v-model="formData.payStatus">
                            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_BOOLEAN_STRING)"
                                      :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                      </el-radio-group>
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
                    <el-form-item label="退款时间" prop="refundTime">
                      <el-date-picker clearable v-model="formData.refundTime" type="date" value-format="timestamp" placeholder="选择退款时间" />
                    </el-form-item>
                    <el-form-item label="用户类型" prop="userType">
                      <el-select v-model="formData.userType" placeholder="请选择用户类型">
                            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="购买的steamId" prop="steamId">
                      <el-input v-model="formData.steamId" placeholder="请输入购买的steamId" />
                    </el-form-item>
                    <el-form-item label="发货信息 json" prop="transferText">
                      <el-input v-model="formData.transferText" placeholder="请输入发货信息 json" />
                    </el-form-item>
                    <el-form-item label="发货状态" prop="transferStatus">
                      <el-radio-group v-model="formData.transferStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="订单支付状态" prop="payOrderStatus">
                      <el-radio-group v-model="formData.payOrderStatus">
                            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.PAY_ORDER_STATUS)"
                                      :key="dict.value" :label="parseInt(dict.value)"
>{{dict.label}}</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="库存表ID参考steam_sell" prop="sellId">
                      <el-input v-model="formData.sellId" placeholder="请输入库存表ID参考steam_sell" />
                    </el-form-item>
                    <el-form-item label="商品描述ID" prop="invDescId">
                      <el-input v-model="formData.invDescId" placeholder="请输入商品描述ID" />
                    </el-form-item>
                    <el-form-item label="库存表ID" prop="invId">
                      <el-input v-model="formData.invId" placeholder="请输入库存表ID" />
                    </el-form-item>
                    <el-form-item label="卖家用户类型" prop="sellUserType">
                      <el-select v-model="formData.sellUserType" placeholder="请选择卖家用户类型">
                            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="卖家ID" prop="sellUserId">
                      <el-input v-model="formData.sellUserId" placeholder="请输入卖家ID" />
                    </el-form-item>
                    <el-form-item label="卖家金额状态" prop="sellCashStatus">
                      <el-radio-group v-model="formData.sellCashStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="退款金额，单位：分" prop="refundAmount">
                      <el-input v-model="formData.refundAmount" placeholder="请输入退款金额，单位：分" />
                    </el-form-item>
                    <el-form-item label="价格，单位：分" prop="paymentAmount">
                      <el-input v-model="formData.paymentAmount" placeholder="请输入价格，单位：分" />
                    </el-form-item>
                    <el-form-item label="服务费，单位分" prop="serviceFee">
                      <el-input v-model="formData.serviceFee" placeholder="请输入服务费，单位分" />
                    </el-form-item>
                    <el-form-item label="服务费率" prop="serviceFeeRate">
                      <el-input v-model="formData.serviceFeeRate" placeholder="请输入服务费率" />
                    </el-form-item>
                    <el-form-item label="商品总额" prop="commodityAmount">
                      <el-input v-model="formData.commodityAmount" placeholder="请输入商品总额" />
                    </el-form-item>
                    <el-form-item label="优惠金额 分" prop="discountAmount">
                      <el-input v-model="formData.discountAmount" placeholder="请输入优惠金额 分" />
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
  import * as InvOrderApi from '@/api/steam/invorder';
      export default {
    name: "InvOrderForm",
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
                            payStatus: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            payRefundId: undefined,
                            refundTime: undefined,
                            userType: undefined,
                            steamId: undefined,
                            transferText: undefined,
                            transferStatus: undefined,
                            payOrderStatus: undefined,
                            sellId: undefined,
                            invDescId: undefined,
                            invId: undefined,
                            sellUserType: undefined,
                            sellUserId: undefined,
                            sellCashStatus: undefined,
                            refundAmount: undefined,
                            paymentAmount: undefined,
                            serviceFee: undefined,
                            serviceFeeRate: undefined,
                            commodityAmount: undefined,
                            discountAmount: undefined,
        },
        // 表单校验
        formRules: {
                        userId: [{ required: true, message: '用户编号不能为空', trigger: 'blur' }],
                        payStatus: [{ required: true, message: '是否已支付：[0:未支付 1:已经支付过]不能为空', trigger: 'blur' }],
                        steamId: [{ required: true, message: '购买的steamId不能为空', trigger: 'blur' }],
                        refundAmount: [{ required: true, message: '退款金额，单位：分不能为空', trigger: 'blur' }],
                        paymentAmount: [{ required: true, message: '价格，单位：分不能为空', trigger: 'blur' }],
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
            const res = await InvOrderApi.getInvOrder(id);
            this.formData = res.data;
            this.title = "修改steam订单";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增steam订单";
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
            await InvOrderApi.updateInvOrder(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await InvOrderApi.createInvOrder(data);
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
                            payStatus: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            payRefundId: undefined,
                            refundTime: undefined,
                            userType: undefined,
                            steamId: undefined,
                            transferText: undefined,
                            transferStatus: undefined,
                            payOrderStatus: undefined,
                            sellId: undefined,
                            invDescId: undefined,
                            invId: undefined,
                            sellUserType: undefined,
                            sellUserId: undefined,
                            sellCashStatus: undefined,
                            refundAmount: undefined,
                            paymentAmount: undefined,
                            serviceFee: undefined,
                            serviceFeeRate: undefined,
                            commodityAmount: undefined,
                            discountAmount: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
