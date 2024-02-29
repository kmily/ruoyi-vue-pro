<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户编号" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="已支付" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择是否已支付：[0:未支付 1:已经支付过]" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_BOOLEAN_STRING)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="支付订单编号" prop="payOrderId">
        <el-input v-model="queryParams.payOrderId" placeholder="请输入支付订单编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="购买steamId" prop="steamId">
        <el-input v-model="queryParams.steamId" placeholder="请输入购买的steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="订单支付状态" prop="payOrderStatus">
        <el-select v-model="queryParams.payOrderStatus" placeholder="请选择订单支付状态" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.PAY_ORDER_STATUS)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="sellID" prop="sellId">
        <el-input v-model="queryParams.sellId" placeholder="请输入库存表ID参考steam_sell" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="卖家用户类型" prop="sellUserType">
        <el-select v-model="queryParams.sellUserType" placeholder="请选择卖家用户类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="卖家ID" prop="sellUserId">
        <el-input v-model="queryParams.sellUserId" placeholder="请输入卖家ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:inv-order:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="订单编号" align="center" prop="id" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="已支付" align="center" prop="payStatus">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.payStatus" />
        </template>
      </el-table-column>
      <el-table-column label="订单编号" align="center" prop="payOrderId" />
      <el-table-column label="支付渠道" align="center" prop="payChannelCode" />
      <el-table-column label="订单支付时间" align="center" prop="payTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款订单编号" align="center" prop="payRefundId" />
      <el-table-column label="退款金额(分)" align="center" prop="refundPrice" />
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
      <el-table-column label="价格(分)" align="center" prop="price" />
      <el-table-column label="用户类型" align="center" prop="userType" >
              <template v-slot="scope">
                <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
              </template>
      </el-table-column>
      <el-table-column label="购买的steamId" align="center" prop="steamId" />
      <el-table-column label="发货信息" align="center">
        <template v-slot="scope">
          {{scope.row.transferText.tradeofferid}}
        </template>
      </el-table-column>
              <el-table-column label="发货错误信息" align="center">
                <template v-slot="scope">
                  {{scope.row.transferText.msg}}
                </template>
              </el-table-column>
<!--      <el-table-column label="发货状态" align="center" prop="transferStatus" />-->
      <el-table-column label="订单支付状态" align="center" prop="payOrderStatus">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.PAY_ORDER_STATUS" :value="scope.row.payOrderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="库存表ID参考steam_sell" align="center" prop="sellId" />
      <el-table-column label="商品描述ID" align="center" prop="invDescId" />
      <el-table-column label="库存表ID" align="center" prop="invId" />
      <el-table-column label="卖家用户类型" align="center" prop="sellUserType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.sellUserType" />
        </template>
      </el-table-column>
      <el-table-column label="卖家ID" align="center" prop="sellUserId" />
      <el-table-column label="卖家金额状态" align="center" prop="sellCashStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button style="color: red" v-if="scope.row.payOrderStatus != 20 && scope.row.payStatus && !scope.row.transferText.tradeofferid" size="mini" type="text" @click="refundOrderClick(scope.row.id)"
                     v-hasPermi="['steam:inv-order:update']">退款</el-button>
          <el-button v-if="scope.row.payOrderStatus != 20 && scope.row.payStatus && !scope.row.transferText.tradeofferid" size="mini" type="text" @click="tradeAssetClick(scope.row.id)"
                     v-hasPermi="['steam:inv-order:delete']">人工发货</el-button>
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
        payStatus: null,
        payOrderId: null,
        userType: null,
        steamId: null,
        transferText: null,
        transferStatus: null,
        payOrderStatus: null,
        sellId: null,
        sellUserType: null,
        sellUserId: null,
        sellCashStatus: null,
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
