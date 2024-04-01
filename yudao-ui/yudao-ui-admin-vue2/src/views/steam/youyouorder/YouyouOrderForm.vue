<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="用户编号" prop="buyUserId">
                      <el-input v-model="formData.buyUserId" placeholder="请输入用户编号" />
                    </el-form-item>
                    <el-form-item label="用户类型" prop="buyUserType">
                      <el-select v-model="formData.buyUserType" placeholder="请选择用户类型">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="买方绑定用户" prop="buyBindUserId">
                      <el-input v-model="formData.buyBindUserId" placeholder="请输入买方绑定用户" />
                    </el-form-item>
                    <el-form-item label="购买的steamId" prop="buySteamId">
                      <el-input v-model="formData.buySteamId" placeholder="请输入购买的steamId" />
                    </el-form-item>
                    <el-form-item label=" 收货方的Steam交易链接" prop="buyTradeLinks">
                      <el-input v-model="formData.buyTradeLinks" placeholder="请输入 收货方的Steam交易链接" />
                    </el-form-item>
                    <el-form-item label="卖家用户ID" prop="sellUserId">
                      <el-input v-model="formData.sellUserId" placeholder="请输入卖家用户ID" />
                    </el-form-item>
                    <el-form-item label="卖家用户类型" prop="sellUserType">
                      <el-select v-model="formData.sellUserType" placeholder="请选择卖家用户类型">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="卖方绑定用户ID" prop="sellBindUserId">
                      <el-input v-model="formData.sellBindUserId" placeholder="请输入卖方绑定用户ID" />
                    </el-form-item>
                    <el-form-item label="卖家steamId" prop="sellSteamId">
                      <el-input v-model="formData.sellSteamId" placeholder="请输入卖家steamId" />
                    </el-form-item>
                    <el-form-item label="订单号，内容生成" prop="orderNo">
                      <el-input v-model="formData.orderNo" placeholder="请输入订单号，内容生成" />
                    </el-form-item>
                    <el-form-item label="商户订单号" prop="merchantNo">
                      <el-input v-model="formData.merchantNo" placeholder="请输入商户订单号" />
                    </el-form-item>
                    <el-form-item label="商品模版ID" prop="commodityTemplateId">
                      <el-input v-model="formData.commodityTemplateId" placeholder="请输入商品模版ID" />
                    </el-form-item>
                    <el-form-item label="实际商品ID" prop="realCommodityId">
                      <el-input v-model="formData.realCommodityId" placeholder="请输入实际商品ID" />
                    </el-form-item>
                    <el-form-item label="商品ID" prop="commodityId">
                      <el-input v-model="formData.commodityId" placeholder="请输入商品ID" />
                    </el-form-item>
                    <el-form-item label="极速发货购买模式0：优先购买极速发货；1：只购买极速发货" prop="fastShipping">
                      <el-input v-model="formData.fastShipping" placeholder="请输入极速发货购买模式0：优先购买极速发货；1：只购买极速发货" />
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
                    <el-form-item label="价格，单位：分 " prop="payAmount">
                      <el-input v-model="formData.payAmount" placeholder="请输入价格，单位：分 " />
                    </el-form-item>
                    <el-form-item label="是否已支付：[0:未支付 1:已经支付过]" prop="payStatus">
                      <el-radio-group v-model="formData.payStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="订单支付状态" prop="payOrderStatus">
                      <el-radio-group v-model="formData.payOrderStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="退款订单编号" prop="payRefundId">
                      <el-input v-model="formData.payRefundId" placeholder="请输入退款订单编号" />
                    </el-form-item>
                    <el-form-item label="退款金额，单位：分" prop="refundPrice">
                      <el-input v-model="formData.refundPrice" placeholder="请输入退款金额，单位：分" />
                    </el-form-item>
                    <el-form-item label="退款时间" prop="refundTime">
                      <el-date-picker clearable v-model="formData.refundTime" type="date" value-format="timestamp" placeholder="选择退款时间" />
                    </el-form-item>
                    <el-form-item label="提现手续费收款人类型" prop="serviceFeeUserType">
                      <el-select v-model="formData.serviceFeeUserType" placeholder="请选择提现手续费收款人类型">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="服务费，单位分" prop="serviceFee">
                      <el-input v-model="formData.serviceFee" placeholder="请输入服务费，单位分" />
                    </el-form-item>
                    <el-form-item label="商户订单号" prop="merchantOrderNo">
                      <el-input v-model="formData.merchantOrderNo" placeholder="请输入商户订单号" />
                    </el-form-item>
                    <el-form-item label="模板hashname" prop="commodityHashName">
                      <el-input v-model="formData.commodityHashName" placeholder="请输入模板hashname" />
                    </el-form-item>
                    <el-form-item label="违约付款接口返回" prop="transferDamagesRet">
                      <el-input v-model="formData.transferDamagesRet" placeholder="请输入违约付款接口返回" />
                    </el-form-item>
                    <el-form-item label="收款状态" prop="sellCashStatus">
                      <el-radio-group v-model="formData.sellCashStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="发货状态" prop="transferStatus">
                      <el-radio-group v-model="formData.transferStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="交易失败时退还" prop="transferRefundAmount">
                      <el-input v-model="formData.transferRefundAmount" placeholder="请输入交易失败时退还" />
                    </el-form-item>
                    <el-form-item label="服务费率" prop="serviceFeeRate">
                      <el-input v-model="formData.serviceFeeRate" placeholder="请输入服务费率" />
                    </el-form-item>
                    <el-form-item label="提现手续费收款钱包" prop="serviceFeeUserId">
                      <el-input v-model="formData.serviceFeeUserId" placeholder="请输入提现手续费收款钱包" />
                    </el-form-item>
                    <el-form-item label="交易违约判定时间" prop="transferDamagesTime">
                      <el-date-picker clearable v-model="formData.transferDamagesTime" type="date" value-format="timestamp" placeholder="选择交易违约判定时间" />
                    </el-form-item>
                    <el-form-item label="商品名称" prop="marketName">
                      <el-input v-model="formData.marketName" placeholder="请输入商品名称" />
                    </el-form-item>
                    <el-form-item label="发货信息 json" prop="transferText">
                      <el-input v-model="formData.transferText" placeholder="请输入发货信息 json" />
                    </el-form-item>
                    <el-form-item label="购买最高价,单价元" prop="purchasePrice">
                      <el-input v-model="formData.purchasePrice" placeholder="请输入购买最高价,单价元" />
                    </el-form-item>
                    <el-form-item label="转帐接口返回" prop="serviceFeeRet">
                      <el-input v-model="formData.serviceFeeRet" placeholder="请输入转帐接口返回" />
                    </el-form-item>
                    <el-form-item label="发货模式 0,卖家直发；1,极速发货" prop="shippingMode">
                      <el-input v-model="formData.shippingMode" placeholder="请输入发货模式 0,卖家直发；1,极速发货" />
                    </el-form-item>
                    <el-form-item label="有品商户订单号" prop="uuMerchantOrderNo">
                      <el-input v-model="formData.uuMerchantOrderNo" placeholder="请输入有品商户订单号" />
                    </el-form-item>
                    <el-form-item label="购买用户编号。" prop="uuBuyerUserId">
                      <el-input v-model="formData.uuBuyerUserId" placeholder="请输入购买用户编号。" />
                    </el-form-item>
                    <el-form-item label="订单失败原因编号。" prop="uuFailCode">
                      <el-input v-model="formData.uuFailCode" placeholder="请输入订单失败原因编号。" />
                    </el-form-item>
                    <el-form-item label="订单失败原因提示信息。" prop="uuFailReason">
                      <el-input v-model="formData.uuFailReason" placeholder="请输入订单失败原因提示信息。" />
                    </el-form-item>
                    <el-form-item label="有品订单号" prop="uuOrderNo">
                      <el-input v-model="formData.uuOrderNo" placeholder="请输入有品订单号" />
                    </el-form-item>
                    <el-form-item label="通知类型描述(等待发货，等待收货，购买成功，订单取消)。" prop="uuNotifyDesc">
                      <el-input v-model="formData.uuNotifyDesc" placeholder="请输入通知类型描述(等待发货，等待收货，购买成功，订单取消)。" />
                    </el-form-item>
                    <el-form-item label="通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。" prop="uuNotifyType">
                      <el-select v-model="formData.uuNotifyType" placeholder="请选择通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="交易状态 0,成功；2,失败。" prop="uuOrderStatus">
                      <el-radio-group v-model="formData.uuOrderStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="订单小状态。" prop="uuOrderSubStatus">
                      <el-radio-group v-model="formData.uuOrderSubStatus">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="预留字段" prop="uuOrderSubType">
                      <el-select v-model="formData.uuOrderSubType" placeholder="请选择预留字段">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="预留字段" prop="uuOrderType">
                      <el-select v-model="formData.uuOrderType" placeholder="请选择预留字段">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="发货模式：0,卖家直发；1,极速发货" prop="uuShippingMode">
                      <el-input v-model="formData.uuShippingMode" placeholder="请输入发货模式：0,卖家直发；1,极速发货" />
                    </el-form-item>
                    <el-form-item label="报价ID" prop="uuTradeOfferId">
                      <el-input v-model="formData.uuTradeOfferId" placeholder="请输入报价ID" />
                    </el-form-item>
                    <el-form-item label="报价链接" prop="uuTradeOfferLinks">
                      <el-input v-model="formData.uuTradeOfferLinks" placeholder="请输入报价链接" />
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
                            buyUserId: undefined,
                            buyUserType: undefined,
                            buyBindUserId: undefined,
                            buySteamId: undefined,
                            buyTradeLinks: undefined,
                            sellUserId: undefined,
                            sellUserType: undefined,
                            sellBindUserId: undefined,
                            sellSteamId: undefined,
                            orderNo: undefined,
                            merchantNo: undefined,
                            commodityTemplateId: undefined,
                            realCommodityId: undefined,
                            commodityId: undefined,
                            fastShipping: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            payAmount: undefined,
                            payStatus: undefined,
                            payOrderStatus: undefined,
                            payRefundId: undefined,
                            refundPrice: undefined,
                            refundTime: undefined,
                            serviceFeeUserType: undefined,
                            serviceFee: undefined,
                            merchantOrderNo: undefined,
                            commodityHashName: undefined,
                            transferDamagesRet: undefined,
                            sellCashStatus: undefined,
                            transferStatus: undefined,
                            transferRefundAmount: undefined,
                            serviceFeeRate: undefined,
                            serviceFeeUserId: undefined,
                            transferDamagesTime: undefined,
                            marketName: undefined,
                            transferText: undefined,
                            purchasePrice: undefined,
                            serviceFeeRet: undefined,
                            shippingMode: undefined,
                            uuMerchantOrderNo: undefined,
                            uuBuyerUserId: undefined,
                            uuFailCode: undefined,
                            uuFailReason: undefined,
                            uuOrderNo: undefined,
                            uuNotifyDesc: undefined,
                            uuNotifyType: undefined,
                            uuOrderStatus: undefined,
                            uuOrderSubStatus: undefined,
                            uuOrderSubType: undefined,
                            uuOrderType: undefined,
                            uuShippingMode: undefined,
                            uuTradeOfferId: undefined,
                            uuTradeOfferLinks: undefined,
        },
        // 表单校验
        formRules: {
                        buyUserId: [{ required: true, message: '用户编号不能为空', trigger: 'blur' }],
                        payAmount: [{ required: true, message: '价格，单位：分 不能为空', trigger: 'blur' }],
                        payStatus: [{ required: true, message: '是否已支付：[0:未支付 1:已经支付过]不能为空', trigger: 'blur' }],
                        refundPrice: [{ required: true, message: '退款金额，单位：分不能为空', trigger: 'blur' }],
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
                            buyUserId: undefined,
                            buyUserType: undefined,
                            buyBindUserId: undefined,
                            buySteamId: undefined,
                            buyTradeLinks: undefined,
                            sellUserId: undefined,
                            sellUserType: undefined,
                            sellBindUserId: undefined,
                            sellSteamId: undefined,
                            orderNo: undefined,
                            merchantNo: undefined,
                            commodityTemplateId: undefined,
                            realCommodityId: undefined,
                            commodityId: undefined,
                            fastShipping: undefined,
                            payOrderId: undefined,
                            payChannelCode: undefined,
                            payTime: undefined,
                            payAmount: undefined,
                            payStatus: undefined,
                            payOrderStatus: undefined,
                            payRefundId: undefined,
                            refundPrice: undefined,
                            refundTime: undefined,
                            serviceFeeUserType: undefined,
                            serviceFee: undefined,
                            merchantOrderNo: undefined,
                            commodityHashName: undefined,
                            transferDamagesRet: undefined,
                            sellCashStatus: undefined,
                            transferStatus: undefined,
                            transferRefundAmount: undefined,
                            serviceFeeRate: undefined,
                            serviceFeeUserId: undefined,
                            transferDamagesTime: undefined,
                            marketName: undefined,
                            transferText: undefined,
                            purchasePrice: undefined,
                            serviceFeeRet: undefined,
                            shippingMode: undefined,
                            uuMerchantOrderNo: undefined,
                            uuBuyerUserId: undefined,
                            uuFailCode: undefined,
                            uuFailReason: undefined,
                            uuOrderNo: undefined,
                            uuNotifyDesc: undefined,
                            uuNotifyType: undefined,
                            uuOrderStatus: undefined,
                            uuOrderSubStatus: undefined,
                            uuOrderSubType: undefined,
                            uuOrderType: undefined,
                            uuShippingMode: undefined,
                            uuTradeOfferId: undefined,
                            uuTradeOfferLinks: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>