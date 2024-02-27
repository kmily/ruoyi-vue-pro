<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="price" prop="price">
        <el-input v-model="queryParams.price" placeholder="请输入price" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="quantity" prop="quantity">
        <el-input v-model="queryParams.quantity" placeholder="请输入quantity" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="deals" prop="deals">
        <el-input v-model="queryParams.deals" placeholder="请输入deals" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="item_id" prop="itemId">
        <el-input v-model="queryParams.itemId" placeholder="请输入item_id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="app_id" prop="appId">
        <el-input v-model="queryParams.appId" placeholder="请输入app_id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="itemName" prop="itemName">
        <el-input v-model="queryParams.itemName" placeholder="请输入itemName" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="shortName" prop="shortName">
        <el-input v-model="queryParams.shortName" placeholder="请输入shortName" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="marketHashName" prop="marketHashName">
        <el-input v-model="queryParams.marketHashName" placeholder="请输入marketHashName" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="imageUrl" prop="imageUrl">
        <el-input v-model="queryParams.imageUrl" placeholder="请输入imageUrl" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="itemInfo" prop="itemInfo">
        <el-input v-model="queryParams.itemInfo" placeholder="请输入itemInfo" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="sellType" prop="sellType">
        <el-input v-model="queryParams.sellType" placeholder="请输入sellType" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="currencyId" prop="currencyId">
        <el-input v-model="queryParams.currencyId" placeholder="请输入currencyId" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="cnyPrice" prop="cnyPrice">
        <el-input v-model="queryParams.cnyPrice" placeholder="请输入cnyPrice" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="salePrice" prop="salePrice">
        <el-input v-model="queryParams.salePrice" placeholder="请输入salePrice" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="subsidyPrice" prop="subsidyPrice">
        <el-input v-model="queryParams.subsidyPrice" placeholder="请输入subsidyPrice" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="activityTag" prop="activityTag">
        <el-input v-model="queryParams.activityTag" placeholder="请输入activityTag" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="tagList" prop="tagList">
        <el-input v-model="queryParams.tagList" placeholder="请输入tagList" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="subsidyTag" prop="subsidyTag">
        <el-input v-model="queryParams.subsidyTag" placeholder="请输入subsidyTag" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="自动发货价格" prop="autoPrice">
        <el-input v-model="queryParams.autoPrice" placeholder="请输入自动发货价格" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="自动发货数量" prop="autoQuantity">
        <el-input v-model="queryParams.autoQuantity" placeholder="请输入自动发货数量" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="参考价" prop="referencePrice">
        <el-input v-model="queryParams.referencePrice" placeholder="请输入参考价" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:inv-preview:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:inv-preview:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="price" align="center" prop="price" />
      <el-table-column label="quantity" align="center" prop="quantity" />
      <el-table-column label="deals" align="center" prop="deals" />
      <el-table-column label="item_id" align="center" prop="itemId" />
      <el-table-column label="app_id" align="center" prop="appId" />
      <el-table-column label="itemName" align="center" prop="itemName" />
      <el-table-column label="shortName" align="center" prop="shortName" />
      <el-table-column label="marketHashName" align="center" prop="marketHashName" />
      <el-table-column label="imageUrl" align="center" prop="imageUrl" />
      <el-table-column label="itemInfo" align="center" prop="itemInfo" />
      <el-table-column label="sellType" align="center" prop="sellType" />
      <el-table-column label="currencyId" align="center" prop="currencyId" />
      <el-table-column label="cnyPrice" align="center" prop="cnyPrice" />
      <el-table-column label="salePrice" align="center" prop="salePrice" />
      <el-table-column label="subsidyPrice" align="center" prop="subsidyPrice" />
      <el-table-column label="activityTag" align="center" prop="activityTag" />
      <el-table-column label="tagList" align="center" prop="tagList" />
      <el-table-column label="subsidyTag" align="center" prop="subsidyTag" />
      <el-table-column label="自动发货价格" align="center" prop="autoPrice" />
      <el-table-column label="自动发货数量" align="center" prop="autoQuantity" />
      <el-table-column label="参考价" align="center" prop="referencePrice" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:inv-preview:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:inv-preview:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <InvPreviewForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as InvPreviewApi from '@/api/steam/invpreview';
import InvPreviewForm from './InvPreviewForm.vue';
export default {
  name: "InvPreview",
  components: {
          InvPreviewForm,
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
      // 饰品在售预览列表
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
        price: null,
        quantity: null,
        deals: null,
        itemId: null,
        appId: null,
        itemName: null,
        shortName: null,
        marketHashName: null,
        imageUrl: null,
        itemInfo: null,
        sellType: null,
        currencyId: null,
        cnyPrice: null,
        salePrice: null,
        subsidyPrice: null,
        activityTag: null,
        tagList: null,
        subsidyTag: null,
        autoPrice: null,
        autoQuantity: null,
        referencePrice: null,
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
              const res = await InvPreviewApi.getInvPreviewPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除饰品在售预览编号为"' + id + '"的数据项?')
      try {
       await InvPreviewApi.deleteInvPreview(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有饰品在售预览数据项?');
      try {
        this.exportLoading = true;
        const res = await InvPreviewApi.exportInvPreviewExcel(this.queryParams);
        this.$download.excel(res, '饰品在售预览.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>