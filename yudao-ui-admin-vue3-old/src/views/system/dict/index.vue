<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryDictRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字典名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入字典名称" clearable style="width: 240px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="字典类型" prop="type">
        <el-input v-model="queryParams.type" placeholder="请输入字典类型" clearable style="width: 240px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="字典状态" clearable style="width: 240px">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)" :key="parseInt(dict.value)" :label="dict.label" :value="parseInt(dict.value)" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRangeCreateTime" style="width: 240px" value-format="YYYY-MM-DD" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:dict:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:dict:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="typeList">
      <el-table-column label="字典编号" align="center" prop="id" />
      <el-table-column label="字典名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
        <template #default="scope">
          <router-link :to="'/system/dict-data/index/' + scope.row.id" class="link-type">
            <span>{{ scope.row.type }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="mini" type="text" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:dict:update']">修改</el-button>
          <el-button size="mini" type="text" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:dict:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="dictDgRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)" :key="parseInt(dict.value)" :label="parseInt(dict.value)">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Dict">
import {
  listType,
  getType,
  delType,
  addType,
  updateType,
  exportType,
} from "@/api/system/dict/type";

import { CommonStatusEnum } from "@/utils/constants";
import { getDictDatas, DICT_TYPE } from "@/utils/dict";

const { proxy } = getCurrentInstance();
const typeList = ref([]);
const open = ref(false);
const title = ref("");
const total = ref(0);
const loading = ref(false);
const exportLoading = ref(false);
const showSearch = ref(true);
const dateRangeCreateTime = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    name: undefined,
    type: undefined,
    status: undefined,
  },
  rules: {
    name: [
      { required: true, message: "字典名称不能为空", trigger: "blur" },
    ],
    type: [
      { required: true, message: "字典类型不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询字典类型列表 */
function getList() {
  loading.value = true;
  // 处理查询参数
  let params = { ...queryParams.value };
  proxy.addBeginAndEndTime(params, dateRangeCreateTime.value, "createTime");
  // 执行查询
  listType(params).then((response) => {
    typeList.value = response.data.list;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 表单重置 */
function reset() {
  proxy.resetForm("dictDgRef");
  form.value = {
    id: undefined,
    name: undefined,
    type: undefined,
    status: CommonStatusEnum.ENABLE,
    remark: undefined,
  };
}

/** 重置按钮操作 */
function resetQuery() {
  dateRangeCreateTime.value = [];
  proxy.resetForm("queryDictRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加字典类型";
}

/** 导出按钮操作 */
function handleExport() {
  // 处理查询参数
  let params = { ...queryParams.value };
  params.pageNo = undefined;
  params.pageSize = undefined;
  proxy.addBeginAndEndTime(params, dateRangeCreateTime.value, "createTime");
  // 执行导出
  proxy.$modal
    .confirm("是否确认导出所有字典类型数据项?")
    .then(() => {
      exportLoading.value = true;
      return exportType(params);
    })
    .then((response) => {
      proxy.$download.excel(response, "字典类型.xls");
      exportLoading.value = false;
    })
    .catch(() => {});
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const id = row.id;
    getType(id).then((response) => {
      form.value = response.data;
      open.value = true;
      title.value = "修改字典类型";
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id;
  proxy.$modal
    .confirm('是否确认删除字典编号为"' + ids + '"的数据项?')
    .then(function () {
      return delType(ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["dictDgRef"].validate((valid) => {
    if (valid) {
      if (form.value.id !== undefined) {
        updateType(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addType(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

getList();
</script>
