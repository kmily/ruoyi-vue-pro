<script lang="ts">
export default {
  name: "post"
};
</script>

<script setup lang="ts">
import { getJobList } from "/@/api/system/post";
import { FormInstance } from "element-plus";
import { reactive, ref, onMounted, computed } from "vue";
import { useEpThemeStoreHook } from "/@/store/modules/epTheme";
import { useRenderIcon } from "/@/components/ReIcon/src/hooks";

const queryParams = reactive({
  code: "",
  user: "",
  status: ""
});
let jobList = ref([]);
let loading = ref(false);
const totalPage = ref(0);
let pageSize = ref(10);
let size = ref("default");

const formRef = ref<FormInstance>();

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

const getDropdownItemStyle = computed(() => {
  return s => {
    return {
      background: s === size.value ? useEpThemeStoreHook().epThemeColor : "",
      color: s === size.value ? "#f4f4f5" : "#000"
    };
  };
});

function handleUpdate(row) {
  console.log(row);
}

function handleDelete(row) {
  console.log(row);
}

function handleCurrentChange(val: number) {
  console.log(`current page: ${val}`);
}

function handleSizeChange(val: number) {
  console.log(`${val} items per page`);
}

onMounted(async () => {
  let { data } = await getJobList();
  jobList.value = data.list;
  totalPage.value = data.total;
});
</script>

<template>
  <div class="main">
    <!-- TODO size="small" -->
    <!-- TODO label-width="68px" -->
    <!-- TODO v-show="showSearch" -->
    <el-form :model="queryParams" ref="formRef" :inline="true" class="bg-white w-99/100 pl-8 pt-4">
      <el-form-item label="岗位编码：">
        <!-- TODO @keyup.enter.native="handleQuery" -->
        <el-input v-model="queryParams.code" placeholder="请输入岗位编码" clearable />
      </el-form-item>
      <el-form-item label="岗位名称：">
        <!-- TODO @keyup.enter.native="handleQuery" -->
        <el-input v-model="queryParams.user" placeholder="请输入岗位名称" clearable />
      </el-form-item>
      <el-form-item label="状态：">
        <!-- TODO @keyup.enter.native="handleQuery" -->
        <el-select v-model="queryParams.status" placeholder="请选择岗位状态" clearable>
          <el-option label="开启" value="1" />
          <el-option label="关闭" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <!-- TODO @keyup.enter.native="handleQuery" -->
        <el-button type="primary" :icon="useRenderIcon('search')">搜索</el-button>
        <!-- TODO resetQuery -->
        <el-button :icon="useRenderIcon('refresh')" @click="resetForm(formRef)">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="w-99/100 mt-6 pb-4 bg-white">
      <div class="flex justify-between w-full h-60px p-4">
        <p class="font-bold">岗位列表</p>
        <div class="w-200px flex items-center justify-around">
          <!-- TODO plain -->
          <!-- TODO icon="el-icon-plus" -->
          <!-- TODO size="mini" -->
          <!-- TODO @click="handleAdd" -->
          <!-- TODO v-hasPermi="['system:post:create']" -->
          <el-button type="primary">新增岗位</el-button>
          <!-- TODO size="mini" -->
          <!-- TODO @click="handleExport" -->
          <!-- TODO :loading="exportLoading" -->
          <!-- TODO -hasPermi="['system:post:export']" -->
          <el-button type="warning" :icon="useRenderIcon('export')">导出</el-button>
          <IconifyIconOffline class="cursor-pointer" icon="refresh-right" width="20" color="#606266"/>

          <!-- TODO 右边按钮的组件封装 -->
          <el-dropdown id="header-translation" trigger="click">
            <IconifyIconOffline class="cursor-pointer" icon="density" width="20" color="#606266"/>
            <template #dropdown>
              <el-dropdown-menu class="translation">
                <el-dropdown-item :style="getDropdownItemStyle('large')" @click="size = 'large'">松散</el-dropdown-item>
                <el-dropdown-item :style="getDropdownItemStyle('default')" @click="size = 'default'">默认</el-dropdown-item>
                <el-dropdown-item :style="getDropdownItemStyle('small')" @click="size = 'small'">紧凑</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <IconifyIconOffline class="cursor-pointer" icon="setting" width="20" color="#606266"/>
        </div>
      </div>
      <!-- TODO 边框不对 -->
      <!-- TODO :header-cell-style="{ background: '#fafafa', color: '#606266' }" -->
      <el-table border :size="size" v-loading="loading" :data="jobList"
                :header-cell-style="{ background: '#fafafa', color: '#606266' }">
        <el-table-column label="岗位编号" align="center" prop="id" />
        <el-table-column label="岗位编码" align="center" prop="code" />
        <el-table-column label="岗位名称" align="center" prop="name" />
        <el-table-column label="岗位排序" align="center" prop="sort" />
        <el-table-column label="状态" align="center" prop="status">
          <!-- TODO dict-tag -->
          <template #default="scope">
            <el-tag>{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime">
          <!-- TODO parseTime(scope.row.createTime) -->
          <template #default="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <!-- TODO class-name="small-padding fixed-width" -->
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <!-- TODO size="mini" -->
            <!-- TODO icon="el-icon-edit" -->
            <!-- TODO v-hasPermi="['system:post:update']" -->
            <el-button type="text" @click="handleUpdate(scope.row)">修改</el-button>
            <el-popconfirm title="确定删除吗？">
              <template #reference>
                <!-- TODO size="mini" -->
                <!-- TODO icon="el-icon-delete" -->
                <!-- TODO v-hasPermi="['system:post:delete']" -->
                <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- TODO 分页组件 -->
      <el-pagination class="flex justify-end mt-4 mr-2" :small="size === 'small'" v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]" :background="true" layout="total, sizes, prev, pager, next, jumper"
          :total="totalPage" @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>

    <!-- TODO 添加或修改岗位对话框 -->
  </div>
</template>

<style lang="scoped"></style>
