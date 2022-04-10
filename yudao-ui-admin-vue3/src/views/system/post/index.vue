<script lang="ts">
export default {
  name: "post"
};
</script>

<script setup lang="ts">
import dayjs from "dayjs";
import { getPostList } from "/@/api/system/post";
import { FormInstance } from "element-plus";
import { Switch } from "@pureadmin/components";
import { successMessage } from "/@/utils/message";
import { reactive, ref, onMounted, computed } from "vue";
import { useEpThemeStoreHook } from "/@/store/modules/epTheme";
import { useRenderIcon } from "/@/components/ReIcon/src/hooks";

const tableOptions = reactive({
  size: "default",
  loading: true,
  buttonRef: undefined,
  visible: false,
  totalPage: 0,
  switchLoadMap: {},
  checkList: [],
  tableData: [],
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    name: undefined,
    user: undefined,
    code: undefined,
    status: undefined
  }
});

const formRef = ref<FormInstance>();

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

const getDropdownItemStyle = computed(() => {
  return s => {
    return {
      background:
        s === tableOptions.size ? useEpThemeStoreHook().epThemeColor : "",
      color: s === tableOptions.size ? "#f4f4f5" : "#000"
    };
  };
});

async function getTableData() {
  tableOptions.loading = true;
  getPostList().then((response: any) => {
    tableOptions.tableData = response?.data?.list;
    tableOptions.totalPage = response?.data?.total;
  });
}

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

function onCheckChange(val) {
  console.log("onCheckChange", val);
}

function handleSelectionChange(val) {
  console.log("handleSelectionChange", val);
}

// function onChange(checked, { $index, row }) {
//   tableOptions.switchLoadMap.[$index] = Object.assign({}, tableOptions.switchLoadMap.[$index], {
//     loading: true
//   });
//   setTimeout(() => {
//     tableOptions.switchLoadMap.[$index] = Object.assign(
//       {},
//       tableOptions.switchLoadMap.[$index],
//       {
//         loading: false
//       }
//     );
//     successMessage("已成功修改岗位状态");
//   }, 300);
// }
onMounted(() => {
  getTableData();
});
</script>

<template>
  <div class="main">
    <!-- TODO size="small" -->
    <!-- TODO label-width="68px" -->
    <!-- TODO v-show="showSearch" -->
    <el-form
      ref="formRef"
      :inline="true"
      :model="tableOptions.queryParams"
      class="bg-white w-99/100 pl-8 pt-4"
    >
      <el-form-item label="岗位编码：" prop="code">
        <el-input
          v-model="tableOptions.queryParams.code"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="岗位名称：" prop="user">
        <el-input
          v-model="tableOptions.queryParams.user"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="状态：" prop="status">
        <el-select
          v-model="tableOptions.queryParams.status"
          placeholder="请选择"
          clearable
        >
          <el-option label="开启" value="1" />
          <el-option label="关闭" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          :icon="useRenderIcon('search')"
          :loading="tableOptions.loading"
          @click="getTableData"
          >搜索</el-button
        >
        <el-button :icon="useRenderIcon('refresh')" @click="resetForm(formRef)"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <div
      class="w-99/100 mt-6 pb-4 bg-white"
      v-loading="tableOptions.loading"
      loading
    >
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
          <el-button type="success" :icon="useRenderIcon('import')"
            >导入</el-button
          >
          <el-button type="warning" :icon="useRenderIcon('export')"
            >导出</el-button
          >
          <el-tooltip effect="dark" content="刷新" placement="top">
            <IconifyIconOffline
              class="cursor-pointer outline-none"
              icon="refresh-right"
              width="20"
              color="#606266"
              @click="getTableData"
            />
          </el-tooltip>

          <!-- TODO 右边按钮的组件封装 -->
          <el-tooltip effect="dark" content="密度" placement="top">
            <el-dropdown id="header-translation" trigger="click">
              <IconifyIconOffline
                class="cursor-pointer outline-none"
                icon="density"
                width="20"
                color="#606266"
              />
              <template #dropdown>
                <el-dropdown-menu class="translation">
                  <el-dropdown-item
                    :style="getDropdownItemStyle('large')"
                    @click="tableOptions.size = 'large'"
                  >
                    松散
                  </el-dropdown-item>
                  <el-dropdown-item
                    :style="getDropdownItemStyle('default')"
                    @click="tableOptions.size = 'default'"
                  >
                    默认
                  </el-dropdown-item>
                  <el-dropdown-item
                    :style="getDropdownItemStyle('small')"
                    @click="tableOptions.size = 'small'"
                  >
                    紧凑
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-tooltip>

          <el-popover :width="200" trigger="click">
            <template #reference>
              <IconifyIconOffline
                class="cursor-pointer outline-none"
                icon="setting"
                width="20"
                color="#606266"
                @mouseover="e => (tableOptions.buttonRef = e.currentTarget)"
                @mouseenter="tableOptions.visible = true"
                @mouseleave="tableOptions.visible = false"
              />
            </template>
            <el-checkbox-group
              v-model="tableOptions.checkList"
              @change="onCheckChange"
            >
              <el-checkbox label="序号列" />
              <el-checkbox label="勾选列" />
            </el-checkbox-group>
          </el-popover>
        </div>
        <el-tooltip
          ref="tooltipRef"
          v-model:visible="tableOptions.visible"
          :popper-options="{
            modifiers: [
              {
                name: 'computeStyles',
                options: {
                  adaptive: false,
                  enabled: false
                }
              }
            ]
          }"
          placement="top"
          :virtual-ref="tableOptions.buttonRef"
          virtual-triggering
          trigger="click"
          content="列设置"
        />
      </div>
      <!-- TODO 边框不对 -->
      <!-- TODO :header-cell-style="{ background: '#fafafa', color: '#606266' }" -->
      <el-table
        border
        :size="tableOptions.size"
        v-loading="tableOptions.loading"
        :data="tableOptions.tableData"
        :header-cell-style="{ background: '#fafafa', color: '#606266' }"
      >
        <el-table-column label="岗位编号" align="center" prop="id" />
        <el-table-column label="岗位编码" align="center" prop="code" />
        <el-table-column label="岗位名称" align="center" prop="name" />
        <el-table-column label="岗位排序" align="center" prop="sort" />
        <el-table-column label="状态" align="center" prop="status">
          <!-- TODO dict-tag -->
          <template #default="scope">
            <Switch
              :size="tableOptions.size"
              v-model:checked="scope.row.status"
              :checkedValue="1"
              :unCheckedValue="0"
              checked-children="已开启"
              un-checked-children="已关闭"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          align="center"
          width="180"
          prop="createTime"
        >
          <template #default="scope">
            <span>{{
              dayjs(scope.row.createTime).format("YYYY-MM-DD HH:mm:ss")
            }}</span>
          </template>
        </el-table-column>
        <!-- TODO class-name="small-padding fixed-width" -->
        <el-table-column label="操作" width="130" align="center">
          <template #default="scope">
            <!-- TODO size="mini" -->
            <!-- TODO icon="el-icon-edit" -->
            <!-- TODO v-hasPermi="['system:post:update']" -->
            <el-button type="text" @click="handleUpdate(scope.row)"
              >修改</el-button
            >
            <el-popconfirm title="是否确认删除？">
              <template #reference>
                <!-- TODO size="mini" -->
                <!-- TODO icon="el-icon-delete" -->
                <!-- TODO v-hasPermi="['system:post:delete']" -->
                <el-button type="text" @click="handleDelete(scope.row)"
                  >删除</el-button
                >
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- TODO 分页组件 -->
      <el-pagination
        class="flex justify-end mt-4 mr-2"
        :size="tableOptions.size"
        v-model:page-size="tableOptions.queryParams.pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableOptions.totalPage"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- TODO 添加或修改岗位对话框 -->
  </div>
</template>

<style lang="scoped"></style>
