<template>
  <div class="app-container">
    <doc-alert title="SaaS 多租户" url="https://doc.iocoder.cn/saas-tenant/"/>
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="套餐名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入套餐名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd"
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
        <el-button type="primary" plain icon="Plus" size="small" @click="handleAdd"
                   v-hasPermi="['system:tenant-package:create']">新增
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="套餐编号" align="center" prop="id" width="120"/>
      <el-table-column label="套餐名" align="center" prop="name"/>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:tenant-package:update']">修改
          </el-button>
          <el-button size="small" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:tenant-package:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="套餐名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入套餐名"/>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</el-checkbox>
          <el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" node-key="id"
                   :check-strictly="menuCheckStrictly" empty-text="加载中，请稍后" :props="defaultProps"></el-tree>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                      :key="dict.value" :label="parseInt(dict.value)">{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="TenantPackage">
import {
  createTenantPackage,
  updateTenantPackage,
  deleteTenantPackage,
  getTenantPackage,
  getTenantPackagePage
} from "@/api/system/tenantPackage";
import {CommonStatusEnum, SystemMenuTypeEnum} from "@/utils/constants";
import {listSimpleMenus} from "@/api/system/menu";

const {proxy} = getCurrentInstance();

const title = ref("");
const loading = ref(true);// 遮罩层
const exportLoading = ref(false);// 导出遮罩层
const showSearch = ref(true);// 显示搜索条件
const total = ref(0);// 总条数
const list = ref([]);// 表格数据
const open = ref(false);// 是否显示弹出层
const dateRange = ref([]);// 日期范围
const menuOptions = ref([]); //菜单列表
const menuExpand = ref(false);
const menuNodeAll = ref(false);
const menuCheckStrictly = ref(true);
const data = reactive({
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    name: null,
    status: null,
    remark: null,
  },
  // 表单参数
  formData: {},

  defaultProps: {
    label: "name",
    children: "children"
  },

  // 表单校验
  rules: {
    name: [{required: true, message: "套餐名不能为空", trigger: "blur"}],
    status: [{required: true, message: "状态不能为空", trigger: "blur"}],
    menuIds: [{required: true, message: "关联的菜单编号不能为空", trigger: "blur"}],
  }
});
const {queryParams, formData, defaultProps, rules} = toRefs(data);


/** 查询列表 */
function getList() {
  loading.value = true;
  // 处理查询参数
  let params = {...queryParams.value};
  proxy.addBeginAndEndTime(params, dateRange, 'createTime');
  // 执行查询
  getTenantPackagePage(params).then(response => {
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
  // 菜单选择重置
  if (proxy.$refs.menu !== undefined) {
    proxy.$refs.menu.setCheckedKeys([]);
  }
  menuExpand.value = false;
  menuNodeAll.value = false;
  menuCheckStrictly.value = true;
  // 表单重置
  formData.value = {
    id: undefined,
    name: undefined,
    status: CommonStatusEnum.ENABLE,
    remark: undefined,
    menuIds: undefined,
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
  // 表单重置
  proxy.resetForm("queryForm");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加租户套餐";
  // 设置为非严格，继续使用半选中
  menuCheckStrictly.value = false;
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id;
  open.value = true;
  title.value = "修改租户套餐";
  // 获得菜单列表
  getTenantPackage(id).then(response => {
    formData.value = response.data;
    // 设置菜单项
    // 设置为严格，避免设置父节点自动选中子节点，解决半选中问题
    menuCheckStrictly.value = true
    // 设置选中
    proxy.$refs.menu.setCheckedKeys(response.data.menuIds);
    // 设置为非严格，继续使用半选中
    menuCheckStrictly.value = false
  });
}

/** 获得菜单 */
function getMenus() {
  listSimpleMenus().then(response => {
    // 处理 menuOptions 参数
    menuOptions.value = [];
    // 只需要配置
    menuOptions.value.push(...proxy.handleTree(response.data, "id"));
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
      updateTenantPackage({
        ...formData.value,
        menuIds: [...proxy.$refs.menu.getCheckedKeys(), ...proxy.$refs.menu.getHalfCheckedKeys()]
      }).then(response => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getList();
      });
      return;
    }
    // 添加的提交
    createTenantPackage({
      ...formData.value,
      menuIds: [...proxy.$refs.menu.getCheckedKeys(), ...proxy.$refs.menu.getHalfCheckedKeys()]
    }).then(response => {
      proxy.$modal.msgSuccess("新增成功");
      open.value = false;
      getList();
    });
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const id = row.id;
  proxy.$modal.confirm('是否确认删除租户套餐编号为"' + id + '"的数据项?').then(function () {
    return deleteTenantPackage(id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

// 树权限（展开/折叠）
function handleCheckedTreeExpand(value, type) {
  let treeList = menuOptions.value;
  for (let i = 0; i < treeList.length; i++) {
    proxy.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
  }
}

// 树权限（全选/全不选）
function handleCheckedTreeNodeAll(value) {
  proxy.$refs.menu.setCheckedNodes(value ? menuOptions.value : []);
}

// 树权限（父子联动）
function handleCheckedTreeConnect(value) {
  formData.value.menuCheckStrictly = value;
}

function initData() {
  getList();
  getMenus();

}

initData();
</script>
