<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParams.tableComment"
          placeholder="请输入表描述"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          :icon="Download"
          @click="handleGenTable"
          v-hasPermi="['infra:codegen:download']"
        >生成</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          :icon="Upload"
          @click="openImportTable"
          v-hasPermi="['infra:codegen:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          :icon="Edit"
          :disabled="single"
          @click="handleEditTable"
          v-hasPermi="['infra:codegen:update']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['infra:codegen:delete']"
        >删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="55"></el-table-column>
      <el-table-column label="序号" type="index" width="50" align="center">
        <template #default="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="表名称"
        align="center"
        prop="tableName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="表描述"
        align="center"
        prop="tableComment"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="实体"
        align="center"
        prop="className"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" :formatter="formatTime"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160" :formatter="formatTime"/>
      <el-table-column label="操作" align="center" width="330" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-tooltip content="预览" placement="top">
            <el-button
              type="text"
              :icon="View"
              @click="handlePreview(scope.row)"
              v-hasPermi="['infra:codegen:preview']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="编辑" placement="top">
            <el-button
              type="text"
              :icon="Edit"
              @click="handleEditTable(scope.row)"
              v-hasPermi="['infra:codegen:update']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button
              type="text"
              :icon="Delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['infra:codegen:delete']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="同步" placement="top">
            <el-button
              type="text"
              :icon="Refresh"
              @click="handleSynchDb(scope.row)"
              v-hasPermi="['infra:codegen:update']"
            ></el-button>
          </el-tooltip>
          <el-tooltip content="生成代码" placement="top">
            <el-button
              type="text"
              :icon="Download"
              @click="handleGenTable(scope.row)"
              v-hasPermi="['infra:codegen:download']"
            ></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 预览界面 -->
    <el-dialog :title="preview.title" v-model="preview.open" width="80%" top="5vh" append-to-body custom-class="scrollbar">
      <el-row>
        <el-col :span="7">
          <el-tree :data="preview.fileTree" :expand-on-click-node="false" default-expand-all highlight-current
                   @node-click="handleNodeClick"/>
        </el-col>
        <el-col :span="17">
          <el-tabs v-model="preview.activeName">
            <el-tab-pane
                v-for="file in preview.data"
                :label="file.filePath.substring(file.filePath.lastIndexOf('/')+1,file.filePath.indexOf('.java'))"
                :name="file.filePath"
                :key="file.filePath"
            >
              <pre><code class="language-html" v-html="highlightedCode(file)"></code></pre>
            </el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>

    </el-dialog>
    <import-table ref="importRef" @ok="handleQuery" />
  </div>
</template>

<script setup>
import * as CodegenApi from "@/api/infra/codegen";
import ImportTable from "./importTable.vue";
import {useRouter, useRoute} from "vue-router";
import {ref, reactive, getCurrentInstance, onActivated, toRefs, onMounted} from "vue";
import { Search, Refresh, Upload, Download, Edit, Delete, View } from "@element-plus/icons-vue"
import Pagination from "@/components/Pagination"

import hljs from "highlight.js";

const router = useRouter()
const route = useRoute()

const { proxy } = getCurrentInstance();

const tableList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const tableNames = ref([]);
const dateRange = ref([]);
const uniqueId = ref("");

const importRef = ref()
const queryRef = ref()
const queryForm = ref()
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tableName: undefined,
    tableComment: undefined
  },
  preview: {
    open: false,
    title: "代码预览",
    data: {},
    fileTree: [],
    activeName: "domain.java"
  }
});

const { queryParams, preview } = toRefs(data);

/** 高亮显示 */
function highlightedCode(item) {
  /**
   * Deprecated as of 10.7.0. highlight(lang, code, ...args) has been deprecated.
   * core.js:995 Deprecated as of 10.7.0. Please use highlight(code, options) instead.
   * https://github.com/highlightjs/highlight.js/issues/2277
   */
  // const vmName = key.substring(key.lastIndexOf("/") + 1, key.indexOf(".vm"));
  // var language = vmName.substring(vmName.indexOf(".") + 1, vmName.length);
  const language = item.filePath.substring(item.filePath.lastIndexOf(".") + 1);
  const result = hljs.highlight(item.code || "", {language: language === "vue" ? "xml" : language, ignoreIllegals: true })
  return result.value || '&nbsp;';
}

onActivated(() => {
  const time = route.query.t;
  if (time != null && time !== uniqueId.value) {
    uniqueId.value = time;
    queryParams.value.pageNum = Number(route.query.pageNum);
    dateRange.value = [];
    queryForm.value?.resetFields()
    // proxy.resetForm("queryForm");
    getList();
  }
})

function formatTime(row, column, cellValue, index) {
  return proxy.parseTime(cellValue, "{y}-{m}-{d} {h}:{i}:{s}")
}

/** 查询表集合 */
function getList() {
  loading.value = true;
  CodegenApi.getCodegenTablePage(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    tableList.value = response.data.list;
    total.value = response.data.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 生成代码操作 */
function handleGenTable(row) {
  const tbNames = row.tableName || tableNames.value;
  if (tbNames === "") {
    proxy.$modal.msgError("请选择要生成的数据");
    return;
  }
  if (row.genType === "1") {
    genCode(row.tableName).then(response => {
      proxy.$modal.msgSuccess("成功生成到自定义路径：" + row.genPath);
    });
  } else {
    CodegenApi.downloadCodegen(tbNames)
    proxy.$download.zip("/tool/gen/batchGenCode?tables=" + tbNames, "ruoyi");
  }
}
/** 同步数据库操作 */
function handleSynchDb(row) {
  const tableName = row.tableName;
  proxy.$modal.confirm('确认要强制同步"' + tableName + '"表结构吗？').then(function () {
    return CodegenApi.syncCodegenFromDB(tableName);
  }).then(() => {
    proxy.$modal.msgSuccess("同步成功");
  }).catch(() => {});
}
/** 打开导入表弹窗 */
function openImportTable() {
  importRef.value.show();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  queryRef.value.resetFields()
  handleQuery();
}
/** 预览按钮 */
function handlePreview(row) {

  CodegenApi.previewCodegen(row.id).then(response => {
    preview.value.data = response.data;
    console.log(response.data)
    preview.value.open = true;
    preview.value.activeName = "domain.java";
    let files = handleFiles(response.data)
    preview.value.fileTree = proxy.handleTree(files, "id", "parentId", "children", "/")
  });
}

function handleNodeClick(data, node) {
  console.log(data, node)
  if (node && !node.isLeaf) {
    return false;
  }
  console.log(preview.value.activeName)
  // 判断，如果非子节点，不允许选中
  preview.value.activeName = data.id;
}

function handleFiles(datas) {
  let exists = {}; // key：file 的 id；value：true
  let files = [];
  // 遍历每个元素
  for (const data of datas) {
    let paths = data.filePath.split('/');
    let fullPath = ''; // 从头开始的路径，用于生成 id
    // 特殊处理 java 文件
    if (paths[paths.length - 1].indexOf('.java') >= 0) {
      let newPaths = [];
      for (let i = 0; i < paths.length; i++) {
        let path = paths[i];
        if (path !== 'java') {
          newPaths.push(path);
          continue;
        }
        newPaths.push(path);
        // 特殊处理中间的 package，进行合并
        let tmp = undefined;
        while (i < paths.length) {
          path = paths[i + 1];
          if (path === 'controller'
              || path === 'convert'
              || path === 'dal'
              || path === 'enums'
              || path === 'service'
              || path === 'vo' // 下面三个，主要是兜底。可能考虑到有人改了包结构
              || path === 'mysql'
              || path === 'dataobject') {
            break;
          }
          tmp = tmp ? tmp + '.' + path : path;
          i++;
        }
        if (tmp) {
          newPaths.push(tmp);
        }
      }
      paths = newPaths;
    }
    // 遍历每个 path， 拼接成树
    for (let i = 0; i < paths.length; i++) {
      // 已经添加到 files 中，则跳过
      let oldFullPath = fullPath;
      // 下面的 replaceAll 的原因，是因为上面包处理了，导致和 tabs 不匹配，所以 replaceAll 下
      fullPath = fullPath.length === 0 ? paths[i] : fullPath.replaceAll('.', '/') + '/' + paths[i];
      if (exists[fullPath]) {
        continue;
      }
      // 添加到 files 中
      exists[fullPath] = true;
      files.push({
        id: fullPath,
        label: paths[i],
        parentId: oldFullPath || '/'  // "/" 为根节点
      });
    }
  }
  return files;
}
// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  tableNames.value = selection.map(item => item.tableName);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}
/** 修改按钮操作 */
function handleEditTable(row) {
  const tableId = row.id || ids.value[0];
  router.push({ path: "/codegen/edit/" + tableId, query: { pageNum: queryParams.value.pageNum } });
}
/** 删除按钮操作 */
function handleDelete(row) {
  const tableIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除表编号为"' + tableIds + '"的数据项？').then(function () {
    return CodegenApi.deleteCodegen(tableIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

onMounted(() => {
  getList()
})

</script>

<style>
@import "highlight.js/styles/github.css";
</style>
