<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="Primary Key" prop="id">
        <el-input v-model="queryParams.id" placeholder="请输入Primary Key" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="资产id(饰品唯一)" prop="assetid">
        <el-input v-model="queryParams.assetid" placeholder="请输入资产id(饰品唯一)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="steamId" prop="steamId">
        <el-input v-model="queryParams.steamId" placeholder="请输入steamId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="图片地址" prop="iconUrl">
        <el-input v-model="queryParams.iconUrl" placeholder="请输入图片地址" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="类别选择" prop="selQuality">
        <el-input v-model="queryParams.selQuality" placeholder="请输入类别选择" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="收藏品选择" prop="selItemset">
        <el-input v-model="queryParams.selItemset" placeholder="请输入收藏品选择" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="武器选择" prop="selWeapon">
        <el-input v-model="queryParams.selWeapon" placeholder="请输入武器选择" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="外观选择" prop="selExterior">
        <el-input v-model="queryParams.selExterior" placeholder="请输入外观选择" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="品质选择" prop="selRarity">
        <el-input v-model="queryParams.selRarity" placeholder="请输入品质选择" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="类型选择" prop="selType">
        <el-select v-model="queryParams.selType" placeholder="请选择类型选择" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="marketHashName" prop="marketHashName">
        <el-input v-model="queryParams.marketHashName" placeholder="请输入marketHashName" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="在售展示权重" prop="displayWeight">
        <el-input v-model="queryParams.displayWeight" placeholder="请输入在售展示权重" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="itemInfo" prop="itemInfo">
        <el-input v-model="queryParams.itemInfo" placeholder="请输入itemInfo" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="short_name" prop="shortName">
        <el-input v-model="queryParams.shortName" placeholder="请输入short_name" clearable @keyup.enter.native="handleQuery"/>
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
      <el-table-column label="库存表id" align="center" prop="invId" />
      <el-table-column label="商品名称" align="center" prop="marketName" />
      <el-table-column label="图片地址" align="center" prop="iconUrl" />
      <el-table-column label="类别选择" align="center" prop="selQuality" />
      <el-table-column label="收藏品选择" align="center" prop="selItemset" />
      <el-table-column label="武器选择" align="center" prop="selWeapon" />
      <el-table-column label="外观选择" align="center" prop="selExterior" />
      <el-table-column label="品质选择" align="center" prop="selRarity" />
      <el-table-column label="类型选择" align="center" prop="selType" />
      <el-table-column label="marketHashName" align="center" prop="marketHashName" />
      <el-table-column label="在售展示权重" align="center" prop="displayWeight" />
      <el-table-column label="itemInfo" align="center" prop="itemInfo" />
      <el-table-column label="short_name" align="center" prop="shortName" />
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
        id: null,
        assetid: null,
        steamId: null,
        status: null,
        iconUrl: null,
        selQuality: null,
        selItemset: null,
        selWeapon: null,
        selExterior: null,
        selRarity: null,
        selType: null,
        marketHashName: null,
        displayWeight: null,
        itemInfo: null,
        shortName: null,
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
