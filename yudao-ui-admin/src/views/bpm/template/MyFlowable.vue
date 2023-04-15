<template>
  <div>
    <div v-if="list.length<=0">
      <el-empty description="暂无待办流程"></el-empty>
    </div>
    <div v-if="list.length>0">
      <!-- 列表 -->
      <el-table v-loading="loading" :data="list" height="365">
        <el-table-column label="流程名" align="center" prop="name"/>
        <el-table-column label="类型" align="center" prop="category" width="70">
          <template slot-scope="scope">
            <dict-tag :type="DICT_TYPE.BPM_MODEL_CATEGORY" :value="scope.row.category"/>
          </template>
        </el-table-column>
        <el-table-column label="当前进度" align="center" prop="tasks">
          <template slot-scope="scope">
            <el-button v-for="(task,index) in scope.row.tasks" type="text" :key="index" @click="handleDetail(scope.row)">
              <span>{{ task.name }}</span>
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" width="80">
          <template slot-scope="scope">
            <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="结果" align="center" prop="result" width="80">
          <template slot-scope="scope">
            <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_RESULT" :value="scope.row.result"/>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" align="center" prop="createTime" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="80">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleDetail(scope.row)"
                       v-hasPermi="['bpm:process-instance:query']">详情
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
import {getMyProcessInstancePage, cancelProcessInstance} from "@/api/bpm/processInstance";

export default {
  name: "MyFlowable",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 工作流的流程实例的拓展列表
      list: [],
      // 是否显示弹出层
      dateRangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        name: null,
        processDefinitionId: null,
        category: null,
        status: null,
        result: null,
      }
    };
  },
  created() {
    // debugger
    // 是否拥有权限
    console.log(this.$auth.hasPermi("bpm:process-instance:query"));

    if (this.$auth.hasPermi("bpm:process-instance:query")){
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
      // 执行查询
      getMyProcessInstancePage(params).then(response => {
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
    /** 新增按钮操作 **/
    handleAdd() {
      this.$router.push({path: "/bpm/process-instance/create"})
    },
    /** 取消按钮操作 */
    handleCancel(row) {
      const id = row.id;
      this.$prompt('请输入取消原因？', "取消流程", {
        type: 'warning',
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/, // 判断非空，且非空格
        inputErrorMessage: "取消原因不能为空",
      }).then(({value}) => {
        return cancelProcessInstance(id, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("取消成功");
      })
    },
    /** 处理详情按钮 */
    handleDetail(row) {
      this.$router.push({path: "/bpm/process-instance/detail", query: {id: row.id}});
    },
  }
};
</script>
