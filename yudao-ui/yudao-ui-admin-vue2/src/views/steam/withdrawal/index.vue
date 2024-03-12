<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.USER_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="已支付" prop="payStatus">
        <el-select v-model="queryParams.payStatus" placeholder="请选择是否已支付[0未支付，1支付]" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_BOOLEAN_STRING)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="支付编号" prop="payOrderId">
        <el-input v-model="queryParams.payOrderId" placeholder="请输入支付订单编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付渠道" prop="payChannelCode">
        <el-input v-model="queryParams.payChannelCode" placeholder="请输入支付成功的支付渠道" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付时间" prop="payTime">
        <el-date-picker v-model="queryParams.payTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="退款编号" prop="payRefundId">
        <el-input v-model="queryParams.payRefundId" placeholder="请输入退款订单编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款金额(分)" prop="refundPrice">
        <el-input v-model="queryParams.refundPrice" placeholder="请输入退款金额，单位分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="退款时间" prop="refundTime">
        <el-date-picker v-model="queryParams.refundTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="提现金额" prop="withdrawalPrice">
        <el-input v-model="queryParams.withdrawalPrice" placeholder="请输入提现金额" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="提现信息" prop="withdrawalInfo">
        <el-input v-model="queryParams.withdrawalInfo" placeholder="请输入提现信息" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="服务费" prop="serviceFee">
        <el-input v-model="queryParams.serviceFee" placeholder="请输入服务费" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="费率" prop="serviceFeeRate">
        <el-input v-model="queryParams.serviceFeeRate" placeholder="请输入费率" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付金额" prop="paymentAmount">
        <el-input v-model="queryParams.paymentAmount" placeholder="请输入支付金额" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:withdrawal:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="用户类型" align="center" prop="userType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" />
      <el-table-column label="支付成功的支付渠道" align="center" prop="payChannelCode" />
      <el-table-column label="订单支付时间" align="center" prop="payTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款订单编号" align="center" prop="payRefundId" />
      <el-table-column label="退款金额，单位分" align="center" prop="refundPrice" />
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
      <el-table-column label="是否已支付" align="center" prop="payStatus" />
      <el-table-column label="提现金额" align="center" prop="withdrawalPrice" />
      <el-table-column label="提现信息" align="center" prop="withdrawalInfo" />
      <el-table-column label="服务费" align="center" prop="serviceFee" />
      <el-table-column label="费率" align="center" prop="serviceFeeRate" />
      <el-table-column label="支付金额" align="center" prop="paymentAmount" />
      <el-table-column label="审批状态" align="center" prop="auditStatus" />
      <el-table-column label="审核人" align="center" prop="auditUserId" />
      <el-table-column label="审核信息" align="center" prop="auditMsg" />
      <el-table-column label="提现手续费收款钱包" align="center" prop="serviceFeeUserId" />
      <el-table-column label="提现手续费收款人类型" align="center" prop="serviceFeeUserType" />
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template v-slot="scope">-->
<!--          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"-->
<!--                     v-hasPermi="['steam:withdrawal:update']">修改</el-button>-->
<!--          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"-->
<!--                     v-hasPermi="['steam:withdrawal:delete']">删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <WithdrawalForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as WithdrawalApi from '@/api/steam/withdrawal';
import WithdrawalForm from './WithdrawalForm.vue';
export default {
  name: "Withdrawal",
  components: {
          WithdrawalForm,
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
      // 提现列表
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
        userType: null,
        payOrderId: null,
        payChannelCode: null,
        payTime: [],
        payRefundId: null,
        refundPrice: null,
        refundTime: [],
        createTime: [],
        payStatus: null,
        withdrawalPrice: null,
        withdrawalInfo: null,
        serviceFee: null,
        serviceFeeRate: null,
        paymentAmount: null,
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
              const res = await WithdrawalApi.getWithdrawalPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除提现编号为"' + id + '"的数据项?')
      try {
       await WithdrawalApi.deleteWithdrawal(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有提现数据项?');
      try {
        this.exportLoading = true;
        const res = await WithdrawalApi.exportWithdrawalExcel(this.queryParams);
        this.$download.excel(res, '提现.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>
