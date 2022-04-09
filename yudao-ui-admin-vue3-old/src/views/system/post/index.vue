<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
         <el-form-item label="岗位编码" prop="postCode">
            <el-input v-model="queryParams.postCode" placeholder="请输入岗位编码" clearable @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item label="岗位名称" prop="postName">
            <el-input v-model="queryParams.postName" placeholder="请输入岗位名称" clearable @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item label="状态" prop="status">
           <el-select v-model="queryParams.status" placeholder="请选择部门状态" clearable>
             <el-option v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"
                        :key="dict.value" :label="dict.label" :value="dict.value"/>
           </el-select>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button type="primary" plain :icon="Plus" @click="handleAdd"
                       v-hasPermi="['system:post:create']">新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="success" plain :icon="Edit" @click="handleUpdate"
                       v-hasPermi="['system:post:update']">修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="warning" plain :icon="Download" @click="handleExport"
                       v-hasPermi="['system:post:export']">导出</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
      </el-row>

      <el-table v-loading="loading" :data="postList">
         <el-table-column label="岗位编号" align="center" prop="id" />
         <el-table-column label="岗位编码" align="center" prop="code" />
         <el-table-column label="岗位名称" align="center" prop="name" />
         <el-table-column label="岗位排序" align="center" prop="sort" />
         <el-table-column label="状态" align="center" prop="status">
            <template #default="scope">
              <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ proxy.parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button type="text" :icon="Edit" @click="handleUpdate(scope.row)"
                          v-hasPermi="['system:post:update']">修改</el-button>
               <el-button type="text" :icon="Delete" @click="handleDelete(scope.row)"
                          v-hasPermi="['system:post:delete']">删除</el-button>
            </template>
         </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize"
                  @pagination="getList" />

      <!-- 添加或修改岗位对话框 -->
     <!-- TODO 字段不对；修改不对； -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
         <el-form ref="postRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="岗位名称" prop="postName">
               <el-input v-model="form.postName" placeholder="请输入岗位名称" />
            </el-form-item>
            <el-form-item label="岗位编码" prop="postCode">
               <el-input v-model="form.postCode" placeholder="请输入编码名称" />
            </el-form-item>
            <el-form-item label="岗位顺序" prop="postSort">
               <el-input-number v-model="form.postSort" controls-position="right" :min="0" />
            </el-form-item>
            <el-form-item label="岗位状态" prop="status">
               <el-radio-group v-model="form.status">
                 <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                           :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
               </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

<script setup>
import {ref, reactive, getCurrentInstance, toRefs, onMounted,} from "vue"
import { listPost, addPost, delPost, getPost, updatePost } from "@/api/system/post";
import {Download, Edit, Plus, Delete} from "@element-plus/icons-vue"
import DictTag from "@/components/DictTag"
import {DICT_TYPE, getDictDatas} from "@/utils/dict"
import {CommonStatusEnum} from "@/utils/constants";

const { proxy } = getCurrentInstance();

const postList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const title = ref("");

const queryRef = ref(null)
/**
 * 备注：未打开对话框时，DOM尚未渲染因此无法获取
 */
const postRef = ref(null)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    postCode: undefined,
    postName: undefined,
    status: undefined
  },
  rules: {
    postName: [{ required: true, message: "岗位名称不能为空", trigger: "blur" }],
    postCode: [{ required: true, message: "岗位编码不能为空", trigger: "blur" }],
    postSort: [{ required: true, message: "岗位顺序不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询岗位列表 */
function getList() {
  loading.value = true;
  listPost(queryParams.value).then(response => {
    postList.value = response.data.list;
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
  form.value = {
    postId: undefined,
    postCode: undefined,
    postName: undefined,
    postSort: 0,
    status: CommonStatusEnum.ENABLE,
    remark: undefined
  };
  postRef.value?.resetFields()
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryRef.value.resetFields()
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加岗位";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const postId = row.postId || ids.value;
  getPost(postId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改岗位";
  });
}

/** 提交按钮 */
function submitForm() {
  postRef.value.validate(valid => {
    if (valid) {
      return;
    }
    // 修改
    if (form.value.postId !== undefined) {
      updatePost(form.value).then(response => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getList();
      });
      return;
    }
    // 新增
    addPost(form.value).then(response => {
      proxy.$modal.msgSuccess("新增成功");
      open.value = false;
      getList();
    });
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除岗位编号为"' + row.id + '"的数据项？').then(function() {
    return delPost(row.id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

// TODO 导出不正确
/** 导出按钮操作 */
function handleExport() {
  proxy.download("system/post/export", {
    ...queryParams.value
  }, `post_${new Date().getTime()}.xlsx`);
}

onMounted(() => {
  getList()
  console.log(queryRef.value)
  console.log(postRef.value)
})

</script>
