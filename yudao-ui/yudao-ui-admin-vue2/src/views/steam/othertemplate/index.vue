<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出售平台id" prop="platformIdentity">
        <el-input v-model="queryParams.platformIdentity" placeholder="请输入出售平台id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="外观" prop="exterior">
        <el-input v-model="queryParams.exterior" placeholder="请输入外观" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="外观名称" prop="exteriorName">
        <el-input v-model="queryParams.exteriorName" placeholder="请输入外观名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="饰品id" prop="itemId">
        <el-input v-model="queryParams.itemId" placeholder="请输入饰品id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="饰品名称" prop="itemName">
        <el-input v-model="queryParams.itemName" placeholder="请输入饰品名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="marketHashName" prop="marketHashName">
        <el-input v-model="queryParams.marketHashName" placeholder="请输入marketHashName" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="自动发货在售最低价" prop="autoDeliverPrice">
        <el-input v-model="queryParams.autoDeliverPrice" placeholder="请输入自动发货在售最低价" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="品质" prop="quality">
        <el-input v-model="queryParams.quality" placeholder="请输入品质" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="稀有度" prop="rarity">
        <el-input v-model="queryParams.rarity" placeholder="请输入稀有度" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="steam类型" prop="type">
        <el-input v-model="queryParams.type" placeholder="请输入steam类型" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="图片地址" prop="imageUrl">
        <el-input v-model="queryParams.imageUrl" placeholder="请输入图片地址" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="自动发货在售数量" prop="autoDeliverQuantity">
        <el-input v-model="queryParams.autoDeliverQuantity" placeholder="请输入自动发货在售数量" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="品质颜色" prop="qualityColor">
        <el-input v-model="queryParams.qualityColor" placeholder="请输入品质颜色" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="品质名称" prop="qualityName">
        <el-input v-model="queryParams.qualityName" placeholder="请输入品质名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="稀有度颜色" prop="rarityColor">
        <el-input v-model="queryParams.rarityColor" placeholder="请输入稀有度颜色" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="稀有度名称" prop="rarityName">
        <el-input v-model="queryParams.rarityName" placeholder="请输入稀有度名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="短名称，去掉前缀" prop="shortName">
        <el-input v-model="queryParams.shortName" placeholder="请输入短名称，去掉前缀" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="steam类型名称" prop="typeName">
        <el-input v-model="queryParams.typeName" placeholder="请输入steam类型名称" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:other-template:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:other-template:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="出售平台id" align="center" prop="platformIdentity" />
      <el-table-column label="外观" align="center" prop="exterior" />
      <el-table-column label="外观名称" align="center" prop="exteriorName" />
      <el-table-column label="饰品id" align="center" prop="itemId" />
      <el-table-column label="饰品名称" align="center" prop="itemName" />
      <el-table-column label="marketHashName" align="center" prop="marketHashName" />
      <el-table-column label="自动发货在售最低价" align="center" prop="autoDeliverPrice" />
      <el-table-column label="品质" align="center" prop="quality" />
      <el-table-column label="稀有度" align="center" prop="rarity" />
      <el-table-column label="steam类型" align="center" prop="type" />
      <el-table-column label="图片地址" align="center" prop="imageUrl" />
      <el-table-column label="自动发货在售数量" align="center" prop="autoDeliverQuantity" />
      <el-table-column label="品质颜色" align="center" prop="qualityColor" />
      <el-table-column label="品质名称" align="center" prop="qualityName" />
      <el-table-column label="稀有度颜色" align="center" prop="rarityColor" />
      <el-table-column label="稀有度名称" align="center" prop="rarityName" />
      <el-table-column label="短名称，去掉前缀" align="center" prop="shortName" />
      <el-table-column label="steam类型名称" align="center" prop="typeName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:other-template:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:other-template:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <OtherTemplateForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as OtherTemplateApi from '@/api/steam/othertemplate';
import OtherTemplateForm from './OtherTemplateForm.vue';
export default {
  name: "OtherTemplate",
  components: {
          OtherTemplateForm,
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
      // 其他平台模板列表
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
        platformIdentity: null,
        exterior: null,
        exteriorName: null,
        itemId: null,
        itemName: null,
        marketHashName: null,
        autoDeliverPrice: null,
        quality: null,
        rarity: null,
        type: null,
        imageUrl: null,
        autoDeliverQuantity: null,
        qualityColor: null,
        qualityName: null,
        rarityColor: null,
        rarityName: null,
        shortName: null,
        typeName: null,
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
              const res = await OtherTemplateApi.getOtherTemplatePage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除其他平台模板编号为"' + id + '"的数据项?')
      try {
       await OtherTemplateApi.deleteOtherTemplate(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有其他平台模板数据项?');
      try {
        this.exportLoading = true;
        const res = await OtherTemplateApi.exportOtherTemplateExcel(this.queryParams);
        this.$download.excel(res, '其他平台模板.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>