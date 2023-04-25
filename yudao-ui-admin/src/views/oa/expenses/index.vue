<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="报销类型" prop="expensesType">
        <el-select v-model="queryParams.expensesType" placeholder="请选择报销类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.OA_EXPENSE_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="展会名称" prop="exhibitName">
        <el-input v-model="queryParams.exhibitName" placeholder="请输入展会名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="审批状态" prop="approvalStatus">
        <el-select v-model="queryParams.approvalStatus" placeholder="请选择审批状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input v-model="queryParams.createBy" placeholder="请输入创建者" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['oa:expenses:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['oa:expenses:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="报销id" align="center" prop="id" />
      <el-table-column label="报销类型" align="center" prop="expensesType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.OA_EXPENSE_TYPE" :value="scope.row.expensesType" />
        </template>
      </el-table-column>
      <el-table-column label="展会名称" align="center" prop="exhibitName" />
      <el-table-column label="展会开始时间" align="center" prop="exhibitBeginDate" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.exhibitBeginDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="展会结束时间" align="center" prop="exhibitEndDate" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.exhibitEndDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="展会地点" align="center" prop="exhibitAddress" />
      <el-table-column label="关联的拜访过的客户" align="center" prop="customerId" />
      <el-table-column label="费用说明" align="center" prop="feeRemark" />
      <el-table-column label="报销总费用" align="center" prop="fee" />
      <el-table-column label="申请单状态" align="center" prop="status" />
      <el-table-column label="审批状态" align="center" prop="approvalStatus" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新者" align="center" prop="updateBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['oa:expenses:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['oa:expenses:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="报销类型" prop="expensesType">
          <el-select v-model="form.expensesType" placeholder="请选择报销类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.OA_EXPENSE_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="展会名称" prop="exhibitName">
          <el-input v-model="form.exhibitName" placeholder="请输入展会名称" />
        </el-form-item>
        <el-form-item label="展会开始时间" prop="exhibitBeginDate">
          <el-date-picker clearable v-model="form.exhibitBeginDate" type="date" value-format="timestamp" placeholder="选择展会开始时间" />
        </el-form-item>
        <el-form-item label="展会结束时间" prop="exhibitEndDate">
          <el-date-picker clearable v-model="form.exhibitEndDate" type="date" value-format="timestamp" placeholder="选择展会结束时间" />
        </el-form-item>
        <el-form-item label="展会地点" prop="exhibitAddress">
          <el-input v-model="form.exhibitAddress" placeholder="请输入展会地点" />
        </el-form-item>
        <el-form-item label="关联的拜访过的客户" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入关联的拜访过的客户" />
        </el-form-item>
        <el-form-item label="费用说明" prop="feeRemark">
          <el-input v-model="form.feeRemark" placeholder="请输入费用说明" />
        </el-form-item>
        <el-form-item label="报销总费用" prop="fee">
          <el-input v-model="form.fee" placeholder="请输入报销总费用" />
        </el-form-item>
        <el-form-item label="申请单状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-radio-group v-model="form.approvalStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="创建者" prop="createBy">
          <el-input v-model="form.createBy" placeholder="请输入创建者" />
        </el-form-item>
        <el-form-item label="更新者" prop="updateBy">
          <el-input v-model="form.updateBy" placeholder="请输入更新者" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createExpenses, updateExpenses, deleteExpenses, getExpenses, getExpensesPage, exportExpensesExcel } from "@/api/oa/expenses";

export default {
  name: "Expenses",
  components: {
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
      // 报销申请列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        expensesType: null,
        exhibitName: null,
        approvalStatus: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        expensesType: [{ required: true, message: "报销类型不能为空", trigger: "change" }],
        fee: [{ required: true, message: "报销总费用不能为空", trigger: "blur" }],
        status: [{ required: true, message: "申请单状态不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getExpensesPage(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        expensesType: undefined,
        exhibitName: undefined,
        exhibitBeginDate: undefined,
        exhibitEndDate: undefined,
        exhibitAddress: undefined,
        customerId: undefined,
        feeRemark: undefined,
        fee: undefined,
        status: undefined,
        approvalStatus: undefined,
        remark: undefined,
        createBy: undefined,
        updateBy: undefined,
      };
      this.resetForm("form");
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加报销申请";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getExpenses(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报销申请";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.id != null) {
          updateExpenses(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createExpenses(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除报销申请编号为"' + id + '"的数据项?').then(function() {
          return deleteExpenses(id);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有报销申请数据项?').then(() => {
          this.exportLoading = true;
          return exportExpensesExcel(params);
        }).then(response => {
          this.$download.excel(response, '报销申请.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
