<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="合同编号" prop="contractNo">
        <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="合同状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择合同状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
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
                   v-hasPermi="['oa:contract:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['oa:contract:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="公司类型" align="center" prop="companyType" />
      <el-table-column label="客户id" align="center" prop="customerId" />
      <el-table-column label="供方代表" align="center" prop="supplierUserId" />
      <el-table-column label="总款" align="center" prop="totalFee" />
      <el-table-column label="劳务费" align="center" prop="serviceFee" />
      <el-table-column label="佣金" align="center" prop="commissions" />
      <el-table-column label="零星费用" align="center" prop="otherFee" />
      <el-table-column label="工程实施联系人" align="center" prop="implContactName" />
      <el-table-column label="工程实施联系电话" align="center" prop="implContactPhone" />
      <el-table-column label="合同状态" align="center" prop="status" />
      <el-table-column label="审批状态" align="center" prop="approvalStatus" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['oa:contract:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['oa:contract:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="合同编号" prop="contractNo">
          <el-input v-model="form.contractNo" placeholder="请输入合同编号" />
        </el-form-item>
        <el-form-item label="公司类型" prop="companyType">
          <el-select v-model="form.companyType" placeholder="请选择公司类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户id" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入客户id" />
        </el-form-item>
        <el-form-item label="供方代表" prop="supplierUserId">
          <el-input v-model="form.supplierUserId" placeholder="请输入供方代表" />
        </el-form-item>
        <el-form-item label="总款" prop="totalFee">
          <el-input v-model="form.totalFee" placeholder="请输入总款" />
        </el-form-item>
        <el-form-item label="劳务费" prop="serviceFee">
          <el-input v-model="form.serviceFee" placeholder="请输入劳务费" />
        </el-form-item>
        <el-form-item label="佣金" prop="commissions">
          <el-input v-model="form.commissions" placeholder="请输入佣金" />
        </el-form-item>
        <el-form-item label="零星费用" prop="otherFee">
          <el-input v-model="form.otherFee" placeholder="请输入零星费用" />
        </el-form-item>
        <el-form-item label="工程实施联系人" prop="implContactName">
          <el-input v-model="form.implContactName" placeholder="请输入工程实施联系人" />
        </el-form-item>
        <el-form-item label="工程实施联系电话" prop="implContactPhone">
          <el-input v-model="form.implContactPhone" placeholder="请输入工程实施联系电话" />
        </el-form-item>
        <el-form-item label="合同状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择合同状态">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-select v-model="form.approvalStatus" placeholder="请选择审批状态">
            <el-option label="请选择字典生成" value="" />
          </el-select>
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
import { createContract, updateContract, deleteContract, getContract, getContractPage, exportContractExcel } from "@/api/oa/contract";

export default {
  name: "Contract",
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
      // 合同列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        contractNo: null,
        status: null,
        approvalStatus: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractNo: [{ required: true, message: "合同编号不能为空", trigger: "blur" }],
        customerId: [{ required: true, message: "客户id不能为空", trigger: "blur" }],
        supplierUserId: [{ required: true, message: "供方代表不能为空", trigger: "blur" }],
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
      getContractPage(this.queryParams).then(response => {
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
        contractNo: undefined,
        companyType: undefined,
        customerId: undefined,
        supplierUserId: undefined,
        totalFee: undefined,
        serviceFee: undefined,
        commissions: undefined,
        otherFee: undefined,
        implContactName: undefined,
        implContactPhone: undefined,
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
      this.title = "添加合同";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getContract(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改合同";
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
          updateContract(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createContract(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除合同编号为"' + id + '"的数据项?').then(function() {
          return deleteContract(id);
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
      this.$modal.confirm('是否确认导出所有合同数据项?').then(() => {
          this.exportLoading = true;
          return exportContractExcel(params);
        }).then(response => {
          this.$download.excel(response, '合同.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
