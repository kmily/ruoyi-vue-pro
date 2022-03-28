<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="登录地址" prop="userIp">
        <el-input v-model="queryParams.userIp" placeholder="请输入登录地址" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="用户名称" prop="username">
        <el-input v-model="queryParams.username" placeholder="请输入用户名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>
    <el-table v-loading="loading" :data="listData" style="width: 100%;">
      <el-table-column label="会话编号" align="center" prop="id" width="300"/>
      <el-table-column label="登录名称" align="center" prop="username" width="100"/>
      <el-table-column label="部门名称" align="center" prop="deptName" width="100"/>
      <el-table-column label="登录地址" align="center" prop="userIp" width="100"/>
      <el-table-column label="userAgent" align="center" prop="userAgent" :show-overflow-tooltip="true"/>
      <el-table-column label="登录时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" icon="Delete" @click="handleForceLogout(scope.row)"
                     v-hasPermi="['system:user-session:delete']">强退
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
                @pagination="getList"/>
  </div>
</template>

<script>
import {list, forceLogout} from "@/api/system/session";
export default {
  name: "Online",
  setup() {
    const {proxy} = getCurrentInstance();
    const state = reactive({
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      listData: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        userIp: undefined,
        username: undefined
      }
    });
    onMounted(() => {
      getList();
    })

    const getList = () => {
      state.loading = true;

      list(state.queryParams).then(response => {
        state.listData = response.data.list;
        state.total = response.data.total;
        state.loading = false;
      });
    }

    /** 搜索按钮操作 */
    const handleQuery = () => {
      state.queryParams.pageNo = 1;
      getList();
    }

    /** 重置按钮操作 */
    const resetQuery = () => {
      proxy.resetForm("queryForm");
      handleQuery();
    }

    /** 强退按钮操作 */
    const handleForceLogout = (row) => {
      proxy.$modal.confirm('是否确认强退名称为"' + row.username + '"的数据项?').then(function () {
        return forceLogout(row.id);
      }).then(() => {
        getList();
        proxy.$modal.msgSuccess("强退成功");
      }).catch(() => {
      });
    }
    return {
      ...toRefs(state),
      getList,
      handleQuery,
      resetQuery,
      handleForceLogout
    }
  }
}
</script>

