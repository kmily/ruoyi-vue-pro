<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="是否已支付：[0:未支付 1:已经支付过]" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择是否已支付：[0:未支付 1:已经支付过]" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
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
      <el-form-item label="退款金额，单位：分" prop="refundPrice">
        <el-input v-model="queryParams.refundPrice" placeholder="请输入退款金额，单位：分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款订单编号" prop="payRefundId">
        <el-input v-model="queryParams.payRefundId" placeholder="请输入退款订单编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款时间" prop="refundTime">
        <el-date-picker v-model="queryParams.refundTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="发货信息 json" prop="transferText">
        <el-input v-model="queryParams.transferText" placeholder="请输入发货信息 json" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货状态" prop="transferStatus">
        <el-select v-model="queryParams.transferStatus" placeholder="请选择发货状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="商户订单号" prop="merchantOrderNo">
        <el-input v-model="queryParams.merchantOrderNo" placeholder="请输入商户订单号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单号，内容生成" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号，内容生成" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货模式 0,卖家直发；1,极速发货" prop="shippingMode">
        <el-input v-model="queryParams.shippingMode" placeholder="请输入发货模式 0,卖家直发；1,极速发货" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品模版ID" prop="commodityTemplateId">
        <el-input v-model="queryParams.commodityTemplateId" placeholder="请输入商品模版ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="模板hashname" prop="commodityHashName">
        <el-input v-model="queryParams.commodityHashName" placeholder="请输入模板hashname" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品ID" prop="commodityId">
        <el-input v-model="queryParams.commodityId" placeholder="请输入商品ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="购买最高价,单价元" prop="purchasePrice">
        <el-input v-model="queryParams.purchasePrice" placeholder="请输入购买最高价,单价元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="实际商品ID" prop="realCommodityId">
        <el-input v-model="queryParams.realCommodityId" placeholder="请输入实际商品ID" clearable @keyup.enter.native="handleQuery"/>
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
      <el-table-column label="价格，单位：分 " align="center" prop="payAmount" />
      <el-table-column label="是否已支付：[0:未支付 1:已经支付过]" align="center" prop="payStatus" />
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" />
      <el-table-column label="支付成功的支付渠道" align="center" prop="payChannelCode" />
      <el-table-column label="订单支付时间" align="center" prop="payTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款金额，单位：分" align="center" prop="refundPrice" />
      <el-table-column label="退款订单编号" align="center" prop="payRefundId" />
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
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="用户类型" align="center" prop="userType" />
      <el-table-column label="发货信息 json" align="center" prop="transferText" />
      <el-table-column label="发货状态" align="center" prop="transferStatus" />
      <el-table-column label="商户订单号" align="center" prop="merchantOrderNo" />
      <el-table-column label="订单号，内容生成" align="center" prop="orderNo" />
      <el-table-column label="发货模式 0,卖家直发；1,极速发货" align="center" prop="shippingMode" />
      <el-table-column label="商品模版ID" align="center" prop="commodityTemplateId" />
      <el-table-column label="模板hashname" align="center" prop="commodityHashName" />
      <el-table-column label="商品ID" align="center" prop="commodityId" />
      <el-table-column label="购买最高价,单价元" align="center" prop="purchasePrice" />
      <el-table-column label="实际商品ID" align="center" prop="realCommodityId" />
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
        payStatus: null,
        payOrderId: null,
        payChannelCode: null,
        payTime: [],
        refundPrice: null,
        payRefundId: null,
        refundTime: [],
        createTime: [],
        userType: null,
        transferText: null,
        transferStatus: null,
        merchantOrderNo: null,
        orderNo: null,
        shippingMode: null,
        commodityTemplateId: null,
        commodityHashName: null,
        commodityId: null,
        purchasePrice: null,
        realCommodityId: null,
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