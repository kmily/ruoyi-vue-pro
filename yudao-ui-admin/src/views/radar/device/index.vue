<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备编号" prop="sn">
        <el-input v-model="queryParams.sn" placeholder="请输入设备编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="雷达类别" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择设备类别" clearable size="small">
        <el-option v-for="dict in this.getDictDatas(DICT_TYPE.RADAR_TYPE)"
            :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="绑定状态" prop="bind">
        <el-select v-model="queryParams.bind" placeholder="请选择绑定状态" clearable size="small">
          <el-option label="未绑定"  value="0"/>
        <el-option  label="已绑定" value="1"/>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                  v-hasPermi="['radar:device:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                  v-hasPermi="['radar:device:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="设备ID" align="center" prop="id" />
      <el-table-column label="设备编号" align="center" prop="sn" />
      <el-table-column label="设备名称" align="center" prop="name" />
      <el-table-column label="绑定用户昵称" align="center" prop="nickname" />
      <el-table-column label="绑定用户手机" align="center" prop="mobile" />
      <el-table-column label="设备类别" align="center" prop="type" >
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.RADAR_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="是否绑定" align="center" prop="bind" >
        <template v-slot="scope">
          <el-tag v-if="scope.row.bind === 1" type="success">已绑定</el-tag>
          <el-tag v-else type="danger">未绑定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="绑定时间" align="center" prop="type" >
        <template v-slot="scope">
        {{parseTime(scope.row.bindTime)}}
        </template>
      </el-table-column>
      <el-table-column label="是否在线" align="center" prop="keepalive" width="180">
        <template v-slot="scope">
          <span v-if="scope.row.bind === 1">
              <el-tag v-if="keepalive(scope.row)" type="success">在线</el-tag>
              <el-tag v-else type="danger">离线</el-tag>
          </span>
          <span v-else></span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button v-if="scope.row.bind === 0" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                    v-hasPermi="['radar:device:update']">修改</el-button>
          <!-- <el-button size="mini" v-if="scope.row.bind === 0" type="text" icon="el-icon-check" @click="handleBind(scope.row)"
                    v-hasPermi="['radar:device:update']">绑定</el-button>
          <el-button size="mini" v-if="scope.row.bind === 1" type="text" icon="el-icon-close" @click="handleUnbind(scope.row)"
                    v-hasPermi="['radar:device:update']">解绑</el-button> -->
          <el-button size="mini" v-if="scope.row.bind === 0" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                    v-hasPermi="['radar:device:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备编号" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入设备sn编号" />
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备类别" prop="type">
          <el-select v-model="form.type" placeholder="请选择设备类别">
          <el-option
            v-for="dict in this.getDictDatas(DICT_TYPE.RADAR_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="parseInt(dict.value)"
          />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="parseInt(dict.value)">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 设备绑定窗口 -->
    <el-dialog title="绑定用户" :visible.sync="bindOpen" width="600px" v-dialogDrag append-to-body>
      <el-form ref="bindForm" :model="bindForm" :rules="bindRules" label-width="90px" size="small">
        <el-form-item label="设备名称" prop="mobile">
          <span>{{bindForm.name}}</span>
        </el-form-item>
        <el-form-item label="设备ID" prop="mobile">
          <span>{{bindForm.sn}}</span>
        </el-form-item>
        <el-form-item label="用户手机号" prop="mobile">
          <el-input v-model="bindForm.mobile" placeholder="用户手机号" :clearable="true" style="width: 80%">
          </el-input>
          <el-button @click="queryUser" type="primary" size="small" icon="el-icon-search" style="width: 10%;margin-left:5px;"></el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="userList">
        <el-table-column label="用户昵称" align="center" prop="nickname" />
        <el-table-column label="用户手机号" align="center" prop="mobile" />
        <el-table-column label="注册日期" align="center" prop="createTile" >
          <template v-slot="scope">
          {{parseTime(scope.row.createTime)}}
          </template>
        </el-table-column>
  
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBindForm">确 定</el-button>
        <el-button @click="bindCancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import { createDevice, updateDevice, deleteDevice, getDevice, getDevicePage, exportDeviceExcel } from "@/api/radar/device";
import {getUserByMobile} from '@/api/member/user';

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
      // 设备信息列表
      list: [],
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      bindOpen: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        sn: null,
        name: null,
        type: null,
        status: null,
        keepalive: null,
        createTime: [],
        bind: null
      },
      // 表单参数
      form: {},
      bindForm: {},
      // 表单校验
      rules: {
        sn: [{ required: true, message: "设备sn编号不能为空", trigger: "blur" }],
        name: [{ required: true, message: "设备名称不能为空", trigger: "blur" }],
        type: [{ required: true, message: "设备类别不能为空", trigger: "change" }],
        status: [{ required: true, message: "状态不能为空", trigger: "change" }],
      },
      bindRules:{
        mobile:[{required: true, message: '手机号不能为空', trigger: 'blur'}]
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
        id: undefined,
        sn: undefined,
        name: undefined,
        type: undefined,
        bind: undefined,
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
      this.title = "添加设备信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getDevice(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备信息";
      });
    },

    /**
     * 绑定操作
     */
    handleBind(row){
      this.bindOpen = true;
      this.bindForm = {
        name: row.name,
        sn: row.sn
      },
      this.userList = [];
    },
    /**
     * 解绑操作
     */
    handleUnbind(row){
      this.bindOpen = true;
      this.bindForm = {
        name: row.name,
        sn: row.sn
      },
      this.userList = [];
    },

    bindCancel(){
      this.bindOpen = false;
      this.bindForm = {},
      this.userList = [];
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

    /**
     * 提交绑定
     */
    submitBindForm(){
      if(this.userList.length ==  0){
        this.$modal.msgError('请选择绑定用户');
        return false;
      }  
      this.$refs["bindForm"].validate(valid => {
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
      this.$modal.confirm('是否确认删除设备信息编号为"' + id + '"的数据项?').then(function() {
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
      this.$modal.confirm('是否确认导出所有设备信息数据项?').then(() => {
          this.exportLoading = true;
          return exportDeviceExcel(params);
        }).then(response => {
          this.$download.excel(response, '设备信息.xls');
          this.exportLoading = false;
        }).catch(() => {});
    },
      keepalive(data){
      let kl = data.keepalive;
      let now = new Date().getTime();
      if(kl == null || now - kl > 5 * 60 * 1000){
        return false;
      }else{
        return true;
      }
    },

    // 查询绑定用户
    queryUser(){
      if(!this.bindForm.mobile){
        this.$modal.msgError('手机号不能为空');
        return false;
      }
      getUserByMobile(this.bindForm.mobile).then(response => {
        if(!response.data){
          this.$modal.msgWarning('用户不存在!');
          this.userList = [];
        }else{
          this.userList = [response.data];
        }
      });
    }
  }
};
</script>
