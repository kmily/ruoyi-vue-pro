<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="用户ID" prop="systemUserId">
        <el-input v-model="queryParams.systemUserId" placeholder="请输入用户ID" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户类型" prop="systemUserType">
        <el-select v-model="queryParams.systemUserType" placeholder="请选择用户类型" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="isv业务系统的申请单id" prop="isvBizId">
        <el-input v-model="queryParams.isvBizId" placeholder="请输入isv业务系统的申请单id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。" prop="appAuthToken">
        <el-input v-model="queryParams.appAuthToken" placeholder="请输入协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付宝分配给开发者的应用Id" prop="appId">
        <el-input v-model="queryParams.appId" placeholder="请输入支付宝分配给开发者的应用Id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付宝分配给商户的应用Id" prop="authAppId">
        <el-input v-model="queryParams.authAppId" placeholder="请输入支付宝分配给商户的应用Id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="被授权用户id" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入被授权用户id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商家支付宝账号对应的ID，2088开头" prop="merchantPid">
        <el-input v-model="queryParams.merchantPid" placeholder="请输入商家支付宝账号对应的ID，2088开头" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="NONE：未签约，表示还没有签约该产品" prop="signStatus">
        <el-select v-model="queryParams.signStatus" placeholder="请选择NONE：未签约，表示还没有签约该产品" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="产品签约状态对象" prop="signStatusList">
        <el-input v-model="queryParams.signStatusList" placeholder="请输入产品签约状态对象" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:alipay-isv:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:alipay-isv:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户ID" align="center" prop="systemUserId" />
      <el-table-column label="用户类型" align="center" prop="systemUserType" />
      <el-table-column label="isv业务系统的申请单id" align="center" prop="isvBizId" />
      <el-table-column label="协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。" align="center" prop="appAuthToken" />
      <el-table-column label="支付宝分配给开发者的应用Id" align="center" prop="appId" />
      <el-table-column label="支付宝分配给商户的应用Id" align="center" prop="authAppId" />
      <el-table-column label="被授权用户id" align="center" prop="userId" />
      <el-table-column label="商家支付宝账号对应的ID，2088开头" align="center" prop="merchantPid" />
      <el-table-column label="NONE：未签约，表示还没有签约该产品" align="center" prop="signStatus" />
      <el-table-column label="产品签约状态对象" align="center" prop="signStatusList" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:alipay-isv:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:alipay-isv:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <AlipayIsvForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as AlipayIsvApi from '@/api/steam/alipayisv';
import AlipayIsvForm from './AlipayIsvForm.vue';
export default {
  name: "AlipayIsv",
  components: {
          AlipayIsvForm,
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
      // 签约ISV用户列表
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
        systemUserId: null,
        systemUserType: null,
        isvBizId: null,
        appAuthToken: null,
        appId: null,
        authAppId: null,
        userId: null,
        merchantPid: null,
        signStatus: null,
        signStatusList: null,
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
              const res = await AlipayIsvApi.getAlipayIsvPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除签约ISV用户编号为"' + id + '"的数据项?')
      try {
       await AlipayIsvApi.deleteAlipayIsv(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有签约ISV用户数据项?');
      try {
        this.exportLoading = true;
        const res = await AlipayIsvApi.exportAlipayIsvExcel(this.queryParams);
        this.$download.excel(res, '签约ISV用户.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>