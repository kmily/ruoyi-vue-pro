<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="岗位编码" prop="postCode">
        <el-input v-model="queryParams.postCode" placeholder="请输入岗位编码" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="岗位名称" prop="postName">
        <el-input v-model="queryParams.postName" placeholder="请输入岗位名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
<!--      <el-form-item label="状态" prop="status">-->
<!--        <el-select v-model="queryParams.status" placeholder="请选择部门状态" clearable>-->
<!--          <el-option v-for="dict in getDictDatas(DICT_TYPE.COMMON_STATUS)"-->
<!--                     :key="dict.value" :label="dict.label" :value="dict.value"/>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
export default {
  name: "user"
};
</script>

<script setup lang="ts">
import {ref, reactive, getCurrentInstance, toRefs, onMounted,} from "vue"

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

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  // getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryRef.value.resetFields()
  handleQuery();
}
</script>
