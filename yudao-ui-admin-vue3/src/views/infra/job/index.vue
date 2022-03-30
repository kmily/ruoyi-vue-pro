<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryFormRef" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="任务名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入任务名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_JOB_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="处理器的名字" prop="handlerName">
        <el-input v-model="queryParams.handlerName" placeholder="请输入处理器的名字" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain :icon="Plus" size="small" @click="handleAdd"
                   v-hasPermi="['infra:job:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" :icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['infra:job:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" :icon="Operation" size="small" @click="handleJobLog"
                   v-hasPermi="['infra:job:query']">执行日志</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="loadList"></right-toolbar>
    </el-row>

    <el-table :height="tableScrollHeight" v-loading="loading" :data="jobList">
      <el-table-column label="任务编号" align="center" prop="id" />
      <el-table-column label="任务名称" align="center" prop="name" />
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>>
      <el-table-column label="处理器的名字" align="center" prop="handlerName" />
      <el-table-column label="处理器的参数" align="center" prop="handlerParam" />
      <el-table-column label="CRON 表达式" align="center" prop="cronExpression" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" :icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['infra:job:update']">修改</el-button>
          <el-button size="small" type="text" :icon="Check" @click="handleChangeStatus(scope.row, true)"
                     v-if="scope.row.status === InfJobStatusEnum.STOP" v-hasPermi="['infra:job:update']">开启</el-button>
          <el-button size="small" type="text" :icon="Close" @click="handleChangeStatus(scope.row, false)"
                     v-if="scope.row.status === InfJobStatusEnum.NORMAL" v-hasPermi="['infra:job:update']">暂停</el-button>
          <el-button size="small" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['infra:job:delete']">删除</el-button>
          <el-dropdown size="small" @command="(command) => handleCommand(command, scope.row)"
                       v-hasPermi="['infra:job:trigger', 'infra:job:query']">
            <el-button style="margin-left: 12px" size="small" type="text" :icon="DArrowRight" v-hasPermi="['infra:job:update']">更多</el-button>

            <template #dropdown>
              <el-dropdown-menu>
                <!--  FIXME： VUE3不支持在此处写自定义指令，因dropdown和trigger source有了完整的权限控制，因此移除下列几项的自定义指令权限控制。若有大神发现更好的方法也可以一起讨论。-->
                <el-dropdown-item command="handleRun" :icon="CaretRight">执行一次</el-dropdown-item>
                <el-dropdown-item command="handleView" :icon="View">任务详细</el-dropdown-item>
                <el-dropdown-item command="handleJobLog" :icon="Operation">调度日志</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="loadList"/>

    <!-- 添加或修改定时任务对话框 -->
    <el-dialog v-model="open" :title="title" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="任务名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="处理器的名字" prop="handlerName">
          <el-input v-model="form.handlerName" placeholder="请输入处理器的名字" :readonly="form.id !== undefined" />
        </el-form-item>
        <el-form-item label="处理器的参数" prop="handlerParam">
          <el-input v-model="form.handlerParam" placeholder="请输入处理器的参数" />
        </el-form-item>
        <el-form-item label="CRON 表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" placeholder="请输入CRON 表达式">
            <template #append>
              <el-button :icon="Timer" type="primary" @click="handleShowCron">生成表达式</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="重试次数" prop="retryCount">
          <el-input v-model="form.retryCount" placeholder="请输入重试次数。设置为 0 时，不进行重试" />
        </el-form-item>
        <el-form-item label="重试间隔" prop="retryInterval">
          <el-input v-model="form.retryInterval" placeholder="请输入重试间隔，单位：毫秒。设置为 0 时，无需间隔" />
        </el-form-item>
        <el-form-item label="监控超时时间" prop="monitorTimeout">
          <el-input v-model="form.monitorTimeout" placeholder="请输入监控超时时间，单位：毫秒" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" v-model="openCron" append-to-body custom-class="scrollbar" destroy-on-close>
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>

    <!-- 任务详细 -->
    <el-dialog title="任务详细" v-model="openView" width="700px" append-to-body>
      <div style="display: flex; flex-direction: column">
        <div style="display: flex; flex-direction: row">
          <div style="width: 90px; text-align: right; font-weight: bold">任务编号:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.id"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">任务名称:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.name"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">任务状态:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center">
            <dict-tag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="form.status" />
          </div>
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
          <div style="width: 90px; text-align: right; font-weight: bold">cron表达式:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.cronExpression"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">重试次数:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.retryCount"></div>
        </div>
        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">重试间隔:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.retryInterval + ' 毫秒'"></div>
        </div>

        <div style="display: flex; flex-direction: row; margin-top: 10px">
          <div style="width: 90px; text-align: right; font-weight: bold">监控超时时间:</div>
          <div style="margin-left: 10px; line-height: 1; display: flex; align-items: center" v-text="form.monitorTimeout > 0 ? form.monitorTimeout + ' 毫秒' : '未开启'"></div>
        </div>

        <div style="display: flex; flex-direction: row; margin-top: 10px; height: 100px; align-items: flex-start; align-content: flex-start; justify-content: flex-start">
          <div style="width: 90px; text-align: right; font-weight: bold">后续执行时间:</div>
          <div style="width: calc(700px - 40px - 90px - 10px); box-sizing: border-box; margin-left: 10px; line-height: 1; display: flex; align-items: center; word-break: break-word;" v-html="Array.from(nextTimes, x => proxy.parseTime(x)).join('<br/>')"></div>
        </div>

      </div>

      <template #footer>
        <el-button @click="openView = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref, getCurrentInstance, computed} from "vue"
import DictTag from "@/components/DictTag"
import { InfraJobStatusEnum as InfJobStatusEnum } from "@/utils/constants";
import * as JobApi from "@/api/infra/job.js"
import {ElMessage} from "element-plus"
import Crontab from '@/components/Crontab'
import {
  DArrowRight, Check, Close, Download, Operation, Search, Refresh, Plus, View, CaretRight,
  Edit, Timer
} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import {DICT_TYPE, } from "@/utils/dict";

const tableScrollHeight = computed(() => {
  const formHeight = 42;
  const opHeight = 32;
  const paginationHeight = 65;
  const padding = 40;
  const header = 84;
  return `calc(100vh - ${header + padding + paginationHeight + opHeight + formHeight}px)`;
})

const router = useRouter()

const formRef = ref(null)
const queryFormRef = ref(null)

const {proxy} = getCurrentInstance()

const loading = ref(true)
const exportLoading = ref(false)
const showSearch = ref(true)

const total = ref(0)

const jobList = ref([])
const title = ref("")
const open = ref(false)
const openView = ref(false)
const openCron = ref(false)
const expression = ref("")
const statusOptions = ref([])
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  status: undefined,
  handlerName: undefined,
})
const form = ref({})
const rules = ref({
  name: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
  handlerName: [{ required: true, message: "处理器的名字不能为空", trigger: "blur" }],
  cronExpression: [{ required: true, message: "CRON 表达式不能为空", trigger: "blur" }],
  retryCount: [{ required: true, message: "重试次数不能为空", trigger: "blur" }],
  retryInterval: [{ required: true, message: "重试间隔不能为空", trigger: "blur" }],
})
const  nextTimes = ref([]) // 后续执行时间

async function loadList() {
  try {
    loading.value = true
    const response = await JobApi.jobPage(queryParams.value)
    jobList.value = response.data.list;
    total.value = response.data.total;
  } catch (e) {
    console.log(e)
    ElMessage.error(`加载失败：${e.message}`)
  } finally {
    loading.value = false
  }
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    handlerName: undefined,
    handlerParam: undefined,
    cronExpression: undefined,
    retryCount: undefined,
    retryInterval: undefined,
    monitorTimeout: undefined,
  };
  nextTimes.value = [];
  formRef.value?.resetFields();
}

function handleQuery() {
  queryParams.value.pageNo = 1;
  loadList();
}

function resetQuery() {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/**
 * 立即执行一次
 */
function handleRun(row) {
  proxy.$modal.confirm(`确认要立即执行一次"${row.name}"任务吗?`).then(function() {
    return JobApi.runJob(row.id);
  }).then(() => {
    proxy.$modal.msgSuccess("执行成功");
  }).catch(() => {});
}

/**
 * 任务详细信息
 */
function handleView(row) {
  JobApi.getJob(row.id).then(response => {
    form.value = response.data;
    openView.value = true;
  });
  // 获取下一次执行时间
  JobApi.getJobNextTimes(row.id).then(response => {
    nextTimes.value = response.data;
  });
}

/**
 * cron表达式按钮操作
 */
function handleShowCron() {
  expression.value = form.value.cronExpression;
  openCron.value = true;
}

/**
 * 确定后回传值
 */
function crontabFill(value) {
  form.value.cronExpression = value;
}

/**
 * 任务日志列表查询
 */
function handleJobLog(row) {
  if (row?.id) {
    router.push({
      path:"/infra/job/log",
      query:{
        jobId: row.id
      }
    });
  } else {
    router.push("/infra/job/log");
  }
}

/**
 * 新增按钮操作
 */
function handleAdd() {
  reset();
  open.value = true
  title.value = "添加任务"
}

/**
 * 修改按钮操作
 */
function handleUpdate(row) {
  reset()
  const id = row.id
  JobApi.getJob(id).then(response => {
    form.value = response.data
    title.value = "修改任务"
    open.value = true
  })
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      if (this.form.id !== undefined) {
        JobApi.updateJob(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          loadList()
        })
      } else {
        JobApi.addJob(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          loadList()
        })
      }
    }
  })
}

/**
 * 删除按钮操作
 */
function handleDelete(row) {
  const ids = row.id;
  proxy.$modal.confirm(`是否确认删除定时任务编号为"${ids}"的数据项?`).then(() => {
    return JobApi.delJob(ids)
  }).then(() => {
    loadList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(e => {
    console.error(e)
    ElMessage.error(`删除失败: ${e.message}`)
  })
}

/**
 * 更新状态操作
 */
function handleChangeStatus(row, opened) {
  const id = row.id;
  let status = opened ? InfJobStatusEnum.NORMAL : InfJobStatusEnum.STOP;
  let statusStr = opened ? '开启' : '关闭';
  proxy.$modal.confirm(`是否确认${statusStr}定时任务编号为"${id}"的数据项?`).then(() => {
    return JobApi.updateJobStatus(id, status);
  }).then(() => {
    loadList()
    proxy.$modal.msgSuccess(statusStr + "成功");
  }).catch(() => {
    console.error(e)
    ElMessage.error(`更新状态失败: ${e.message}`)
  });
}

function handleCommand(command, row) {
  switch (command) {
    case "handleRun":
      handleRun(row);
      break;
    case "handleView":
      handleView(row);
      break;
    case "handleJobLog":
      handleJobLog(row);
      break;
    default:
      break;
  }
}

function handleExport() {
  const queryParams = this.queryParams;
  proxy.$modal.confirm("是否确认导出所有定时任务数据项?").then(() => {
    exportLoading.value = true;
    return JobApi.exportJob(queryParams);
  }).then(response => {
    proxy.$download.excel(response, '定时任务.xls');
    exportLoading.value = false;
  }).catch(() => {});
}

onMounted( () => {
  loadList()
})


</script>

<style scoped>

</style>