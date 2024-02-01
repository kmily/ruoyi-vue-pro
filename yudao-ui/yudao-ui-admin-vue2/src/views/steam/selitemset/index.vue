<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="父级编号" prop="parentId">
        <el-input v-model="queryParams.parentId" placeholder="请输入父级编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="英文名称" prop="internalName">
        <el-input v-model="queryParams.internalName" placeholder="请输入英文名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="中文名称" prop="localizedTagName">
        <el-input v-model="queryParams.localizedTagName" placeholder="请输入中文名称" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:sel-itemset:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:sel-itemset:export']">导出</el-button>
      </el-col>
                  <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">
              展开/折叠
            </el-button>
          </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table
        v-loading="loading"
        :data="list"
        :stripe="true"
        :show-overflow-tooltip="true"
        v-if="refreshTable"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
            <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="父级编号" align="center" prop="parentId" />
      <el-table-column label="英文名称" align="center" prop="internalName" />
      <el-table-column label="中文名称" align="center" prop="localizedTagName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:sel-itemset:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:sel-itemset:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 对话框(添加 / 修改) -->
    <SelItemsetForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as SelItemsetApi from '@/api/steam/selitemset';
import SelItemsetForm from './SelItemsetForm.vue';
export default {
  name: "SelItemset",
  components: {
          SelItemsetForm,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
            // 收藏品选择列表
      list: [],
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 选中行
      currentRow: {},
      // 查询参数
      queryParams: {
                parentId: null,
        internalName: null,
        localizedTagName: null,
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
             const res = await SelItemsetApi.getSelItemsetList(this.queryParams);
       this.list = this.handleTree(res.data, 'id', 'parentId');
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
      await this.$modal.confirm('是否确认删除收藏品选择编号为"' + id + '"的数据项?')
      try {
       await SelItemsetApi.deleteSelItemset(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有收藏品选择数据项?');
      try {
        this.exportLoading = true;
        const res = await SelItemsetApi.exportSelItemsetExcel(this.queryParams);
        this.$download.excel(res, '收藏品选择.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
                    /** 展开/折叠操作 */
        toggleExpandAll() {
          this.refreshTable = false
          this.isExpandAll = !this.isExpandAll
          this.$nextTick(function () {
            this.refreshTable = true
          })
        }
  }
};
</script>