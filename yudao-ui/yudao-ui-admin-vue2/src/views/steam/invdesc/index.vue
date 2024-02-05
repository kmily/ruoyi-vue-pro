<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="appid" prop="appid">
        <el-input v-model="queryParams.appid" placeholder="请输入appid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="classid" prop="classid">
        <el-input v-model="queryParams.classid" placeholder="请输入classid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="instanceid" prop="instanceid">
        <el-input v-model="queryParams.instanceid" placeholder="请输入instanceid" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="currency" prop="currency">
        <el-input v-model="queryParams.currency" placeholder="请输入currency" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="background_color" prop="backgroundColor">
        <el-input v-model="queryParams.backgroundColor" placeholder="请输入background_color" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="tradable" prop="tradable">
        <el-input v-model="queryParams.tradable" placeholder="请输入tradable" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="actions" prop="actions">
        <el-input v-model="queryParams.actions" placeholder="请输入actions" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="fraudwarnings" prop="fraudwarnings">
        <el-input v-model="queryParams.fraudwarnings" placeholder="请输入fraudwarnings" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="name" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入name" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="name_color" prop="nameColor">
        <el-input v-model="queryParams.nameColor" placeholder="请输入name_color" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="type" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择type" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="market_name" prop="marketName">
        <el-input v-model="queryParams.marketName" placeholder="请输入market_name" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="market_hash_name" prop="marketHashName">
        <el-input v-model="queryParams.marketHashName" placeholder="请输入market_hash_name" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="market_actions" prop="marketActions">
        <el-input v-model="queryParams.marketActions" placeholder="请输入market_actions" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="commodity" prop="commodity">
        <el-input v-model="queryParams.commodity" placeholder="请输入commodity" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="market_tradable_restriction" prop="marketTradableRestriction">
        <el-input v-model="queryParams.marketTradableRestriction" placeholder="请输入market_tradable_restriction" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="marketable" prop="marketable">
        <el-input v-model="queryParams.marketable" placeholder="请输入marketable" clearable @keyup.enter.native="handleQuery"/>
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
      <el-form-item label="steamId" prop="steamId">
        <el-input v-model="queryParams.steamId" placeholder="请输入steamId" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:inv-desc:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:inv-desc:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="appid" align="center" prop="appid" />
      <el-table-column label="classid" align="center" prop="classid" />
      <el-table-column label="instanceid" align="center" prop="instanceid" />
      <el-table-column label="currency" align="center" prop="currency" />
      <el-table-column label="background_color" align="center" prop="backgroundColor" />
      <el-table-column label="tradable" align="center" prop="tradable" />
      <el-table-column label="actions" align="center" prop="actions" />
      <el-table-column label="fraudwarnings" align="center" prop="fraudwarnings" />
      <el-table-column label="name" align="center" prop="name" />
      <el-table-column label="name_color" align="center" prop="nameColor" />
      <el-table-column label="type" align="center" prop="type" />
      <el-table-column label="market_name" align="center" prop="marketName" />
      <el-table-column label="market_hash_name" align="center" prop="marketHashName" />
      <el-table-column label="market_actions" align="center" prop="marketActions" />
      <el-table-column label="commodity" align="center" prop="commodity" />
      <el-table-column label="market_tradable_restriction" align="center" prop="marketTradableRestriction" />
      <el-table-column label="marketable" align="center" prop="marketable" />
      <el-table-column label="类别选择" align="center" prop="selQuality" />
      <el-table-column label="收藏品选择" align="center" prop="selItemset" />
      <el-table-column label="武器选择" align="center" prop="selWeapon" />
      <el-table-column label="外观选择" align="center" prop="selExterior" />
      <el-table-column label="品质选择" align="center" prop="selRarity" />
      <el-table-column label="类型选择" align="center" prop="selType" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="steamId" align="center" prop="steamId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:inv-desc:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:inv-desc:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <InvDescForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as InvDescApi from '@/api/steam/invdesc';
import InvDescForm from './InvDescForm.vue';
export default {
  name: "InvDesc",
  components: {
          InvDescForm,
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
      // 库存信息详情列表
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
        classid: null,
        instanceid: null,
        currency: null,
        backgroundColor: null,
        tradable: null,
        actions: null,
        fraudwarnings: null,
        name: null,
        nameColor: null,
        type: null,
        marketName: null,
        marketHashName: null,
        marketActions: null,
        commodity: null,
        marketTradableRestriction: null,
        marketable: null,
        selQuality: null,
        selItemset: null,
        selWeapon: null,
        selExterior: null,
        selRarity: null,
        selType: null,
        steamId: null,
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
              const res = await InvDescApi.getInvDescPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除库存信息详情编号为"' + id + '"的数据项?')
      try {
       await InvDescApi.deleteInvDesc(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有库存信息详情数据项?');
      try {
        this.exportLoading = true;
        const res = await InvDescApi.exportInvDescExcel(this.queryParams);
        this.$download.excel(res, '库存信息详情.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>