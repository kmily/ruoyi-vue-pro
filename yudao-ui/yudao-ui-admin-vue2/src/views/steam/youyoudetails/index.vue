<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="通过申请获取的AppKey" prop="appkey">
        <el-input v-model="queryParams.appkey" placeholder="请输入通过申请获取的AppKey" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="时间戳" prop="timestamp">
        <el-date-picker clearable v-model="queryParams.timestamp" type="date" value-format="yyyy-MM-dd" placeholder="选择时间戳" />
      </el-form-item>
      <el-form-item label="API输入参数签名结果" prop="sign">
        <el-input v-model="queryParams.sign" placeholder="请输入API输入参数签名结果" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="明细类型，1=订单明细，2=资金流水" prop="dataType">
        <el-input v-model="queryParams.dataType" placeholder="请输入明细类型，1=订单明细，2=资金流水" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker v-model="queryParams.startTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="queryParams.endTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="申请标识" prop="applyCode">
        <el-input v-model="queryParams.applyCode" placeholder="请输入申请标识" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="查询明细结果返回的url" prop="data">
        <el-input v-model="queryParams.data" placeholder="请输入查询明细结果返回的url" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:youyou-details:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:youyou-details:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="通过申请获取的AppKey" align="center" prop="appkey" />
      <el-table-column label="时间戳" align="center" prop="timestamp" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.timestamp) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="API输入参数签名结果" align="center" prop="sign" />
      <el-table-column label="明细类型，1=订单明细，2=资金流水" align="center" prop="dataType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.INFRA_CONFIG_TYPE" :value="scope.row.dataType" />
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请标识" align="center" prop="applyCode" />
      <el-table-column label="查询明细结果返回的url" align="center" prop="data" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:youyou-details:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:youyou-details:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <YouyouDetailsForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as YouyouDetailsApi from '@/api/steam/youyoudetails';
import YouyouDetailsForm from './YouyouDetailsForm.vue';
export default {
  name: "YouyouDetails",
  components: {
          YouyouDetailsForm,
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
      // 用户查询明细列表
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
        appkey: null,
        timestamp: null,
        sign: null,
        dataType: null,
        startTime: [],
        endTime: [],
        createTime: [],
        applyCode: null,
        data: null,
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
              const res = await YouyouDetailsApi.getYouyouDetailsPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除用户查询明细编号为"' + id + '"的数据项?')
      try {
       await YouyouDetailsApi.deleteYouyouDetails(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有用户查询明细数据项?');
      try {
        this.exportLoading = true;
        const res = await YouyouDetailsApi.exportYouyouDetailsExcel(this.queryParams);
        this.$download.excel(res, '用户查询明细.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>
