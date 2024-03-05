<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品id" prop="id">
        <el-input v-model="queryParams.id" placeholder="请输入商品id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品模板id" prop="templateId">
        <el-input v-model="queryParams.templateId" placeholder="请输入商品模板id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品名称" prop="commodityName">
        <el-input v-model="queryParams.commodityName" placeholder="请输入商品名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="商品价格（单位元）" prop="commodityPrice">
        <el-input v-model="queryParams.commodityPrice" placeholder="请输入商品价格（单位元）" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发货状态" prop="transferStatus">
        <el-select v-model="queryParams.transferStatus" placeholder="请选择发货状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
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
                   v-hasPermi="['steam:youyou-commodity:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['steam:youyou-commodity:export']">导出</el-button>
      </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
            <el-table-column label="商品id" align="center" prop="id" />
      <el-table-column label="商品模板id" align="center" prop="templateId" />
      <el-table-column label="商品名称" align="center" prop="commodityName" />
      <el-table-column label="商品价格（单位元）" align="center" prop="commodityPrice" />
      <el-table-column label="商品磨损度" align="center" prop="commodityAbrade" />
      <el-table-column label="图案模板" align="center" prop="commodityPaintSeed" />
      <el-table-column label="皮肤编号" align="center" prop="commodityPaintIndex" />
      <el-table-column label="是否有名称标签：0否1是" align="center" prop="commodityHaveNameTag" />
      <el-table-column label="是否有布章：0否1是" align="center" prop="commodityHaveBuzhang" />
      <el-table-column label="是否有印花：0否1是" align="center" prop="commodityHaveSticker" />
      <el-table-column label="发货模式：0,卖家直发；1,极速发货" align="center" prop="shippingMode" />
      <el-table-column label="是否渐变色：0否1是" align="center" prop="templateisFade" />
      <el-table-column label="Integer	是否表面淬火：0否1是" align="center" prop="templateisHardened" />
      <el-table-column label="是否多普勒：0否1是" align="center" prop="templateisDoppler" />
      <el-table-column label="印花Id" align="center" prop="commodityStickersStickerId" />
      <el-table-column label="插槽编号" align="center" prop="commodityStickersRawIndex" />
      <el-table-column label="印花名称" align="center" prop="commodityStickersName" />
      <el-table-column label="唯一名称" align="center" prop="commodityStickersHashName" />
      <el-table-column label="材料" align="center" prop="commodityStickersMaterial" />
      <el-table-column label="图片链接地址" align="center" prop="commodityStickersImgUrl" />
      <el-table-column label="印花价格(单位元)" align="center" prop="commodityStickersPrice" />
      <el-table-column label="磨损值" align="center" prop="commodityStickersAbrade" />
      <el-table-column label="多普勒属性分类名称" align="center" prop="commodityDopplerTitle" />
      <el-table-column label="多普勒属性分类缩写" align="center" prop="commodityDopplerAbbrTitle" />
      <el-table-column label="多普勒属性显示颜色" align="center" prop="commodityDopplerColor" />
      <el-table-column label="渐变色属性属性名称" align="center" prop="commodityFadeTitle" />
      <el-table-column label="渐变色属性对应数值" align="center" prop="commodityFadeNumerialValue" />
      <el-table-column label="渐变色属性显示颜色" align="center" prop="commodityFadeColor" />
      <el-table-column label="表面淬火属性分类名称" align="center" prop="commodityHardenedTitle" />
      <el-table-column label="表面淬火属性分类缩写" align="center" prop="commodityHardenedAbbrTitle" />
      <el-table-column label="表面淬火属性显示颜色" align="center" prop="commodityHardenedColor" />
      <el-table-column label="发货状态" align="center" prop="transferStatus" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.id)"
                     v-hasPermi="['steam:youyou-commodity:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['steam:youyou-commodity:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <YouyouCommodityForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as YouyouCommodityApi from '@/api/steam/youyoucommodity';
import YouyouCommodityForm from './YouyouCommodityForm.vue';
export default {
  name: "YouyouCommodity",
  components: {
          YouyouCommodityForm,
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
      // 悠悠商品列表列表
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
        templateId: null,
        commodityName: null,
        commodityPrice: null,
        transferStatus: null,
        createTime: [],
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
              const res = await YouyouCommodityApi.getYouyouCommodityPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除悠悠商品列表编号为"' + id + '"的数据项?')
      try {
       await YouyouCommodityApi.deleteYouyouCommodity(id);
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有悠悠商品列表数据项?');
      try {
        this.exportLoading = true;
        const res = await YouyouCommodityApi.exportYouyouCommodityExcel(this.queryParams);
        this.$download.excel(res, '悠悠商品列表.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>