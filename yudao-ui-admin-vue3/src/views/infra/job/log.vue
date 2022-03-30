<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="处理器的名字" prop="handlerName">
        <el-input v-model="queryParams.handlerName" placeholder="请输入处理器的名字" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="开始执行时间" prop="beginTime">
        <el-date-picker clearable v-model="queryParams.beginTime" type="date" value-format="yyyy-MM-dd" placeholder="选择开始执行时间" />
      </el-form-item>
      <el-form-item label="结束执行时间" prop="endTime">
        <el-date-picker clearable v-model="queryParams.endTime" type="date" value-format="yyyy-MM-dd" placeholder="选择结束执行时间" />
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.INFRA_JOB_LOG_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" :icon="Download" size="small" @click="handleExport"
                   v-hasPermi="['infra:job:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="任务编号" align="center" prop="jobId" />
      <el-table-column label="处理器的名字" align="center" prop="handlerName" />
      <el-table-column label="处理器的参数" align="center" prop="handlerParam" />
      <el-table-column label="第几次执行" align="center" prop="executeIndex" />
      <el-table-column label="执行时间" align="center" width="300">
        <template #default="scope">
          <span>{{ proxy.parseTime(scope.row.beginTime) + ' ~ ' + proxy.parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行时长" align="center" prop="duration">
        <template #default="scope">
          <span>{{ scope.row.duration + ' 毫秒' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" :icon="View" @click="handleView(scope.row)" :loading="exportLoading"
                     v-hasPermi="['infra:job:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 调度日志详细 -->
    <el-dialog title="调度日志详细" v-model="open" width="700px" append-to-body>

      <div style="display: flex; flex-direction: column">
        <div style="display: flex; flex-direction: row">
          <div style="width: 90px; text-align: right; font-weight: bold">日志编号:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.id"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">任务编号:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.jobId"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">处理器的名字:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.handlerName"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">处理器的参数:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.handlerParam"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">第几次执行:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.executeIndex"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">执行时间:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="proxy.parseTime(form.beginTime) + ' ~ ' + proxy.parseTime(form.endTime)"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">任务状态:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center">
            <dict-tag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="form.status" />
          </div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">执行结果:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.result"></div>
        </div>
      </div>

      <template #footer>
        <el-button @click="open = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref, getCurrentInstance} from "vue"
import {useRoute} from "vue-router";
import * as JobLogApi from "@/api/infra/JobLog.js"
import {ElMessage} from "element-plus";
import {Download, Search, Refresh, View} from "@element-plus/icons-vue"
import {DICT_TYPE, getDictDatas} from "@/utils/dict";
import DictTag from "@/components/DictTag"

const {proxy} = getCurrentInstance()
const queryForm = ref(null)
const route = useRoute()

const loading = ref(true)
const exportLoading = ref(false)
const showSearch = ref(true)
const total = ref(0)
const list = ref([])
const open = ref(false)
const form = ref({})
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  handlerName: null,
  beginTime: null,
  endTime: null,
  status: null,
})

function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

function resetQuery() {
  queryForm.value?.resetFields()
  handleQuery();
}

function handleView(row) {
  form.value = row
  open.value = true
}

function handleExport() {
  // 处理查询参数
  let params = {...queryParams.value,
    beginTime: queryParams.value.beginTime ? queryParams.value.beginTime + ' 00:00:00' : undefined,
    endTime: queryParams.value.endTime ? queryParams.value.endTime + ' 23:59:59' : undefined,
  };
  params.pageNo = undefined;
  params.pageSize = undefined;
  // 执行导出
  proxy.$modal.confirm('是否确认导出所有定时任务日志数据项?').then(() => {
    exportLoading.value = true;
    return JobLogApi.exportJobLogExcel(params);
  }).then(response => {
    proxy.$download.excel(response, '定时任务日志.xls');
    exportLoading.value = false;
  }).catch(e => {
    ElMessage.error(`导出失败: ${e.message}`)
    console.error(e)
  });
}
onMounted(() => {
  queryParams.value.jobId = route.query && route.query.jobId;
  getList()
})

function getList() {
  loading.value = true
  JobLogApi.getJobLogPage({
    ...queryParams.value,
    beginTime: queryParams.value.beginTime ? queryParams.value.beginTime + ' 00:00:00' : undefined,
    endTime: queryParams.value.endTime ? queryParams.value.endTime + ' 23:59:59' : undefined,
  }).then(response => {
    list.value = response.data.list;
    total.value = response.data.total;
    loading.value = false;
  })
}

</script>

<style scoped>

</style>