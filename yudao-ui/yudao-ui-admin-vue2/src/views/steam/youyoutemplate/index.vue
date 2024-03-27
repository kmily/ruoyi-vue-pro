<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="武器全称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入武器全称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="武器英文全称" prop="hashName">
        <el-input v-model="queryParams.hashName" placeholder="请输入武器英文全称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="类型编号" prop="typeId">
        <el-input v-model="queryParams.typeId" placeholder="请输入类型编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="类型名称" prop="typeName">
        <el-input v-model="queryParams.typeName" placeholder="请输入类型名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="类型英文名称" prop="typeHashName">
        <el-input v-model="queryParams.typeHashName" placeholder="请输入类型英文名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="武器编号" prop="weaponId">
        <el-input v-model="queryParams.weaponId" placeholder="请输入武器编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="武器名称" prop="weaponName">
        <el-input v-model="queryParams.weaponName" placeholder="请输入武器名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="武器英文名称" prop="weaponHashName">
        <el-input v-model="queryParams.weaponHashName" placeholder="请输入武器英文名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="图片地址" prop="iconUrl">
        <el-input v-model="queryParams.iconUrl" placeholder="请输入图片地址" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="在售最低价" prop="minSellPrice">
        <el-input v-model="queryParams.minSellPrice" placeholder="请输入在售最低价" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="极速发货在售最低价" prop="fastShippingMinSellPrice">
        <el-input v-model="queryParams.fastShippingMinSellPrice" placeholder="请输入极速发货在售最低价" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="模板参考价" prop="referencePrice">
        <el-input v-model="queryParams.referencePrice" placeholder="请输入模板参考价" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="外观" prop="exteriorName">
        <el-input v-model="queryParams.exteriorName" placeholder="请输入外观" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="稀有度" prop="rarityName">
        <el-input v-model="queryParams.rarityName" placeholder="请输入稀有度" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="品质" prop="qualityName">
        <el-input v-model="queryParams.qualityName" placeholder="请输入品质" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="在售数量" prop="sellNum">
        <el-input v-model="queryParams.sellNum" placeholder="请输入在售数量" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['steam:youyou-template:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:youyou-template:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="武器全称" align="center" prop="name" />
      <el-table-column label="武器英文全称" align="center" prop="hashName" />
      <el-table-column label="类型编号" align="center" prop="typeId" />
      <el-table-column label="类型名称" align="center" prop="typeName" />
      <el-table-column label="类型英文名称" align="center" prop="typeHashName" />
      <el-table-column label="武器编号" align="center" prop="weaponId" />
      <el-table-column label="武器名称" align="center" prop="weaponName" />
      <el-table-column label="武器英文名称" align="center" prop="weaponHashName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="图片地址" align="center" prop="iconUrl" />
      <el-table-column label="在售最低价" align="center" prop="minSellPrice" />
      <el-table-column label="极速发货在售最低价" align="center" prop="fastShippingMinSellPrice" />
      <el-table-column label="模板参考价" align="center" prop="referencePrice" />
      <el-table-column label="外观" align="center" prop="exteriorName" />
      <el-table-column label="稀有度" align="center" prop="rarityName" />
      <el-table-column label="品质" align="center" prop="qualityName" />
      <el-table-column label="模板ID" align="center" prop="templateId" />
      <el-table-column label="在售数量" align="center" prop="sellNum" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.templateId)"
                     v-hasPermi="['steam:youyou-template:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:youyou-template:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <YouyouTemplateForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as YouyouTemplateApi from '@/api/steam/youyoutemplate';
import YouyouTemplateForm from './YouyouTemplateForm.vue';
export default {
  name: "YouyouTemplate",
  components: {
          YouyouTemplateForm,
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
      // 悠悠商品模板列表
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
        name: null,
        hashName: null,
        typeId: null,
        typeName: null,
        typeHashName: null,
        weaponId: null,
        weaponName: null,
        weaponHashName: null,
        createTime: [],
        iconUrl: null,
        minSellPrice: null,
        fastShippingMinSellPrice: null,
        referencePrice: null,
        exteriorName: null,
        rarityName: null,
        qualityName: null,
        sellNum: null,
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
              const res = await YouyouTemplateApi.getYouyouTemplatePage(this.queryParams);
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
      const templateId = row.templateId;
      await this.$modal.confirm('是否确认删除悠悠商品模板编号为"' + templateId + '"的数据项?')
      try {
       await YouyouTemplateApi.deleteYouyouTemplate(templateId);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有悠悠商品模板数据项?');
      try {
        this.exportLoading = true;
        const res = await YouyouTemplateApi.exportYouyouTemplateExcel(this.queryParams);
        this.$download.excel(res, '悠悠商品模板.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>
