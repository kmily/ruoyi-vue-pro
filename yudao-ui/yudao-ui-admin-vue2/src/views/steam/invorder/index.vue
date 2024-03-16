<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户编号" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买的steamId" prop="steamId">
        <el-input v-model="queryParams.steamId" placeholder="请输入购买的steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="是否支付" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择是否支付：[0:未支付 1:已经支付过]" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付订号" prop="payOrderId">
        <el-input v-model="queryParams.payOrderId" placeholder="请输入支付订号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付渠道" prop="payChannelCode">
        <el-input v-model="queryParams.payChannelCode" placeholder="请输入支付渠道" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付时间" prop="payTime">
        <el-date-picker v-model="queryParams.payTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="退款单号" prop="payRefundId">
        <el-input v-model="queryParams.payRefundId" placeholder="请输入退订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款金额 分" prop="refundAmount">
        <el-input v-model="queryParams.refundAmount" placeholder="请输入退款金额 分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款时间" prop="refundTime">
        <el-date-picker v-model="queryParams.refundTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="价格，单位：分" prop="paymentAmount">
        <el-input v-model="queryParams.paymentAmount" placeholder="请输入价格，单位：分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单支付状态" prop="payOrderStatus">
        <el-select v-model="queryParams.payOrderStatus" placeholder="请选择订单支付状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="服务费，单位分" prop="serviceFee">
        <el-input v-model="queryParams.serviceFee" placeholder="请输入服务费，单位分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="服务费率" prop="serviceFeeRate">
        <el-input v-model="queryParams.serviceFeeRate" placeholder="请输入服务费率" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="优惠金额 分" prop="discountAmount">
        <el-input v-model="queryParams.discountAmount" placeholder="请输入优惠金额 分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货状态" prop="transferStatus">
        <el-select v-model="queryParams.transferStatus" placeholder="请选择发货状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="sellId" prop="sellId">
        <el-input v-model="queryParams.sellId" placeholder="请输入库存表ID参考steam_sell" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品描述ID" prop="invDescId">
        <el-input v-model="queryParams.invDescId" placeholder="请输入商品描述ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="库存表ID" prop="invId">
        <el-input v-model="queryParams.invId" placeholder="请输入库存表ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="卖家用户类型" prop="sellUserType">
        <el-select v-model="queryParams.sellUserType" placeholder="请选择卖家用户类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="卖家ID" prop="sellUserId">
        <el-input v-model="queryParams.sellUserId" placeholder="请输入卖家ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="卖家金额状态" prop="sellCashStatus">
        <el-select v-model="queryParams.sellCashStatus" placeholder="请选择卖家金额状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="商品总额" prop="commodityAmount">
        <el-input v-model="queryParams.commodityAmount" placeholder="请输入商品总额" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="提现手续费收款钱包" prop="serviceFeeUserId">
        <el-input v-model="queryParams.serviceFeeUserId" placeholder="请输入提现手续费收款钱包" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="提现手续费收款人类型" prop="serviceFeeUserType">
        <el-select v-model="queryParams.serviceFeeUserType" placeholder="请选择提现手续费收款人类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="转帐接口返回" prop="serviceFeeRet">
        <el-input v-model="queryParams.serviceFeeRet" placeholder="请输入转帐接口返回" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买平台" prop="platformName">
        <el-input v-model="queryParams.platformName" placeholder="请输入购买平台" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买平台代码" prop="platformCode">
        <el-input v-model="queryParams.platformCode" placeholder="请输入购买平台代码" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商户订单号" prop="merchantNo">
        <el-input v-model="queryParams.merchantNo" placeholder="请输入商户订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="交易失败时退还" prop="transferRefundAmount">
        <el-input v-model="queryParams.transferRefundAmount" placeholder="请输入交易失败时退还" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="交易违约金" prop="transferDamagesAmount">
        <el-input v-model="queryParams.transferDamagesAmount" placeholder="请输入交易违约金" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="交易违约判定时间" prop="transferDamagesTime">
        <el-date-picker v-model="queryParams.transferDamagesTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
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
                   v-hasPermi="['steam:inv-order:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:inv-order:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="订单编号" align="center" prop="id" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="购买的steamId" align="center" prop="steamId" />
      <el-table-column label="是否已支付" align="center" prop="payStatus" />
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" />
      <el-table-column label="支付成功的支付渠道" align="center" prop="payChannelCode" />
      <el-table-column label="订单支付时间" align="center" prop="payTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款订单编号" align="center" prop="payRefundId" />
      <el-table-column label="退款金额，单位：分" align="center" prop="refundAmount" />
      <el-table-column label="退款时间" align="center" prop="refundTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.refundTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格，单位：分" align="center" prop="paymentAmount" />
      <el-table-column label="用户类型" align="center" prop="userType" />
      <el-table-column label="订单支付状态" align="center" prop="payOrderStatus" />
      <el-table-column label="服务费，单位分" align="center" prop="serviceFee" />
      <el-table-column label="服务费率" align="center" prop="serviceFeeRate" />
      <el-table-column label="优惠金额 分" align="center" prop="discountAmount" />
      <el-table-column label="发货信息 json" align="center" prop="transferText" />
      <el-table-column label="发货状态" align="center" prop="transferStatus" />
      <el-table-column label="库存表ID参考steam_sell" align="center" prop="sellId" />
      <el-table-column label="商品描述ID" align="center" prop="invDescId" />
      <el-table-column label="库存表ID" align="center" prop="invId" />
      <el-table-column label="卖家用户类型" align="center" prop="sellUserType" />
      <el-table-column label="卖家ID" align="center" prop="sellUserId" />
      <el-table-column label="卖家金额状态" align="center" prop="sellCashStatus" />
      <el-table-column label="商品总额" align="center" prop="commodityAmount" />
      <el-table-column label="提现手续费收款钱包" align="center" prop="serviceFeeUserId" />
      <el-table-column label="提现手续费收款人类型" align="center" prop="serviceFeeUserType" />
      <el-table-column label="转帐接口返回" align="center" prop="serviceFeeRet" />
      <el-table-column label="购买平台" align="center" prop="platformName" />
      <el-table-column label="购买平台代码" align="center" prop="platformCode" />
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="商户订单号" align="center" prop="merchantNo" />
      <el-table-column label="交易失败时退还" align="center" prop="transferRefundAmount" />
      <el-table-column label="交易违约金" align="center" prop="transferDamagesAmount" />
      <el-table-column label="交易违约判定时间" align="center" prop="transferDamagesTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.transferDamagesTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:inv-order:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:inv-order:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <InvOrderForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as InvOrderApi from '@/api/steam/invorder';
import InvOrderForm from './InvOrderForm.vue';
export default {
  name: "InvOrder",
  components: {
          InvOrderForm,
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
        userId: null,
        steamId: null,
        payStatus: null,
        payOrderId: null,
        payChannelCode: null,
        payTime: [],
        payRefundId: null,
        refundAmount: null,
        refundTime: [],
        createTime: [],
        paymentAmount: null,
        userType: null,
        payOrderStatus: null,
        serviceFee: null,
        serviceFeeRate: null,
        discountAmount: null,
        transferText: null,
        transferStatus: null,
        sellId: null,
        invDescId: null,
        invId: null,
        sellUserType: null,
        sellUserId: null,
        sellCashStatus: null,
        commodityAmount: null,
        serviceFeeUserId: null,
        serviceFeeUserType: null,
        serviceFeeRet: null,
        platformName: null,
        platformCode: null,
        orderNo: null,
        merchantNo: null,
        transferRefundAmount: null,
        transferDamagesAmount: null,
        transferDamagesTime: [],
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
              const res = await InvOrderApi.getInvOrderPage(this.queryParams);
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
       await InvOrderApi.deleteInvOrder(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有steam订单数据项?');
      try {
        this.exportLoading = true;
        const res = await InvOrderApi.exportInvOrderExcel(this.queryParams);
        this.$download.excel(res, 'steam订单.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>
