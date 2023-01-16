<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="创建时间" prop="createTime">-->
<!--        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"-->
<!--                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />-->
<!--      </el-form-item>-->
      <el-form-item label="设备名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="设备编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入设备编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="联系人" prop="contact">
        <el-input v-model="queryParams.contact" placeholder="请输入联系人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入联系电话" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="激活序号" prop="serialNo">
        <el-input v-model="queryParams.serialNo" placeholder="请输入激活序号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item label="省名称" prop="provinceName">-->
<!--        <el-input v-model="queryParams.provinceName" placeholder="请输入省名称" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="省编号" prop="provinceCode">-->
<!--        <el-input v-model="queryParams.provinceCode" placeholder="请输入省编号" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="市名称" prop="cityName">-->
<!--        <el-input v-model="queryParams.cityName" placeholder="请输入市名称" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="市编号" prop="cityCode">-->
<!--        <el-input v-model="queryParams.cityCode" placeholder="请输入市编号" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="区名称" prop="areaName">-->
<!--        <el-input v-model="queryParams.areaName" placeholder="请输入区名称" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="区编号" prop="areaCode">-->
<!--        <el-input v-model="queryParams.areaCode" placeholder="请输入区编号" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="详细地址" prop="address">-->
<!--        <el-input v-model="queryParams.address" placeholder="请输入详细地址" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="设备管理密码" prop="password">-->
<!--        <el-input v-model="queryParams.password" placeholder="请输入设备管理密码" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item label="门店名称" prop="storeName">
        <el-input v-model="queryParams.storeName" placeholder="请输入门店名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"-->
<!--                   v-hasPermi="['scan:device:create']">新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['scan:device:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="门店名称" align="center" prop="storeName" />
      <el-table-column label="设备名称" align="center" prop="name" />
      <el-table-column label="设备编号" align="center" prop="code" />
      <el-table-column label="联系人" align="center" prop="contact" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="激活序号" align="center" prop="serialNo" />
      <el-table-column label="省名称" align="center" prop="provinceName" />
<!--      <el-table-column label="省编号" align="center" prop="provinceCode" />-->
      <el-table-column label="市名称" align="center" prop="cityName" />
<!--      <el-table-column label="市编号" align="center" prop="cityCode" />-->
      <el-table-column label="区名称" align="center" prop="areaName" />
<!--      <el-table-column label="区编号" align="center" prop="areaCode" />-->
      <el-table-column label="详细地址" align="center" prop="address" />
<!--      <el-table-column label="设备管理密码" align="center" prop="password" />-->


      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['scan:device:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['scan:device:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="联系人" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="激活序号" prop="serialNo">
          <el-input v-model="form.serialNo" placeholder="请输入激活序号" />
        </el-form-item>
        <el-form-item label="省名称" prop="provinceName">
          <el-input v-model="form.provinceName" placeholder="请输入省名称" />
        </el-form-item>
        <el-form-item label="省编号" prop="provinceCode">
          <el-input v-model="form.provinceCode" placeholder="请输入省编号" />
        </el-form-item>
        <el-form-item label="市名称" prop="cityName">
          <el-input v-model="form.cityName" placeholder="请输入市名称" />
        </el-form-item>
        <el-form-item label="市编号" prop="cityCode">
          <el-input v-model="form.cityCode" placeholder="请输入市编号" />
        </el-form-item>
        <el-form-item label="区名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入区名称" />
        </el-form-item>
        <el-form-item label="区编号" prop="areaCode">
          <el-input v-model="form.areaCode" placeholder="请输入区编号" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="设备管理密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入设备管理密码" />
        </el-form-item>
        <el-form-item label="门店名称" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入门店名称" />
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
import { createDevice, updateDevice, deleteDevice, getDevice, getDevicePage, exportDeviceExcel } from "@/api/scan/device";

export default {
  name: "Device",
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
      // 设备列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        createTime: [],
        name: null,
        code: null,
        contact: null,
        phone: null,
        serialNo: null,
        provinceName: null,
        provinceCode: null,
        cityName: null,
        cityCode: null,
        areaName: null,
        areaCode: null,
        address: null,
        password: null,
        storeName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{ required: true, message: "设备名称不能为空", trigger: "blur" }],
        code: [{ required: true, message: "设备编号不能为空", trigger: "blur" }],
        contact: [{ required: true, message: "联系人不能为空", trigger: "blur" }],
        phone: [{ required: true, message: "联系电话不能为空", trigger: "blur" }],
        serialNo: [{ required: true, message: "激活序号不能为空", trigger: "blur" }],
        password: [{ required: true, message: "设备管理密码不能为空", trigger: "blur" }],
        storeName: [{ required: true, message: "门店名称不能为空", trigger: "blur" }],
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
      getDevicePage(this.queryParams).then(response => {
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
        name: undefined,
        code: undefined,
        contact: undefined,
        phone: undefined,
        serialNo: undefined,
        provinceName: undefined,
        provinceCode: undefined,
        cityName: undefined,
        cityCode: undefined,
        areaName: undefined,
        areaCode: undefined,
        address: undefined,
        password: undefined,
        id: undefined,
        storeName: undefined,
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
      this.title = "添加设备";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getDevice(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
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
          updateDevice(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createDevice(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除设备编号为"' + id + '"的数据项?').then(function() {
          return deleteDevice(id);
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
      this.$modal.confirm('是否确认导出所有设备数据项?').then(() => {
          this.exportLoading = true;
          return exportDeviceExcel(params);
        }).then(response => {
          this.$download.excel(response, '设备.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
