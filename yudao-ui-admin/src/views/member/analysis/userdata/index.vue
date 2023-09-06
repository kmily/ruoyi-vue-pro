<template>
  <div class="app-container">
    <el-card class="box-card">
      <div style="text-align: center; color: #999999">总用户数</div>
      <div style="color:#9999CC;font-size:30px;text-align: center;">
        <count-to :start-val="0" :end-val="userTotal" :duration="2600" class="card-panel-num" />
      </div>
    </el-card>
    <el-card class="box-card">
      <div class="month-days">
        <div>
          <div style="text-align: center; color: #999999">本月新增用户</div>
          <div style="color:#9999CC;font-size:30px;text-align: center;">
            <count-to :start-val="0" :end-val="month" :duration="2600" class="card-panel-num" />
          </div>
        </div>
        <div class="divider"></div>
        <div>
          <div style="text-align: center; color: #999999">7天新增用户</div>
          <div style="color:#9999CC;font-size:30px;text-align: center;">
            <count-to :start-val="0" :end-val="sevenDay" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span style="color: #9999CC">数据明细</span>
        <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading">导出</el-button>
      </div>
      <div>
        <el-table
          :data="tableData.slice((pageNo-1)*pageSize,pageNo*pageSize)"
          style="width: 100%" size="mini" header-row-class-name="table-header">
          <el-table-column
            prop="month"
            label="日期">
          </el-table-column>
          <el-table-column
            prop="quantity"
            label="新增用户">
          </el-table-column>
        </el-table>
          <!-- 分页组件 -->
        <pagination v-show="total > 0"  :total="total" :page.sync="pageNo" :limit.sync="pageSize" @pagination="handleCurrentChange"/>
      </div>
    </el-card>

    </div>
</template>

<script>
import CountTo from 'vue-count-to'
import {getUserStatistics,exportStatisticsExcel} from "@/api/member/user";
export default {
  components: {
    CountTo
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      exportLoading: false,
      // 总条数
      total: 0,
      // 总用户数
      userTotal: 0,
      // 月数量
      month: 0,
      // 七天数量
      sevenDay: 0,
      // 会员列表
      tableData: [],
      // 查询参数
      pageNo: 1,
      pageSize: 10,
      
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getUserStatistics().then(response => {
        this.tableData = response.data.detail;
        this.total = response.data.detail.length;
        this.userTotal = response.data.total;
        this.month =  response.data.month;
        this.sevenDay =  response.data.sevenDays;
        this.loading = false;
      });
    },
    handleCurrentChange(data){
      this.pageNo = data.page;
    },
      /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      this.$modal.confirm('是否确认导出所有数据?').then(() => {
          this.exportLoading = true;
          return exportStatisticsExcel();
        }).then(response => {
          this.$download.excel(response, '数据明细.xls');
          this.exportLoading = false;
        }).catch(() => {});
    },
  }

}
</script>

<style scoped lang="scss">
  .box-card{
    margin-bottom: 20px;

    .month-days{
      display: flex;
      justify-content: center;
      height: 100%;
      .divider{
        background-color: #CCCCCC;
        height: 50px;
        width: 2px;
        margin: 0 30px;
      }
    }

    .table-header{
      background-color: #F0F2F5;
    }
  }

</style>