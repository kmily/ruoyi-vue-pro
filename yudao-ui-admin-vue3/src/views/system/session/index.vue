<template>
  <div class="system-session-container">
    <el-card shadow="hover">
      <div class="system-user-search mb15">
        <el-input
          size="default"
          placeholder="请输入登录地址"
          style="max-width: 180px"
        />
        <el-button size="default" type="primary" class="ml10">
          <el-icon>
            <ele-Search />
          </el-icon>
          查询
        </el-button>
      </div>
      <el-table :data="tableData.data" style="width: 100%">
        <el-table-column
          label="会话编号"
          align="center"
          prop="id"
          width="300"
        />
        <el-table-column
          label="登录名称"
          align="center"
          prop="username"
          show-overflow-tooltip
        />
        <el-table-column
          label="部门名称"
          align="center"
          prop="deptName"
          show-overflow-tooltip
        />
        <el-table-column
          label="登录地址"
          align="center"
          prop="userIp"
          show-overflow-tooltip
        />
        <el-table-column
          label="userAgent"
          align="center"
          prop="userAgent"
          show-overflow-tooltip
        />
        <el-table-column
          label="登录时间"
          align="center"
          prop="createTime"
          width="180"
        >
          <!-- <template #default="scope">
            <span>{{ formatPast(scope.row.createTime, "YYYY-mm-dd") }}</span>
          </template> -->
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button size="small" type="text">强退</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script lang="ts">
import { toRefs, reactive, onMounted, defineComponent } from "vue";
import { getSeesionList, forceLogout } from "/@/api/system/session";
import { formatPast } from "/@/utils/formatTime";
export default defineComponent({
  name: "systemSeesion",
  setup() {
    const state = reactive({
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          pageNo: 1,
          pageSize: 10,
          name: undefined,
          code: undefined,
          status: undefined
        }
      }
    });
    // 初始化表格数据
    const initTableData = () => {
      state.tableData.loading = true;
      getSeesionList(state.tableData.param).then((response: any) => {
        if (response?.data) {
          state.tableData.data = response?.data?.list;
          state.tableData.total = response?.data?.total;
          state.tableData.loading = false;
        }
      });
    };
    // 分页改变
    const onHandleSizeChange = (val: number) => {
      state.tableData.param.pageSize = val;
      getSeesionList(state.tableData.param).then((response: any) => {
        if (response?.data) {
          state.tableData.data = response.data.list;
          state.tableData.total = response.data.total;
        }
      });
    };
    // 分页改变
    const onHandleCurrentChange = (val: number) => {
      state.tableData.param.pageNo = val;
      getSeesionList(state.tableData.param).then((response: any) => {
        if (response?.data) {
          state.tableData.data = response?.data?.list;
          state.tableData.total = response?.data?.total;
        }
      });
    };
    // 页面加载时
    onMounted(() => {
      initTableData();
    });
    return {
      onHandleSizeChange,
      onHandleCurrentChange,
      formatPast,
      ...toRefs(state)
    };
  }
});
</script>
