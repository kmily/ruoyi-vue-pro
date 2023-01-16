<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="Apk公钥信息	" prop="pubKey">-->
<!--        <el-input v-model="queryParams.pubKey" placeholder="请输入Apk公钥信息	" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item label="上线时间" prop="onlineTime">
        <el-date-picker v-model="queryParams.onlineTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
<!--      <el-form-item label="下线时间，下线时更新，但可能用不到" prop="offlineTime">-->
<!--        <el-date-picker v-model="queryParams.offlineTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"-->
<!--                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="创建时间" prop="createTime">-->
<!--        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"-->
<!--                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />-->
<!--      </el-form-item>-->
      <el-form-item label="应用类型" prop="sysType">
        <el-select v-model="queryParams.sysType" placeholder="请选择" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_SYS_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="文件包名称，带版本号，带后缀名	" prop="fileName">-->
<!--        <el-input v-model="queryParams.fileName" placeholder="请输入文件包名称，带版本号，带后缀名	" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item label="版本号" prop="verNo">
        <el-input v-model="queryParams.verNo" placeholder="请输入版本号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="审核状态" prop="auditState">
        <el-select v-model="queryParams.auditState" placeholder="请选择" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_APP_AUDIT_STATE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="备注说明" prop="remark">-->
<!--        <el-input v-model="queryParams.remark" placeholder="请输入备注说明" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="软件包哈希值" prop="packageHash">-->
<!--        <el-input v-model="queryParams.packageHash" placeholder="请输入软件包哈希值" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item label="版本状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_APP_STATE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="版本序号" prop="verCode">-->
<!--        <el-input v-model="queryParams.verCode" placeholder="请输入版本序号" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item label="强制升级" prop="forceUpdate">
        <el-select v-model="queryParams.forceUpdate" placeholder="请选择" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_FORCE_UPDATE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
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
                   v-hasPermi="['scan:app-version:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['scan:app-version:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" align="center" prop="id" />
<!--      <el-table-column label="图片上传地址，有何用？" align="center" prop="fileUrl" />-->
<!--      <el-table-column label="Apk公钥信息	" align="center" prop="pubKey" />-->
      <el-table-column label="上线时间" align="center" prop="onlineTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.onlineTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="下线时间，下线时更新，但可能用不到" align="center" prop="offlineTime" width="180">-->
<!--        <template v-slot="scope">-->
<!--          <span>{{ parseTime(scope.row.offlineTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
<!--        <template v-slot="scope">-->
<!--          <span>{{ parseTime(scope.row.createTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="终端类型" align="center" prop="sysType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.SCAN_SYS_TYPE" :value="scope.row.sysType" />
        </template>
      </el-table-column>
      <el-table-column label="文件包名称" align="center" prop="fileName" />
      <el-table-column label="版本号" align="center" prop="verNo" />
      <el-table-column label="审核状态" align="center" prop="auditState">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.SCAN_APP_AUDIT_STATE" :value="scope.row.auditState" />
        </template>
      </el-table-column>
      <el-table-column label="备注说明" align="center" prop="remark" />
      <el-table-column label="版本描述信息" align="center" prop="description" />
      <el-table-column label="软件包哈希值" align="center" prop="packageHash" />
      <el-table-column label="版本状态" align="center" prop="state">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.SCAN_APP_STATE" :value="scope.row.state" />
        </template>
      </el-table-column>
      <el-table-column label="版本序号" align="center" prop="verCode" />
      <el-table-column label="强制升级" align="center" prop="forceUpdate">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.SCAN_FORCE_UPDATE" :value="scope.row.forceUpdate" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['scan:app-version:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['scan:app-version:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件上传">
          <fileUpload v-model="form.fileUrl"/>
        </el-form-item>
<!--        <el-form-item label="Apk公钥信息	" prop="pubKey">-->
<!--          <el-input v-model="form.pubKey" placeholder="请输入Apk公钥信息	" />-->
<!--        </el-form-item>-->
        <el-form-item label="上线时间" prop="onlineTime">
          <el-date-picker clearable v-model="form.onlineTime" type="date" value-format="timestamp" placeholder="选择上线时间，新增记录时，" />
        </el-form-item>
<!--        <el-form-item label="下线时间，下线时更新，但可能用不到" prop="offlineTime">-->
<!--          <el-date-picker clearable v-model="form.offlineTime" type="date" value-format="timestamp" placeholder="选择下线时间，下线时更新，但可能用不到" />-->
<!--        </el-form-item>-->
        <el-form-item label="应用类型" prop="sysType">
          <el-select v-model="form.sysType" placeholder="请选择">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_SYS_TYPE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件包名称" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件包名称" />
        </el-form-item>
        <el-form-item label="版本号" prop="verNo">
          <el-input v-model="form.verNo" placeholder="请输入版本号" />
        </el-form-item>
        <el-form-item label="审核状态" prop="auditState">
          <el-select v-model="form.auditState" placeholder="请选择">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_APP_AUDIT_STATE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注说明" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注说明" />
        </el-form-item>
        <el-form-item label="版本描述信息">
          <editor v-model="form.description" :min-height="192"/>
        </el-form-item>
        <el-form-item label="软件包哈希值" prop="packageHash">
          <el-input v-model="form.packageHash" placeholder="请输入软件包哈希值" />
        </el-form-item>
        <el-form-item label="版本状态" prop="state">
          <el-radio-group v-model="form.state">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_APP_STATE)"
                      :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="版本序号" prop="verCode">
          <el-input v-model="form.verCode" placeholder="请输入版本序号" />
        </el-form-item>
        <el-form-item label="强制升级" prop="forceUpdate">
          <el-radio-group v-model="form.forceUpdate">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SCAN_FORCE_UPDATE)"
                      :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
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
import { createAppVersion, updateAppVersion, deleteAppVersion, getAppVersion, getAppVersionPage, exportAppVersionExcel } from "@/api/scan/appVersion";
import FileUpload from '@/components/FileUpload';
import Editor from '@/components/Editor';

export default {
  name: "AppVersion",
  components: {
    FileUpload,
    Editor,
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
      // 应用版本记录列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        fileUrl: null,
        pubKey: null,
        onlineTime: [],
        offlineTime: [],
        createTime: [],
        sysType: null,
        fileName: null,
        verNo: null,
        auditState: null,
        remark: null,
        description: null,
        packageHash: null,
        state: null,
        verCode: null,
        forceUpdate: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        auditState: [{ required: true, message: "审核状态，0待审核，1审核通过，2审核不通过不能为空", trigger: "change" }],
        state: [{ required: true, message: "版本状态0:正常1注销不能为空", trigger: "blur" }],
        verCode: [{ required: true, message: "版本序号不能为空", trigger: "blur" }],
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
      getAppVersionPage(this.queryParams).then(response => {
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
        fileUrl: undefined,
        pubKey: undefined,
        onlineTime: undefined,
        offlineTime: undefined,
        id: undefined,
        sysType: undefined,
        fileName: undefined,
        verNo: undefined,
        auditState: undefined,
        remark: undefined,
        description: undefined,
        packageHash: undefined,
        state: undefined,
        verCode: undefined,
        forceUpdate: undefined,
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
      this.title = "添加应用版本记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getAppVersion(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改应用版本记录";
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
          updateAppVersion(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createAppVersion(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除应用版本记录编号为"' + id + '"的数据项?').then(function() {
        return deleteAppVersion(id);
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
      this.$modal.confirm('是否确认导出所有应用版本记录数据项?').then(() => {
        this.exportLoading = true;
        return exportAppVersionExcel(params);
      }).then(response => {
        this.$download.excel(response, '应用版本记录.xls');
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
