<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryFrom" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字典名称" prop="dictType">
        <el-select v-model="queryParams.dictType">
          <el-option v-for="item in typeOptions" :key="item.id" :label="item.name" :value="item.type"/>
        </el-select>
      </el-form-item>
      <el-form-item label="字典标签" prop="label">
        <el-input v-model="queryParams.label" placeholder="请输入字典标签" clearable @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="数据状态" clearable>
          <el-option v-for="dict in statusDictDatas" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="mini" @click="handleAdd"
                   v-hasPermi="['system:dict:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:dict:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" >
      <el-table-column label="字典编码" align="center" prop="id" />
      <el-table-column label="字典标签" align="center" prop="label" />
      <el-table-column label="字典键值" align="center" prop="value" />
      <el-table-column label="字典排序" align="center" prop="sort" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="颜色类型" align="center" prop="colorType" />
      <el-table-column label="CSS Class" align="center" prop="cssClass" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="mini" type="text" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:dict:update']">修改</el-button>
          <el-button size="mini" type="text" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:dict:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="dataRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" disabled="true" />
        </el-form-item>
        <el-form-item label="数据标签" prop="label">
          <el-input v-model="form.label" placeholder="请输入数据标签" />
        </el-form-item>
        <el-form-item label="数据键值" prop="value">
          <el-input v-model="form.value" placeholder="请输入数据键值" />
        </el-form-item>
        <el-form-item label="显示排序" prop="sort">
          <el-input-number v-model="form.sort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in statusDictDatas" :key="parseInt(dict.value)" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="颜色类型" prop="colorType">
          <el-select v-model="form.colorType">
            <el-option v-for="item in colorTypeOptions" :key="item.value" :label="item.label + '(' + item.value + ')'" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="CSS Class" prop="cssClass">
          <el-input v-model="form.cssClass" placeholder="请输入 CSS Class" />
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

<script setup name="Data">
import { listData, getData, delData, addData, updateData, exportData } from "@/api/system/dict/data";
import { listAllSimple, getType } from "@/api/system/dict/type";

import { CommonStatusEnum } from '@/utils/constants'
import { getDictDatas, DICT_TYPE } from '@/utils/dict'

const { proxy } = getCurrentInstance();
// 数据字典
const statusDictDatas = getDictDatas(DICT_TYPE.COMMON_STATUS);
const dataList = ref([]);
const open = ref(false);
const title = ref("");
const total = ref(0);
const loading = ref(false);
const exportLoading = ref(false);
const showSearch = ref(true);
const defaultDictType = ref("");
const typeOptions = ref([]);
const route = useRoute();


// 数据标签回显样式
const colorTypeOptions = ref([
  { value: "default", label: "默认" }, 
  { value: "primary", label: "主要" }, 
  { value: "success", label: "成功" },
  { value: "info", label: "信息" },
  { value: "warning", label: "警告" },
  { value: "danger", label: "危险" }
]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    dictName: undefined,
    dictType: undefined,
    status: undefined
  },
  rules: {
    dictLabel: [{ required: true, message: "数据标签不能为空", trigger: "blur" }],
    dictValue: [{ required: true, message: "数据键值不能为空", trigger: "blur" }],
    dictSort: [{ required: true, message: "数据顺序不能为空", trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);


/** 查询字典类型详细 */
function getTypes(dictId) {
  getType(dictId).then(response => {
    queryParams.value.dictType = response.data.type;
    defaultDictType.value = response.data.type;
    getList();
  });
}
/** 查询字典类型列表 */
function getTypeList() {
  listAllSimple().then(response => {
    typeOptions.value = response.data;
  });
}
/** 查询字典数据列表 */
function getList() {
  loading.value = true;
  listData(queryParams.value).then(response => {
    dataList.value = response.data.list;
    total.value = response.data.total;
    loading.value = false;
  });
}
// 取消按钮
function cancel() {
  open.value = false;
  reset();
}
// 表单重置
function reset() {
  form.value = {
    id: undefined,
    label: undefined,
    value: undefined,
    sort: 0,
    status: CommonStatusEnum.ENABLE,
    colorType: 'default',
    cssClass: undefined,
    remark: undefined
  };
  proxy.resetForm("dataRef");
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryFrom");
  queryParams.value.dictType = defaultDictType.value;
  handleQuery();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加字典数据";
  form.value.dictType = queryParams.value.dictType;
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || this.ids
  getData(id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改字典数据";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["dataRef"].validate(valid => {
    if (valid) {
      if (form.value.id !== undefined) {
        updateData(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addData(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id;
  proxy.$modal.confirm('是否确认删除字典编码为"' + ids + '"的数据项?').then(function() {
      return delData(ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.$modal.confirm('是否确认导出所有数据项?').then(() => {
      exportLoading.value = true;
      return exportData(queryParams.value);
    }).then(response => {
      proxy.$download.excel(response, '字典数据.xls');
      exportLoading.value = false;
  }).catch(() => {});
}

getTypes(route.params && route.params.dictId);
getTypeList();
</script>
