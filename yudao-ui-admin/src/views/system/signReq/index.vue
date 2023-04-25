<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="openId" prop="openId">
        <el-input v-model="queryParams.openId" placeholder="请输入openId" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="queryParams.email" placeholder="请输入邮箱" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery" />
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
          v-hasPermi="['system:sign-req:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
          v-hasPermi="['system:sign-req:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <!-- <el-table-column label="id" align="center" prop="id" /> -->
      <el-table-column label="openId" align="center" prop="openId" />
      <el-table-column label="姓名" align="center" prop="userName" />
      <el-table-column label="性别" align="center" prop="sex">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="scope.row.sex" />
        </template>
      </el-table-column>
      <el-table-column label="手机号码" align="center" prop="phonenumber" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="处理状态" align="center" prop="status" />
      <el-table-column label="提交时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:sign-req:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:sign-req:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注信息" />
        </el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="openId" prop="openId">
          <el-input v-model="form.openId" placeholder="请输入openId" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
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
import { createSignReq, updateSignReq, deleteSignReq, getSignReq, getSignReqPage, exportSignReqExcel } from "@/api/system/signReq";

export default {
  name: "SignReq",
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
      // 注册申请列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        openId: null,
        email: null,
        phonenumber: null,
        userName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
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
      getSignReqPage(this.queryParams).then(response => {
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
        remark: undefined,
        status: undefined,
        openId: undefined,
        email: undefined,
        phonenumber: undefined,
        userName: undefined,
        sex: undefined,
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
      this.title = "添加注册申请";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getSignReq(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改注册申请";
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
          updateSignReq(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createSignReq(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除注册申请编号为"' + id + '"的数据项?').then(function () {
        return deleteSignReq(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = { ...this.queryParams };
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有注册申请数据项?').then(() => {
        this.exportLoading = true;
        return exportSignReqExcel(params);
      }).then(response => {
        this.$download.excel(response, '注册申请.xls');
        this.exportLoading = false;
      }).catch(() => { });
    }
  }
};
</script>
