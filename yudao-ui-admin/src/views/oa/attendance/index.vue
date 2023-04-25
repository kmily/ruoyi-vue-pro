<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="打卡类型" prop="attendanceType">
        <el-select v-model="queryParams.attendanceType" placeholder="请选择打卡类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.OA_ATTENDANCE_TYPE)" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input v-model="queryParams.createBy" placeholder="请输入创建者" clearable @keyup.enter.native="handleQuery" />
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
          v-hasPermi="['oa:attendance:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
          v-hasPermi="['oa:attendance:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="打卡类型" align="center" prop="attendanceType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.ATTENDANCE_TYPE" :value="scope.row.attendanceType" />
        </template>
      </el-table-column>
      <el-table-column label="打卡时间段" align="center" prop="attendancePeriod">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.ATTENDANCE_PERIOD" :value="scope.row.attendancePeriod" />
        </template>
      </el-table-column>
      <el-table-column label="工作内容" align="center" prop="workContent" />
      <el-table-column label="拜访客户id" align="center" prop="customerId" />
      <el-table-column label="拜访类型" align="center" prop="visitType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.VISIT_CUSTOMER_TYPE" :value="scope.row.visitType" />
        </template>
      </el-table-column>
      <el-table-column label="拜访事由" align="center" prop="visitReason" />
      <el-table-column label="请假开始时间" align="center" prop="leaveBeginTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.leaveBeginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假结束日期" align="center" prop="leaveEndTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.leaveEndTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假事由" align="center" prop="leaveReason" />
      <el-table-column label="请假工作交接" align="center" prop="leaveHandover" />
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
            v-hasPermi="['oa:attendance:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['oa:attendance:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="打卡类型" prop="attendanceType">
          <el-select v-model="form.attendanceType" placeholder="请选择打卡类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.OA_ATTENDANCE_TYPE)" :key="dict.value"
              :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="打卡时间段" prop="attendancePeriod">
          <el-input v-model="form.attendancePeriod" placeholder="请输入打卡时间段" />
        </el-form-item>
        <el-form-item label="工作内容">
          <editor v-model="form.workContent" :min-height="192" />
        </el-form-item>
        <el-form-item label="拜访客户id" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入拜访客户id" />
        </el-form-item>
        <el-form-item label="拜访类型" prop="visitType">
          <el-select v-model="form.visitType" placeholder="请选择拜访类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.OA_VISIT_TYPE)" :key="dict.value" :label="dict.label"
              :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="拜访事由" prop="visitReason">
          <el-input v-model="form.visitReason" placeholder="请输入拜访事由" />
        </el-form-item>
        <el-form-item label="请假开始时间" prop="leaveBeginTime">
          <el-date-picker clearable v-model="form.leaveBeginTime" type="date" value-format="timestamp"
            placeholder="选择请假开始时间" />
        </el-form-item>
        <el-form-item label="请假结束日期" prop="leaveEndTime">
          <el-date-picker clearable v-model="form.leaveEndTime" type="date" value-format="timestamp"
            placeholder="选择请假结束日期" />
        </el-form-item>
        <el-form-item label="请假事由" prop="leaveReason">
          <el-input v-model="form.leaveReason" placeholder="请输入请假事由" />
        </el-form-item>
        <el-form-item label="请假工作交接" prop="leaveHandover">
          <el-input v-model="form.leaveHandover" placeholder="请输入请假工作交接" />
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
import { createAttendance, updateAttendance, deleteAttendance, getAttendance, getAttendancePage, exportAttendanceExcel } from "@/api/oa/attendance";
import Editor from '@/components/Editor';

export default {
  name: "Attendance",
  components: {
    Editor
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
      // 考勤打卡列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        attendanceType: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        attendanceType: [{ required: true, message: "打卡类型不能为空", trigger: "change" }],
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
      getAttendancePage(this.queryParams).then(response => {
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
        attendanceType: undefined,
        attendancePeriod: undefined,
        workContent: undefined,
        customerId: undefined,
        visitType: undefined,
        visitReason: undefined,
        leaveBeginTime: undefined,
        leaveEndTime: undefined,
        leaveReason: undefined,
        leaveHandover: undefined,
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
      this.title = "添加考勤打卡";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getAttendance(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改考勤打卡";
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
          updateAttendance(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createAttendance(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除考勤打卡编号为"' + id + '"的数据项?').then(function () {
        return deleteAttendance(id);
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
      this.$modal.confirm('是否确认导出所有考勤打卡数据项?').then(() => {
        this.exportLoading = true;
        return exportAttendanceExcel(params);
      }).then(response => {
        this.$download.excel(response, '考勤打卡.xls');
        this.exportLoading = false;
      }).catch(() => { });
    }
  }
};
</script>
