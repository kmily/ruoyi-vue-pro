<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="价格，单位：分 " prop="payAmount">
                      <el-input v-model="formData.payAmount" placeholder="请输入价格，单位：分 " />
                    </el-form-item>
                    <el-form-item label="是否已支付：[0:未支付 1:已经支付过]" prop="payStatus">
                      <el-radio-group v-model="formData.payStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
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
                    <el-form-item label="退款金额，单位：分" prop="refundPrice">
                      <el-input v-model="formData.refundPrice" placeholder="请输入退款金额，单位：分" />
                    </el-form-item>
                    <el-form-item label="退款订单编号" prop="payRefundId">
                      <el-input v-model="formData.payRefundId" placeholder="请输入退款订单编号" />
                    </el-form-item>
                    <el-form-item label="退款时间" prop="refundTime">
                      <el-date-picker clearable v-model="formData.refundTime" type="date" value-format="timestamp" placeholder="选择退款时间" />
                    </el-form-item>
                    <el-form-item label="用户编号" prop="userId">
                      <el-input v-model="formData.userId" placeholder="请输入用户编号" />
                    </el-form-item>
                    <el-form-item label="用户类型" prop="userType">
                      <el-select v-model="formData.userType" placeholder="请选择用户类型">
                            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
                      </el-select>
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
                    <el-form-item label="商户订单号" prop="merchantOrderNo">
                      <el-input v-model="formData.merchantOrderNo" placeholder="请输入商户订单号" />
                    </el-form-item>
                    <el-form-item label="订单号，内容生成" prop="orderNo">
                      <el-input v-model="formData.orderNo" placeholder="请输入订单号，内容生成" />
                    </el-form-item>
                    <el-form-item label="发货模式 0,卖家直发；1,极速发货" prop="shippingMode">
                      <el-input v-model="formData.shippingMode" placeholder="请输入发货模式 0,卖家直发；1,极速发货" />
                    </el-form-item>
                    <el-form-item label=" 收货方的Steam交易链接" prop="tradeLinks">
                      <el-input v-model="formData.tradeLinks" placeholder="请输入 收货方的Steam交易链接" />
                    </el-form-item>
                    <el-form-item label="商品模版ID" prop="commodityTemplateId">
                      <el-input v-model="formData.commodityTemplateId" placeholder="请输入商品模版ID" />
                    </el-form-item>
                    <el-form-item label="模板hashname" prop="commodityHashName">
                      <el-input v-model="formData.commodityHashName" placeholder="请输入模板hashname" />
                    </el-form-item>
                    <el-form-item label="商品ID" prop="commodityId">
                      <el-input v-model="formData.commodityId" placeholder="请输入商品ID" />
                    </el-form-item>
                    <el-form-item label="购买最高价,单价元" prop="purchasePrice">
                      <el-input v-model="formData.purchasePrice" placeholder="请输入购买最高价,单价元" />
                    </el-form-item>
                    <el-form-item label="实际商品ID" prop="realCommodityId">
                      <el-input v-model="formData.realCommodityId" placeholder="请输入实际商品ID" />
                    </el-form-item>
                    <el-form-item label="极速发货购买模式0：优先购买极速发货；1：只购买极速发货" prop="fastShipping">
                      <el-input v-model="formData.fastShipping" placeholder="请输入极速发货购买模式0：优先购买极速发货；1：只购买极速发货" />
                    </el-form-item>
                    <el-form-item label="有品订单号" prop="uuOrderNo">
                      <el-input v-model="formData.uuOrderNo" placeholder="请输入有品订单号" />
                    </el-form-item>
                    <el-form-item label="有品商户订单号" prop="uuMerchantOrderNo">
                      <el-input v-model="formData.uuMerchantOrderNo" placeholder="请输入有品商户订单号" />
                    </el-form-item>
                    <el-form-item label="交易状态 0,成功；2,失败。" prop="uuOrderStatus">
                      <el-radio-group v-model="formData.uuOrderStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="收款状态" prop="sellCashStatus">
                      <el-radio-group v-model="formData.sellCashStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="卖家用户ID" prop="sellUserId">
                      <el-input v-model="formData.sellUserId" placeholder="请输入卖家用户ID" />
                    </el-form-item>
                    <el-form-item label="卖家用户类型" prop="sellUserType">
                      <el-select v-model="formData.sellUserType" placeholder="请选择卖家用户类型">
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
  import * as YouyouOrderApi from '@/api/steam/youyouorder';
      export default {
    name: "YouyouOrderForm",
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
                            payAmount: undefined,
                            payStatus: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            refundPrice: undefined,
                            payRefundId: undefined,
                            refundTime: undefined,
                            userId: undefined,
                            userType: undefined,
                            transferText: undefined,
                            transferStatus: undefined,
                            payOrderStatus: undefined,
                            merchantOrderNo: undefined,
                            orderNo: undefined,
                            shippingMode: undefined,
                            tradeLinks: undefined,
                            commodityTemplateId: undefined,
                            commodityHashName: undefined,
                            commodityId: undefined,
                            purchasePrice: undefined,
                            realCommodityId: undefined,
                            fastShipping: undefined,
                            uuOrderNo: undefined,
                            uuMerchantOrderNo: undefined,
                            uuOrderStatus: undefined,
                            sellCashStatus: undefined,
                            sellUserId: undefined,
                            sellUserType: undefined,
        },
        // 表单校验
        formRules: {
                        payAmount: [{ required: true, message: '价格，单位：分 不能为空', trigger: 'blur' }],
                        payStatus: [{ required: true, message: '是否已支付：[0:未支付 1:已经支付过]不能为空', trigger: 'blur' }],
                        refundPrice: [{ required: true, message: '退款金额，单位：分不能为空', trigger: 'blur' }],
                        userId: [{ required: true, message: '用户编号不能为空', trigger: 'blur' }],
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
            const res = await YouyouOrderApi.getYouyouOrder(id);
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
            await YouyouOrderApi.updateYouyouOrder(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await YouyouOrderApi.createYouyouOrder(data);
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
                            payAmount: undefined,
                            payStatus: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            refundPrice: undefined,
                            payRefundId: undefined,
                            refundTime: undefined,
                            userId: undefined,
                            userType: undefined,
                            transferText: undefined,
                            transferStatus: undefined,
                            payOrderStatus: undefined,
                            merchantOrderNo: undefined,
                            orderNo: undefined,
                            shippingMode: undefined,
                            tradeLinks: undefined,
                            commodityTemplateId: undefined,
                            commodityHashName: undefined,
                            commodityId: undefined,
                            purchasePrice: undefined,
                            realCommodityId: undefined,
                            fastShipping: undefined,
                            uuOrderNo: undefined,
                            uuMerchantOrderNo: undefined,
                            uuOrderStatus: undefined,
                            sellCashStatus: undefined,
                            sellUserId: undefined,
                            sellUserType: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
