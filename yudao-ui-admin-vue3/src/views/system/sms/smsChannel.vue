<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="短信签名" prop="signature">
        <el-input v-model="queryParams.signature" placeholder="请输入短信签名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="启用状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择启用状态" clearable>
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRangeCreateTime" style="width: 240px" value-format="YYYY-MM-DD"
                        type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" />
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
                   v-hasPermi="['system:sms-channel:create']">新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="短信签名" align="center" prop="signature" />
      <el-table-column label="渠道编码" align="center" prop="code">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.code"/>
        </template>
      </el-table-column>>
      <el-table-column label="启用状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="短信 API 的账号" align="center" prop="apiKey" />
      <el-table-column label="短信 API 的秘钥" align="center" prop="apiSecret" />
      <el-table-column label="短信发送回调 URL" align="center" prop="callbackUrl" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ proxy.parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="mini" type="text" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:sms-channel:update']">修改</el-button>
          <el-button size="mini" type="text" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:sms-channel:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="短信签名" prop="signature">
          <el-input v-model="form.signature" placeholder="请输入短信签名" />
        </el-form-item>
        <el-form-item label="渠道编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入渠道编码" />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in proxy.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                      :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="短信 API 的账号" prop="apiKey">
          <el-input v-model="form.apiKey" placeholder="请输入短信 API 的账号" />
        </el-form-item>
        <el-form-item label="短信 API 的秘钥" prop="apiSecret">
          <el-input v-model="form.apiSecret" placeholder="请输入短信 API 的秘钥" />
        </el-form-item>
        <el-form-item label="短信发送回调 URL" prop="callbackUrl">
          <el-input v-model="form.callbackUrl" placeholder="请输入短信发送回调 URL" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="SmsChannel">
import { createSmsChannel, updateSmsChannel, deleteSmsChannel, getSmsChannel, getSmsChannelPage} from "@/api/system/sms/smsChannel";
import { SystemMenuTypeEnum } from '@/utils/constants';

const {proxy} = getCurrentInstance();
const loading = ref(true);// 遮罩层
const showSearch = ref(true);// 显示搜索条件
const total = ref(0);// 总条数
const title = ref("");
const list = ref([]);// 表单数据
const open = ref(false);// 是否显示弹出层
const dateRangeCreateTime = ref([]);
const data = reactive({
  // 查询参数
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    signature: null,
    status: null,
  },
  // 表单参数
  form: {},
  // 表单校验
  rules: {
    signature: [{ required: true, message: "短信签名不能为空", trigger: "blur" }],
    code: [{ required: true, message: "渠道编码不能为空", trigger: "blur" }],
    status: [{ required: true, message: "启用状态不能为空", trigger: "blur" }],
    apiKey: [{ required: true, message: "短信 API 的账号不能为空", trigger: "blur" }],
  }
});
const {queryParams,form, rules} = toRefs(data);

function getList(){
  loading.value = true;
  // 处理查询参数
  let params = {...queryParams.value};
  proxy.addBeginAndEndTime(params, dateRangeCreateTime.value, 'createTime');
  console.log(params);
  // 执行查询
  getSmsChannelPage(params).then(response => {
    list.value = response.data.list;
    total.value = response.data.total;
    loading.value = false;
  });
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

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    id: undefined,
    signature: undefined,
    code: undefined,
    status: undefined,
    remark: undefined,
    apiKey: undefined,
    apiSecret: undefined,
    callbackUrl: undefined,
  };
  proxy.resetForm("queryForm");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加短信渠道";
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const id = row.id;
    getSmsChannel(id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改短信渠道";
    });
}

/** 提交表单 */
function submitForm() {
  proxy.$refs["formRef"].validate(valid => {
      if (!valid) {
        return;
      }
      // 修改的提交
      if (form.value.id !== undefined) {
        updateSmsChannel(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
        return;
      }
      // 添加的提交
      createSmsChannel(form.value).then(response => {
        proxy.$modal.msgSuccess("新增成功");
        open.value = false;
        getList();
      });
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const id = row.id;
  proxy.$modal.confirm('是否确认删除短信渠道编号为"' + id + '"的数据项?').then(function() {
    return deleteSmsChannel(id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
</script>
