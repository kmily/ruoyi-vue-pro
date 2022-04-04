<template>
  <div class="app-container">
    <doc-alert title="SaaS 多租户" url="https://doc.iocoder.cn/saas-tenant/"/>
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="租户名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入租户名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="联系人" prop="contactName">
        <el-input v-model="queryParams.contactName" placeholder="请输入联系人" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="联系手机" prop="contactMobile">
        <el-input v-model="queryParams.contactMobile" placeholder="请输入联系手机" clearable
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="租户状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择租户状态" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd"
                   v-hasPermi="['system:tenant:create']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="small" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['system:tenant:export']">导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"/>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="租户编号" align="center" prop="id"/>
      <el-table-column label="租户名" align="center" prop="name"/>
      <el-table-column label="租户套餐" align="center" prop="packageId">
        <template #default="scope">
          <el-tag v-if="scope.row.packageId === 0" type="danger">系统租户</el-tag>
          <el-tag v-else> {{ getPackageName(scope.row.packageId) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="联系人" align="center" prop="contactName"/>
      <el-table-column label="联系手机" align="center" prop="contactMobile"/>
      <el-table-column label="账号额度" align="center" prop="accountCount">
        <template #default="scope">
          <el-tag> {{ scope.row.accountCount }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="过期时间" align="center" prop="expireTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="绑定域名" align="center" prop="domain" width="180"/>
      <el-table-column label="租户状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:tenant:update']">修改
          </el-button>
          <el-button size="small" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:tenant:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo"
                v-model::limit="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="租户名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入租户名"/>
        </el-form-item>
        <el-form-item label="租户套餐" prop="packageId">
          <el-select v-model="formData.packageId" placeholder="请选择租户套餐" clearable size="small">
            <el-option v-for="item in packageList" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="formData.contactName" placeholder="请输入联系人"/>
        </el-form-item>
        <el-form-item label="联系手机" prop="contactMobile">
          <el-input v-model="formData.contactMobile" placeholder="请输入联系手机"/>
        </el-form-item>
        <el-form-item v-if="formData.id === undefined" label="用户名称" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名称"/>
        </el-form-item>
        <el-form-item v-if="formData.id === undefined" label="用户密码" prop="password">
          <el-input v-model="formData.password" placeholder="请输入用户密码" type="password" show-password/>
        </el-form-item>
        <el-form-item label="账号额度" prop="accountCount">
          <el-input-number v-model="formData.accountCount" placeholder="请输入账号额度" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker clearable size="small" v-model="formData.expireTime" type="date"
                          value-format="timestamp" placeholder="请选择过期时间"/>
        </el-form-item>
        <el-form-item label="绑定域名" prop="domain">
          <el-input v-model="formData.domain" placeholder="请输入绑定域名"/>
        </el-form-item>
        <el-form-item label="租户状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                      :key="dict.value" :label="parseInt(dict.value)">{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Tenant">
import {
  createTenant,
  updateTenant,
  deleteTenant,
  getTenant,
  getTenantPage,
  exportTenantExcel
} from "@/api/system/tenant";
import {CommonStatusEnum} from '@/utils/constants'
import {getTenantPackageList} from "@/api/system/tenantPackage";


const {proxy} = getCurrentInstance();

const title = ref("");
const loading = ref(true);// 遮罩层
const exportLoading = ref(false);// 导出遮罩层
const showSearch = ref(true);// 显示搜索条件
const total = ref(0);// 总条数
const list = ref([]);// 表格数据
const open = ref(false);// 是否显示弹出层
const dateRange = ref([]);// 日期范围
const packageList = ref([]); //套餐选中范围
const data = reactive({
  // 查询参数
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    name: null,
    contactName: null,
    contactMobile: null,
    status: undefined,
  },
  // 表单参数
  formData: {},
  // 表单校验
  rules: {
    name: [{required: true, message: "租户名不能为空", trigger: "blur"}],
    packageId: [{required: true, message: "租户套餐不能为空", trigger: "blur"}],
    contactName: [{required: true, message: "联系人不能为空", trigger: "blur"}],
    status: [{required: true, message: "租户状态不能为空", trigger: "blur"}],
    accountCount: [{required: true, message: "账号额度不能为空", trigger: "blur"}],
    expireTime: [{required: true, message: "过期时间不能为空", trigger: "blur"}],
    domain: [{required: true, message: "绑定域名不能为空", trigger: "blur"}],
    username: [{required: true, message: "用户名称不能为空", trigger: "blur"}],
    password: [{required: true, message: "用户密码不能为空", trigger: "blur"}],
  }
});
const {queryParams, formData, rules} = toRefs(data);


/** 查询列表 */
function getList() {
  loading.value = true;
  // 处理查询参数
  let params = {...queryParams.value};
  proxy.addBeginAndEndTime(params, dateRange.value, 'createTime');
  // 执行查询
  getTenantPage(params).then(response => {
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
    name: undefined,
    packageId: undefined,
    contactName: undefined,
    contactMobile: undefined,
    accountCount: undefined,
    expireTime: undefined,
    domain: undefined,
    status: CommonStatusEnum.ENABLE,
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
  dateRange.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加租户";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id;
  getTenant(id).then(response => {
    formData.value = response.data;
    open.value = true;
    title.value = "修改租户";
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
      updateTenant(formData.value).then(response => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getList();
      });
      return;
    }
    // 添加的提交
    createTenant(formData.value).then(response => {
      proxy.$modal.msgSuccess("新增成功");
      open.vlaue = false;
      getList();
    });
  });
}


/** 删除按钮操作 */
function handleDelete(row) {
  const id = row.id;
  proxy.$modal.confirm('是否确认删除租户编号为"' + id + '"的数据项?').then(function () {
    return deleteTenant(id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  // 处理查询参数
  let params = {...queryParams.vlaue};
  proxy.addBeginAndEndTime(params.value, dateRange, 'createTime');
  // 执行导出
  proxy.$modal.confirm('是否确认导出所有租户数据项?').then(() => {
    exportLoading.value = true;
    return exportTenantExcel(params);
  }).then(response => {
    proxy.$download.excel(response, '租户.xls');
    exportLoading.value = false;
  }).catch(() => {
  });
}

/** 套餐名格式化 */
function getPackageName(packageId) {
  for (const item of packageList.value) {
    if (item.id === packageId) {
      return item.name;
    }
  }
  return '未知套餐';
}


function initData() {
  // 获得租户套餐列表
  getTenantPackageList().then(response => {
    packageList.value = response.data;
  })
  getList();


}

initData();
</script>
