<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="queryParams.mobile" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="短信渠道" prop="channelId">
        <el-select v-model="queryParams.channelId" placeholder="请选择短信渠道" clearable>
          <el-option v-for="channel in channelOptions"
                     :key="channel.id" :value="channel.id"
                     :label="channel.signature + '【' + getDictDataLabel(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE, channel.code) + '】'"/>
        </el-select>
      </el-form-item>
      <el-form-item label="模板编号" prop="templateId">
        <el-input v-model="queryParams.templateId" placeholder="请输入模板编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="发送状态" prop="sendStatus">
        <el-select v-model="queryParams.sendStatus" placeholder="请选择发送状态" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.SYSTEM_SMS_SEND_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间">
        <el-date-picker v-model="dateRangeSendTime" style="width: 240px" value-format="yyyy-MM-dd"
                        type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
      </el-form-item>
      <el-form-item label="接收状态" prop="receiveStatus">
        <el-select v-model="queryParams.receiveStatus" placeholder="请选择接收状态" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="接收时间">
        <el-date-picker v-model="dateRangeReceiveTime" style="width: 240px" value-format="yyyy-MM-dd"
                        type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd"-->
      <!--                   v-hasPermi="['system:sms-log:create']">新增-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button type="warning"  icon="el-icon-download" size="small" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['system:sms-log:export']">导出
        </el-button>

      </el-col>
      <right-toolbar :showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="mobile" width="120">
        <template #default="scope">
          <div>{{ scope.row.mobile }}</div>
          <div v-if="scope.row.userType && scope.row.userId">
            <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType"/>
            {{ '(' + scope.row.userId + ')' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="短信内容" align="center" prop="templateContent" width="300"/>
      <el-table-column label="发送状态" align="center" width="180">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_SEND_STATUS" :value="scope.row.sendStatus"/>
          <div>{{ parseTime(scope.row.sendTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column label="接收状态" align="center" width="180">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS" :value="scope.row.receiveStatus"/>
          <div>{{ parseTime(scope.row.receiveTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column label="短信渠道" align="center" width="120">
        <template #default="scope">
          <div>{{ formatChannelSignature(scope.row.channelId) }}</div>
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.channelCode"/>
        </template>
      </el-table-column>
      <el-table-column label="模板编号" align="center" prop="templateId"/>
      <el-table-column label="短信类型" align="center" prop="templateType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="scope.row.templateType"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" icon="el-icon-view" @click="handleView(scope.row,scope.index)"
                     v-hasPermi="['system:sms-log:query']">详细
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 短信日志详细 -->
    <el-dialog title="短信日志详细" v-model="open" width="700px" append-to-body>
      <el-form ref="form" :model="formData" label-width="140px" size="smalls">
        <el-row>
          <el-col :span="24">
            <el-form-item label="日志主键：">{{ formData.id }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="短信渠道：">
              {{ formatChannelSignature(formData.channelId) }}
              <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="formData.channelCode"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="短信模板：">
              {{ formData.templateId }} | {{ formData.templateCode }}
              <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="formData.templateType"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="API 的模板编号：">{{ formData.apiTemplateId }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="用户信息：">{{ formData.mobile }}
              <span v-if="formData.userType && formData.userId">
                <dict-tag :type="DICT_TYPE.USER_TYPE" :value="formData.userType"/>({{ formData.userId }})
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="短信内容：">{{ formData.templateContent }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="短信参数：">{{ formData.templateParams }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="创建时间：">{{ parseTime(formData.createTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="发送状态：">
              <dict-tag :type="DICT_TYPE.SYSTEM_SMS_SEND_STATUS" :value="formData.sendStatus"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="发送时间：">{{ parseTime(formData.sendTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="发送结果：">{{ formData.sendCode }} | {{ formData.sendMsg }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="API 发送结果：">{{ formData.apiSendCode }} | {{ formData.apiSendMsg }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="API 短信编号：">{{ formData.apiSerialNo }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="API 请求编号：">{{ formData.apiRequestId }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="接收状态：">
              <dict-tag :type="DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS" :value="formData.receiveStatus"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="接收时间：">{{ parseTime(formData.receiveTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="API 接收结果：">{{ formData.apiReceiveCode }} | {{ formData.apiReceiveMsg }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script setup name="SmsLog">
import {getSmsLogPage, exportSmsLogExcel} from "@/api/system/sms/smsLog";
import {getSimpleSmsChannels} from "@/api/system/sms/smsChannel";


const {proxy} = getCurrentInstance();
const title = ref("");
const loading = ref(true);// 遮罩层
const exportLoading = ref(false);// 导出遮罩层
const showSearch = ref(true);// 显示搜索条件
const total = ref(0);// 总条数
const list = ref([]);// 表格数据
const open = ref(false);// 是否显示弹出层
const dateRangeSendTime = ref([]);// 日期范围
const dateRangeReceiveTime = ref([]);// 日期范围
const channelOptions = ref([]);
const data = reactive({
  // 查询参数
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    channelId: null,
    templateId: null,
    mobile: null,
    sendStatus: null,
    receiveStatus: null,
  },
  // 表单参数
  formData: {
    storage: undefined,
    config: {}
  },


});
const {queryParams, formData} = toRefs(data);

/** 查询列表 */
function getList() {
  loading.value = true;
  // 处理查询参数
  let params = {...queryParams.value};
  proxy.addBeginAndEndTime(params, dateRangeSendTime, 'sendTime');
  proxy.addBeginAndEndTime(params, dateRangeReceiveTime, 'receiveTime');
  // 执行查询
  getSmsLogPage(params).then(response => {
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

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRangeSendTime.value = [];
  dateRangeReceiveTime.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}

/** 导出按钮操作 */
function handleExport() {
  // 处理查询参数
  let params = {...queryParams.value};
  params.pageNo = undefined;
  params.pageSize = undefined;
  proxy.addBeginAndEndTime(params, dateRangeSendTime.value, 'sendTime');
  proxy.addBeginAndEndTime(params, dateRangeReceiveTime.value, 'receiveTime');
  // 执行导出
  proxy.$modal.confirm('是否确认导出所有短信日志数据项?').then(() => {
    exportLoading.value = true;
    return exportSmsLogExcel(params);
  }).then(response => {
    proxy.$download.excel(response, '短信日志.xls');
    exportLoading.value = false;
  }).catch(() => {
  });
}

/** 详细按钮操作 */
function handleView(row) {
  open.value = true;
  formData.value = row;
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
