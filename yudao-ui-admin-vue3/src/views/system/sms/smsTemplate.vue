<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="150px">
      <el-form-item label="短信类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择短信类型" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择开启状态" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="模板编码" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入模板编码" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="短信 API 的模板编号" prop="apiTemplateId">
        <el-input v-model="queryParams.apiTemplateId" placeholder="请输入短信 API 的模板编号" clearable
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="短信渠道" prop="channelId">
        <el-select v-model="queryParams.channelId" placeholder="请选择短信渠道" clearable>
          <el-option v-for="channel in channelOptions"
                     :key="channel.id" :value="channel.id"
                     :label="channel.signature + '【' + getDictDataLabel(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE, channel.code) + '】'"/>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRangeCreateTime" style="width: 240px" value-format="YYYY-MM-DD"
                        type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd"
                   v-hasPermi="['system:sms-template:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:sms-template:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="模板编码" align="center" prop="code"/>
      <el-table-column label="模板名称" align="center" prop="name"/>
      <el-table-column label="模板内容" align="center" prop="content" width="300"/>
      <el-table-column label="短信类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="开启状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="短信 API 的模板编号" align="center" prop="apiTemplateId" width="180"/>
      <el-table-column label="短信渠道" align="center" width="120">
        <template #default="scope">
          <div>{{ formatChannelSignature(scope.row.channelId) }}</div>
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.channelCode"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template #default="scope">
          <el-button size="small" type="text" icon="Share" @click="handleSendSms(scope.row)"
                     v-hasPermi="['system:sms-template:send-sms']">测试
          </el-button>
          <el-button size="small" type="text" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:sms-template:update']">修改
          </el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:sms-template:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="formData" :rules="rules" label-width="140px">
        <el-form-item label="短信渠道编号" prop="channelId">
          <el-select v-model="formData.channelId" placeholder="请选择短信渠道编号">
            <el-option v-for="channel in channelOptions"
                       :key="channel.id" :value="channel.id"
                       :label="channel.signature + '【' + getDictDataLabel(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE, channel.code) + '】'"/>
          </el-select>
        </el-form-item>
        <el-form-item label="短信类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择短信类型">
            <el-option v-for="dict in getDictDatas(DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="模板编号" prop="code">
          <el-input v-model="formData.code" placeholder="请输入模板编号"/>
        </el-form-item>
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入模板名称"/>
        </el-form-item>
        <el-form-item label="模板内容" prop="content">
          <el-input type="textarea" v-model="formData.content" placeholder="请输入模板内容"/>
        </el-form-item>
        <el-form-item label="开启状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                      :key="dict.value" :label="parseInt(dict.value)">{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="短信 API 模板编号" prop="apiTemplateId">
          <el-input v-model="formData.apiTemplateId" placeholder="请输入短信 API 的模板编号"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 对话框(发送短信) -->
    <el-dialog title="测试发送短信" v-model="sendSmsOpen" width="500px" append-to-body>
      <el-form ref="sendSmsForm" :model="sendSmsForm" :rules="sendSmsRules" label-width="140px">
        <el-form-item label="模板内容" prop="content">
          <el-input v-model="sendSmsForm.content" type="textarea" placeholder="请输入模板内容" readonly/>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="sendSmsForm.mobile" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item v-for="param in sendSmsForm.params" :label="'参数 {' + param + '}'"
                      :prop="'templateParams.' + param">
          <el-input v-model="sendSmsForm.templateParams[param]" :placeholder="'请输入 ' + param + ' 参数'"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitSendSmsForm">确 定</el-button>
          <el-button @click="cancelSendSms">取 消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="SmsTemplate">
import {
  createSmsTemplate, updateSmsTemplate, deleteSmsTemplate, getSmsTemplate, getSmsTemplatePage,
  exportSmsTemplateExcel, sendSms
} from "@/api/system/sms/smsTemplate";
import {getSimpleSmsChannels} from "@/api/system/sms/smsChannel";

const {proxy} = getCurrentInstance();

const loading = ref(true);// 遮罩层
const exportLoading = ref(false);// 导出遮罩层
const showSearch = ref(true);// 显示搜索条件
const title = ref(""); // 弹窗标题
const total = ref(0);// 总条数
const list = ref([]);// 表格数据
const open = ref(false);// 是否显示弹出层
const dateRangeCreateTime = ref([]);// 日期范围
const channelOptions = ref([]);

const sendSmsOpen = ref(false);  // 发送短信

const data = reactive({
  // 查询参数
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    type: null,
    status: null,
    code: null,
    content: null,
    apiTemplateId: null,
    channelId: null,
  },
  // 表单校验
  rules: {
    type: [{required: true, message: "短信类型不能为空", trigger: "change"}],
    status: [{required: true, message: "开启状态不能为空", trigger: "blur"}],
    code: [{required: true, message: "模板编码不能为空", trigger: "blur"}],
    name: [{required: true, message: "模板名称不能为空", trigger: "blur"}],
    content: [{required: true, message: "模板内容不能为空", trigger: "blur"}],
    apiTemplateId: [{required: true, message: "短信 API 的模板编号不能为空", trigger: "blur"}],
    channelId: [{required: true, message: "短信渠道编号不能为空", trigger: "change"}],
  },
  sendSmsForm: {
    mobile: undefined,
    params: [], // 模板的参数列表
  },
  sendSmsRules: {
    mobile: [{required: true, message: "手机不能为空", trigger: "blur"}],
    templateCode: [{required: true, message: "手机不能为空", trigger: "blur"}],
    templateParams: {}
  },
  formData: {},
});

const {queryParams, rules, sendSmsForm, sendSmsRules, formData} = toRefs(data);

/** 查询列表 */
function getList() {
  loading.value = true;
  // 处理查询参数
  let params = {...queryParams.value};
  proxy.addBeginAndEndTime(params, dateRangeCreateTime.value, 'createTime');
  // 执行查询
  getSmsTemplatePage(params).then(response => {
    list.value = response.data.list;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  formData.value = {
    id: undefined,
    type: undefined,
    status: undefined,
    code: undefined,
    name: undefined,
    content: undefined,
    remark: undefined,
    apiTemplateId: undefined,
    channelId: undefined,
  };
  proxy.resetForm("form");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRangeCreateTime.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加短信模板";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id;
  getSmsTemplate(id).then(response => {
    formData.value = response.data;
    open.value = true;
    title.value = "修改短信模板";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["form"].validate(valid => {
    if (!valid) {
      return;
    }
    // 修改的提交
    if (formData.value.id != null) {
      updateSmsTemplate(formData.value).then(response => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getList();
      });
      return;
    }
    // 添加的提交
    createSmsTemplate(formData.value).then(response => {
      proxy.$modal.msgSuccess("新增成功");
      open.value = false;
      getList();
    });
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const id = row.id;
  proxy.$modal.confirm('是否确认删除短信模板编号为"' + id + '"的数据项?').then(function () {
    return deleteSmsTemplate(id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  // 处理查询参数
  let params = {...queryParams.value};
  params.pageNo = undefined;
  params.pageSize = undefined;
  proxy.addBeginAndEndTime(params, dateRangeCreateTime, 'createTime');
  // 执行导出
  proxy.$modal.confirm('是否确认导出所有短信模板数据项?', "警告").then(() => {
    exportLoading.value = true;
    return exportSmsTemplateExcel(params);
  }).then(response => {
    proxy.$download.excel(response, '短信模板.xls');
    exportLoading.value = false;
  }).catch(() => {
  });
}

/** 发送短息按钮 */
function handleSendSms(row) {
  resetSendSms(row);
  // 设置参数
  sendSmsForm.value.content = row.content;
  sendSmsForm.value.params = row.params;
  sendSmsForm.value.templateCode = row.code;
  sendSmsForm.value.templateParams = row.params.reduce(function (obj, item) {
    obj[item] = undefined;
    return obj;
  }, {});
  // 根据 row 重置 rules
  sendSmsRules.value.templateParams = row.params.reduce(function (obj, item) {
    obj[item] = {required: true, message: '参数 ' + item + " 不能为空", trigger: "change"};
    return obj;
  }, {});
  // 设置打开
  sendSmsOpen.value = true;
}

/** 重置发送短信的表单 */
function resetSendSms() {
  // 根据 row 重置表单
  sendSmsForm.value = {
    content: undefined,
    params: undefined,
    mobile: undefined,
    templateCode: undefined,
    templateParams: {}
  };
  proxy.resetForm("sendSmsForm");
}

/** 取消发送短信 */
function cancelSendSms() {
  sendSmsOpen.value = false;
  resetSendSms();
}

/** 提交按钮 */
function submitSendSmsForm() {
  proxy.$refs["sendSmsForm"].validate(valid => {
    if (!valid) {
      return;
    }
    // 添加的提交
    sendSms(sendSmsForm).then(response => {
      proxy.$modal.msgSuccess("提交发送成功！发送结果，见发送日志编号：" + response.data);
      sendSmsOpen.value = false;
    });
  });
}

/** 格式化短信渠道 */
function formatChannelSignature(channelId) {
  for (const channel of channelOptions.value) {
    if (channel.id === channelId) {
      return channel.signature;
    }
  }
  return '找不到签名：' + channelId;
}

function initData() {

  // 获得短信渠道
  getSimpleSmsChannels().then(response => {
    channelOptions.value = response.data;
  })
  getList();
}

initData();

</script>
