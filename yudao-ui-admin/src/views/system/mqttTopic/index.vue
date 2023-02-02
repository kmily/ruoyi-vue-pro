<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主题名称" prop="topicName">
        <el-input v-model="queryParams.topicName" placeholder="请输入主题名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="使用状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择使用状态" clearable>
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
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
                   v-hasPermi="['system:mqtt-topic:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:mqtt-topic:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" :max-height="tableHeight">
      <!-- <el-table-column label="编号" align="center" prop="id" /> -->
      <el-table-column label="序号" width="50px" align="center">
        <template v-slot="scope">
          {{ scope.$index + (queryParams.pageNo - 1) * queryParams.pageSize + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="主题名称" align="center" prop="topicName"/>
      <el-table-column label="订阅主题" align="center" prop="subTopic"/>
      <el-table-column label="发布主题" align="center" prop="pusTopic"/>
      <el-table-column label="启用状态" align="center" prop="status">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:mqtt-topic:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:mqtt-topic:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主题名称" prop="topicName">
          <el-input v-model="form.topicName" placeholder="请输入主题名称"/>
        </el-form-item>
        <el-form-item label="订阅主题" prop="subTopic">
          <el-input v-model="form.subTopic" placeholder="请输入订阅主题"/>
        </el-form-item>
        <el-form-item label="发布主题" prop="pusTopic">
          <el-input v-model="form.pusTopic" placeholder="请输入发布主题"/>
        </el-form-item>
        <el-form-item label="启用状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)" :key="parseInt(dict.value)"
                      :label="parseInt(dict.value)">
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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
import { createMqttTopic, updateMqttTopic, deleteMqttTopic, getMqttTopic, getMqttTopicPage, exportMqttTopicExcel } from "@/api/system/mqttTopic";

export default {
  name: "MqttTopic",
  components: {},
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
      // mqtt主题列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      tableHeight: '0px',
      scrollHeight: document.documentElement.scrollHeight - 210,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        topicName: null,
        subTopic: null,
        pusTopic: null,
        status: null,
        createTime: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [{required: true, message: "启用状态不能为空!", trigger: "change"}],
        topicName: [{required: true, message: "请输入主题名称!", trigger: "change"}],
        pusTopic: [{required: true, message: "请输入发布主题!", trigger: "change"}],
        subTopic: [{required: true, message: "请输入订阅主题!", trigger: "change"}],
      }
    };
  },
  created() {
    this.getList();
    this.setTableHeight();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getMqttTopicPage(this.queryParams).then(response => {
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
        topicName: undefined,
        subTopic: undefined,
        pusTopic: undefined,
        status: 0,
        remark: undefined,
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
      this.title = "添加MQTT主题";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getMqttTopic(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MQTT主题";
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
          updateMqttTopic(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createMqttTopic(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除MQTT主题名称为"' + row.topicName + '"的数据项?').then(function () {
        return deleteMqttTopic(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有MQTT主题数据项?').then(() => {
        this.exportLoading = true;
        return exportMqttTopicExcel(params);
      }).then(response => {
        this.$download.excel(response, 'MQTT主题.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    },
    //动态改变table高度
    setTableHeight() {
      this.$nextTick(() => {
        const queryFormHeight = this.$refs['queryForm'].$el.offsetHeight
        this.tableHeight = this.scrollHeight - queryFormHeight + "px"
      })
    },
  }
};
</script>
