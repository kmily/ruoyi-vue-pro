<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="appid" prop="appid">
        <el-input v-model="queryParams.appid" placeholder="请输入appid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="assetid" prop="assetid">
        <el-input v-model="queryParams.assetid" placeholder="请输入assetid" clearable @keyup.enter.native="handleQuery"/>
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
      <el-form-item label="steamId" prop="steamId">
        <el-input v-model="queryParams.steamId" placeholder="请输入steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="启用" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择启用" clearable size="small">
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
                   v-hasPermi="['steam:inv:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:inv:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="Primary Key" align="center" prop="id" />
      <el-table-column label="appid" align="center" prop="appid" />
      <el-table-column label="assetid" align="center" prop="assetid" />
      <el-table-column label="classid" align="center" prop="classid" />
      <el-table-column label="instanceid" align="center" prop="instanceid" />
      <el-table-column label="amount" align="center" prop="amount" />
      <el-table-column label="steamId" align="center" prop="steamId" />
      <el-table-column label="启用" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:inv:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:inv:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <InvForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as InvApi from '@/api/steam/inv';
import InvForm from './InvForm.vue';
export default {
  name: "Inv",
  components: {
          InvForm,
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
      // steam用户库存储列表
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
        steamId: null,
        status: null,
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
              const res = await InvApi.getInvPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除steam用户库存储编号为"' + id + '"的数据项?')
      try {
       await InvApi.deleteInv(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有steam用户库存储数据项?');
      try {
        this.exportLoading = true;
        const res = await InvApi.exportInvExcel(this.queryParams);
        this.$download.excel(res, 'steam用户库存储.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>