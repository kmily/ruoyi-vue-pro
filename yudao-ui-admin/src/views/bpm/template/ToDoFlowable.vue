<template>
  <div>
    <div v-if="list.length<=0">
      <el-empty description="暂无待办流程"></el-empty>
    </div>
    <div v-if="list.length>0">
      <!-- 列表 -->
      <el-table v-loading="loading" :data="list" height="365">
        <el-table-column label="任务名称" align="center" prop="name"/>
        <el-table-column label="所属流程" align="center" prop="processInstance.name"/>
        <el-table-column label="流程发起人" align="center" prop="processInstance.startUserNickname"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="version" width="70">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
            <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleAudit(scope.row)"
                       v-hasPermi="['bpm:task:update']">审批
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                  @pagination="getList"/>

    </div>
  </div>

</template>

<script>
import {getTodoTaskPage} from '@/api/bpm/task'

export default {
  name: "ToDoFlowable",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 待办任务列表
      list: [],
      // 查询参数
      dateRangeCreateTime: [],
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        name: null,
      },
    };
  },
  created() {
    // 是否拥有权限
    if (this.$auth.hasPermi("bpm:task:query")){
      this.getList();
    }
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      getTodoTaskPage(params).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 处理审批按钮 */
    handleAudit(row) {
      this.$router.push({path: "/bpm/process-instance/detail", query: {id: row.processInstance.id}});
    },
  }
};
</script>
