<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="用户编号" prop="buyUserId">
        <el-input v-model="queryParams.buyUserId" placeholder="请输入用户编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户类型" prop="buyUserType">
        <el-select v-model="queryParams.buyUserType" placeholder="请选择用户类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="买方绑定用户" prop="buyBindUserId">
        <el-input v-model="queryParams.buyBindUserId" placeholder="请输入买方绑定用户" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买的steamId" prop="buySteamId">
        <el-input v-model="queryParams.buySteamId" placeholder="请输入购买的steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label=" 收货方的Steam交易链接" prop="buyTradeLinks">
        <el-input v-model="queryParams.buyTradeLinks" placeholder="请输入 收货方的Steam交易链接" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="卖家用户ID" prop="sellUserId">
        <el-input v-model="queryParams.sellUserId" placeholder="请输入卖家用户ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="卖家用户类型" prop="sellUserType">
        <el-select v-model="queryParams.sellUserType" placeholder="请选择卖家用户类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="卖方绑定用户ID" prop="sellBindUserId">
        <el-input v-model="queryParams.sellBindUserId" placeholder="请输入卖方绑定用户ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="卖家steamId" prop="sellSteamId">
        <el-input v-model="queryParams.sellSteamId" placeholder="请输入卖家steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单号，内容生成" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号，内容生成" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商户订单号" prop="merchantNo">
        <el-input v-model="queryParams.merchantNo" placeholder="请输入商户订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品模版ID" prop="commodityTemplateId">
        <el-input v-model="queryParams.commodityTemplateId" placeholder="请输入商品模版ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="实际商品ID" prop="realCommodityId">
        <el-input v-model="queryParams.realCommodityId" placeholder="请输入实际商品ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品ID" prop="commodityId">
        <el-input v-model="queryParams.commodityId" placeholder="请输入商品ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="极速发货购买模式0：优先购买极速发货；1：只购买极速发货" prop="fastShipping">
        <el-input v-model="queryParams.fastShipping" placeholder="请输入极速发货购买模式0：优先购买极速发货；1：只购买极速发货" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付订单编号" prop="payOrderId">
        <el-input v-model="queryParams.payOrderId" placeholder="请输入支付订单编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付成功的支付渠道" prop="payChannelCode">
        <el-input v-model="queryParams.payChannelCode" placeholder="请输入支付成功的支付渠道" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单支付时间" prop="payTime">
        <el-date-picker v-model="queryParams.payTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="价格，单位：分 " prop="payAmount">
        <el-input v-model="queryParams.payAmount" placeholder="请输入价格，单位：分 " clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="是否已支付：[0:未支付 1:已经支付过]" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择是否已支付：[0:未支付 1:已经支付过]" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单支付状态" prop="payOrderStatus">
        <el-select v-model="queryParams.payOrderStatus" placeholder="请选择订单支付状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="退款订单编号" prop="payRefundId">
        <el-input v-model="queryParams.payRefundId" placeholder="请输入退款订单编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款金额，单位：分" prop="refundPrice">
        <el-input v-model="queryParams.refundPrice" placeholder="请输入退款金额，单位：分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款时间" prop="refundTime">
        <el-date-picker v-model="queryParams.refundTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="提现手续费收款人类型" prop="serviceFeeUserType">
        <el-select v-model="queryParams.serviceFeeUserType" placeholder="请选择提现手续费收款人类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="服务费，单位分" prop="serviceFee">
        <el-input v-model="queryParams.serviceFee" placeholder="请输入服务费，单位分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商户订单号" prop="merchantOrderNo">
        <el-input v-model="queryParams.merchantOrderNo" placeholder="请输入商户订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="模板hashname" prop="commodityHashName">
        <el-input v-model="queryParams.commodityHashName" placeholder="请输入模板hashname" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="违约付款接口返回" prop="transferDamagesRet">
        <el-input v-model="queryParams.transferDamagesRet" placeholder="请输入违约付款接口返回" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="收款状态" prop="sellCashStatus">
        <el-select v-model="queryParams.sellCashStatus" placeholder="请选择收款状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="发货状态" prop="transferStatus">
        <el-select v-model="queryParams.transferStatus" placeholder="请选择发货状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="交易失败时退还" prop="transferRefundAmount">
        <el-input v-model="queryParams.transferRefundAmount" placeholder="请输入交易失败时退还" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="服务费率" prop="serviceFeeRate">
        <el-input v-model="queryParams.serviceFeeRate" placeholder="请输入服务费率" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="提现手续费收款钱包" prop="serviceFeeUserId">
        <el-input v-model="queryParams.serviceFeeUserId" placeholder="请输入提现手续费收款钱包" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="交易违约判定时间" prop="transferDamagesTime">
        <el-date-picker v-model="queryParams.transferDamagesTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="商品名称" prop="marketName">
        <el-input v-model="queryParams.marketName" placeholder="请输入商品名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货信息 json" prop="transferText">
        <el-input v-model="queryParams.transferText" placeholder="请输入发货信息 json" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买最高价,单价元" prop="purchasePrice">
        <el-input v-model="queryParams.purchasePrice" placeholder="请输入购买最高价,单价元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="转帐接口返回" prop="serviceFeeRet">
        <el-input v-model="queryParams.serviceFeeRet" placeholder="请输入转帐接口返回" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货模式 0,卖家直发；1,极速发货" prop="shippingMode">
        <el-input v-model="queryParams.shippingMode" placeholder="请输入发货模式 0,卖家直发；1,极速发货" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="有品商户订单号" prop="uuMerchantOrderNo">
        <el-input v-model="queryParams.uuMerchantOrderNo" placeholder="请输入有品商户订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买用户编号。" prop="uuBuyerUserId">
        <el-input v-model="queryParams.uuBuyerUserId" placeholder="请输入购买用户编号。" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单失败原因编号。" prop="uuFailCode">
        <el-input v-model="queryParams.uuFailCode" placeholder="请输入订单失败原因编号。" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单失败原因提示信息。" prop="uuFailReason">
        <el-input v-model="queryParams.uuFailReason" placeholder="请输入订单失败原因提示信息。" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="有品订单号" prop="uuOrderNo">
        <el-input v-model="queryParams.uuOrderNo" placeholder="请输入有品订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="通知类型描述(等待发货，等待收货，购买成功，订单取消)。" prop="uuNotifyDesc">
        <el-input v-model="queryParams.uuNotifyDesc" placeholder="请输入通知类型描述(等待发货，等待收货，购买成功，订单取消)。" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。" prop="uuNotifyType">
        <el-select v-model="queryParams.uuNotifyType" placeholder="请选择通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="交易状态 0,成功；2,失败。" prop="uuOrderStatus">
        <el-select v-model="queryParams.uuOrderStatus" placeholder="请选择交易状态 0,成功；2,失败。" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单小状态。" prop="uuOrderSubStatus">
        <el-select v-model="queryParams.uuOrderSubStatus" placeholder="请选择订单小状态。" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="预留字段" prop="uuOrderSubType">
        <el-select v-model="queryParams.uuOrderSubType" placeholder="请选择预留字段" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="预留字段" prop="uuOrderType">
        <el-select v-model="queryParams.uuOrderType" placeholder="请选择预留字段" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="发货模式：0,卖家直发；1,极速发货" prop="uuShippingMode">
        <el-input v-model="queryParams.uuShippingMode" placeholder="请输入发货模式：0,卖家直发；1,极速发货" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="报价ID" prop="uuTradeOfferId">
        <el-input v-model="queryParams.uuTradeOfferId" placeholder="请输入报价ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="报价链接" prop="uuTradeOfferLinks">
        <el-input v-model="queryParams.uuTradeOfferLinks" placeholder="请输入报价链接" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="openForm(undefined)"
                   v-hasPermi="['steam:youyou-order:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:youyou-order:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="订单编号" align="center" prop="id" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户编号" align="center" prop="buyUserId" />
      <el-table-column label="用户类型" align="center" prop="buyUserType" />
      <el-table-column label="买方绑定用户" align="center" prop="buyBindUserId" />
      <el-table-column label="购买的steamId" align="center" prop="buySteamId" />
      <el-table-column label=" 收货方的Steam交易链接" align="center" prop="buyTradeLinks" />
      <el-table-column label="卖家用户ID" align="center" prop="sellUserId" />
      <el-table-column label="卖家用户类型" align="center" prop="sellUserType" />
      <el-table-column label="卖方绑定用户ID" align="center" prop="sellBindUserId" />
      <el-table-column label="卖家steamId" align="center" prop="sellSteamId" />
      <el-table-column label="订单号，内容生成" align="center" prop="orderNo" />
      <el-table-column label="商户订单号" align="center" prop="merchantNo" />
      <el-table-column label="商品模版ID" align="center" prop="commodityTemplateId" />
      <el-table-column label="实际商品ID" align="center" prop="realCommodityId" />
      <el-table-column label="商品ID" align="center" prop="commodityId" />
      <el-table-column label="极速发货购买模式0：优先购买极速发货；1：只购买极速发货" align="center" prop="fastShipping" />
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" />
      <el-table-column label="支付成功的支付渠道" align="center" prop="payChannelCode" />
      <el-table-column label="订单支付时间" align="center" prop="payTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格，单位：分 " align="center" prop="payAmount" />
      <el-table-column label="是否已支付：[0:未支付 1:已经支付过]" align="center" prop="payStatus" />
      <el-table-column label="订单支付状态" align="center" prop="payOrderStatus" />
      <el-table-column label="退款订单编号" align="center" prop="payRefundId" />
      <el-table-column label="退款金额，单位：分" align="center" prop="refundPrice" />
      <el-table-column label="退款时间" align="center" prop="refundTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.refundTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="提现手续费收款人类型" align="center" prop="serviceFeeUserType" />
      <el-table-column label="服务费，单位分" align="center" prop="serviceFee" />
      <el-table-column label="商户订单号" align="center" prop="merchantOrderNo" />
      <el-table-column label="模板hashname" align="center" prop="commodityHashName" />
      <el-table-column label="违约付款接口返回" align="center" prop="transferDamagesRet" />
      <el-table-column label="收款状态" align="center" prop="sellCashStatus" />
      <el-table-column label="发货状态" align="center" prop="transferStatus" />
      <el-table-column label="交易失败时退还" align="center" prop="transferRefundAmount" />
      <el-table-column label="服务费率" align="center" prop="serviceFeeRate" />
      <el-table-column label="提现手续费收款钱包" align="center" prop="serviceFeeUserId" />
      <el-table-column label="交易违约判定时间" align="center" prop="transferDamagesTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.transferDamagesTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" align="center" prop="marketName" />
      <el-table-column label="发货信息 json" align="center" prop="transferText" />
      <el-table-column label="购买最高价,单价元" align="center" prop="purchasePrice" />
      <el-table-column label="转帐接口返回" align="center" prop="serviceFeeRet" />
      <el-table-column label="发货模式 0,卖家直发；1,极速发货" align="center" prop="shippingMode" />
      <el-table-column label="有品商户订单号" align="center" prop="uuMerchantOrderNo" />
      <el-table-column label="购买用户编号。" align="center" prop="uuBuyerUserId" />
      <el-table-column label="订单失败原因编号。" align="center" prop="uuFailCode" />
      <el-table-column label="订单失败原因提示信息。" align="center" prop="uuFailReason" />
      <el-table-column label="有品订单号" align="center" prop="uuOrderNo" />
      <el-table-column label="通知类型描述(等待发货，等待收货，购买成功，订单取消)。" align="center" prop="uuNotifyDesc" />
      <el-table-column label="通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)。" align="center" prop="uuNotifyType" />
      <el-table-column label="交易状态 0,成功；2,失败。" align="center" prop="uuOrderStatus" />
      <el-table-column label="订单小状态。" align="center" prop="uuOrderSubStatus" />
      <el-table-column label="预留字段" align="center" prop="uuOrderSubType" />
      <el-table-column label="预留字段" align="center" prop="uuOrderType" />
      <el-table-column label="发货模式：0,卖家直发；1,极速发货" align="center" prop="uuShippingMode" />
      <el-table-column label="报价ID" align="center" prop="uuTradeOfferId" />
      <el-table-column label="报价链接" align="center" prop="uuTradeOfferLinks" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:youyou-order:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:youyou-order:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <YouyouOrderForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as YouyouOrderApi from '@/api/steam/youyouorder';
import YouyouOrderForm from './YouyouOrderForm.vue';
export default {
  name: "YouyouOrder",
  components: {
          YouyouOrderForm,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
              // 总条数
        total: 0,
      // steam订单列表
      list: [],
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 选中行
      currentRow: {},
      // 查询参数
      queryParams: {
                    pageNo: 1,
            pageSize: 10,
        createTime: [],
        buyUserId: null,
        buyUserType: null,
        buyBindUserId: null,
        buySteamId: null,
        buyTradeLinks: null,
        sellUserId: null,
        sellUserType: null,
        sellBindUserId: null,
        sellSteamId: null,
        orderNo: null,
        merchantNo: null,
        commodityTemplateId: null,
        realCommodityId: null,
        commodityId: null,
        fastShipping: null,
        payOrderId: null,
        payChannelCode: null,
        payTime: [],
        payAmount: null,
        payStatus: null,
        payOrderStatus: null,
        payRefundId: null,
        refundPrice: null,
        refundTime: [],
        serviceFeeUserType: null,
        serviceFee: null,
        merchantOrderNo: null,
        commodityHashName: null,
        transferDamagesRet: null,
        sellCashStatus: null,
        transferStatus: null,
        transferRefundAmount: null,
        serviceFeeRate: null,
        serviceFeeUserId: null,
        transferDamagesTime: [],
        marketName: null,
        transferText: null,
        purchasePrice: null,
        serviceFeeRet: null,
        shippingMode: null,
        uuMerchantOrderNo: null,
        uuBuyerUserId: null,
        uuFailCode: null,
        uuFailReason: null,
        uuOrderNo: null,
        uuNotifyDesc: null,
        uuNotifyType: null,
        uuOrderStatus: null,
        uuOrderSubStatus: null,
        uuOrderSubType: null,
        uuOrderType: null,
        uuShippingMode: null,
        uuTradeOfferId: null,
        uuTradeOfferLinks: null,
      },
            };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    async getList() {
      try {
      this.loading = true;
              const res = await YouyouOrderApi.getYouyouOrderPage(this.queryParams);
        this.list = res.data.list;
        this.total = res.data.total;
      } finally {
        this.loading = false;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 添加/修改操作 */
    openForm(id) {
      this.$refs["formRef"].open(id);
    },
    /** 删除按钮操作 */
    async handleDelete(row) {
      const id = row.id;
      await this.$modal.confirm('是否确认删除steam订单编号为"' + id + '"的数据项?')
      try {
       await YouyouOrderApi.deleteYouyouOrder(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有steam订单数据项?');
      try {
        this.exportLoading = true;
        const res = await YouyouOrderApi.exportYouyouOrderExcel(this.queryParams);
        this.$download.excel(res, 'steam订单.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>