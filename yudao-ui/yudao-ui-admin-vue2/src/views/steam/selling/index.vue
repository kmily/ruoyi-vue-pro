<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="csgoid" prop="appid">
        <el-input v-model="queryParams.appid" placeholder="请输入csgoid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="资产id(饰品唯一)" prop="assetid">
        <el-input v-model="queryParams.assetid" placeholder="请输入资产id(饰品唯一)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="classid" prop="classid">
        <el-input v-model="queryParams.classid" placeholder="请输入classid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="instanceid" prop="instanceid">
        <el-input v-model="queryParams.instanceid" placeholder="请输入instanceid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="amount" prop="amount">
        <el-input v-model="queryParams.amount" placeholder="请输入amount" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="steamId" prop="steamId">
        <el-input v-model="queryParams.steamId" placeholder="请输入steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="出售价格单价分" prop="price">
        <el-input v-model="queryParams.price" placeholder="请输入出售价格单价分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="平台用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入平台用户ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户类型(前后台用户)" prop="userType">
        <el-input v-model="queryParams.userType" placeholder="请输入用户类型(前后台用户)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="绑定用户ID" prop="bindUserId">
        <el-input v-model="queryParams.bindUserId" placeholder="请输入绑定用户ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="contextid" prop="contextid">
        <el-input v-model="queryParams.contextid" placeholder="请输入contextid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="inv_desc_id" prop="invDescId">
        <el-input v-model="queryParams.invDescId" placeholder="请输入inv_desc_id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货状态(0代表未出售，1代表出售中，2代表已出售 )" prop="transferStatus">
        <el-select v-model="queryParams.transferStatus" placeholder="请选择发货状态(0代表未出售，1代表出售中，2代表已出售 )" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
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
                   v-hasPermi="['steam:selling:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:selling:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="Primary Key" align="center" prop="id" />
      <el-table-column label="csgoid" align="center" prop="appid" />
      <el-table-column label="资产id(饰品唯一)" align="center" prop="assetid" />
      <el-table-column label="classid" align="center" prop="classid" />
      <el-table-column label="instanceid" align="center" prop="instanceid" />
      <el-table-column label="amount" align="center" prop="amount" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="steamId" align="center" prop="steamId" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="出售价格单价分" align="center" prop="price" />
      <el-table-column label="平台用户ID" align="center" prop="userId" />
      <el-table-column label="用户类型(前后台用户)" align="center" prop="userType" />
      <el-table-column label="绑定用户ID" align="center" prop="bindUserId" />
      <el-table-column label="contextid" align="center" prop="contextid" />
      <el-table-column label="inv_desc_id" align="center" prop="invDescId" />
      <el-table-column label="发货状态(0代表未出售，1代表出售中，2代表已出售 )" align="center" prop="transferStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:selling:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:selling:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <SellingForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as SellingApi from '@/api/steam/selling';
import SellingForm from './SellingForm.vue';
export default {
  name: "Selling",
  components: {
          SellingForm,
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
      // 在售饰品列表
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
        appid: null,
        assetid: null,
        classid: null,
        instanceid: null,
        amount: null,
        createTime: [],
        steamId: null,
        status: null,
        price: null,
        userId: null,
        userType: null,
        bindUserId: null,
        contextid: null,
        invDescId: null,
        transferStatus: null,
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
              const res = await SellingApi.getSellingPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除在售饰品编号为"' + id + '"的数据项?')
      try {
       await SellingApi.deleteSelling(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有在售饰品数据项?');
      try {
        this.exportLoading = true;
        const res = await SellingApi.exportSellingExcel(this.queryParams);
        this.$download.excel(res, '在售饰品.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>