<template>
  <div class="app-container">
    <el-card class="box-card">
          <!-- 搜索工作栏 -->
        <el-date-picker v-model="createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                      @change="getList" size="small" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
    </el-card>
    <el-card class="box-card">
      <div class="month-days">
        <div>
          <div style="text-align: center; color: #999999">启动次数</div>
          <div style="color:#9999CC;font-size:30px;text-align: center;">
            <count-to :start-val="0" :end-val="userTotal" :duration="2600" class="card-panel-num" />
          </div>
        </div>
        <div class="divider"></div>
        <div>
          <div style="text-align: center; color: #999999">只启动一次用户</div>
          <div style="color:#9999CC;font-size:30px;text-align: center;">
            <count-to :start-val="0" :end-val="once" :duration="2600" class="card-panel-num" />
          </div>
        </div>
        <div class="divider"></div>
        <div>
          <div style="text-align: center; color: #999999">只启动一次用户占比</div>
          <div style="color:#9999CC;font-size:30px;text-align: center;">
            <!-- <count-to :start-val="0" :end-val="percentage" :duration="2600" class="card-panel-num" /> -->
            {{percentage}}
          </div>
        </div>
      </div>
    </el-card>
    <el-card class="box-card">
      <!-- <div style="text-align: center; color: #999999"></div> -->
      <div style="color:#9999CC;font-size:30px;text-align: center;">
        <line-chart :chart-data="lineChartData" />
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
            prop="date"
            label="日期">
          </el-table-column>
          <el-table-column
            prop="times"
            label="启动次数">
          </el-table-column>
          <el-table-column
            prop="percentage"
            label="启动次数占比">
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

import LineChart from "./LineChart";

import { parseTime} from "@/utils/ruoyi";
import {getBootUp,exportBootUpExcel} from "@/api/member/bootUp";


export default {
  components: {
    CountTo,
    LineChart
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
      once: 0,
      // 七天数量
      percentage: '0%',
      // 会员列表
      tableData: [],
      // 查询参数
      pageNo: 1,
      pageSize: 10,
      createTime: [],
      lineChartData: {}
    };
  },
  created() {
    let now1 = new Date();
    let end = now1.getTime() - 3600 * 1000 * 24 * 30;

    let start = parseTime(end, '{y}-{m}-{d}') + ' 00:00:00';
    let endStr = parseTime(now1, '{y}-{m}-{d}') + ' 23:59:59';
    this.createTime = [start, endStr];
  
    console.log('参数', this.createTime);
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getBootUp({createTime: this.createTime}).then(response => {
        let detials = response.data.resVOList;
        this.tableData = [...detials].reverse();
        this.total = response.data.resVOList.length;
        this.userTotal = response.data.total;
        this.once =  response.data.once;
        this.percentage =  response.data.percentage;

        let labels = detials.map(item => {
          return item.date;
        });

        let chartData = detials.map(item => {
          return item.times;
        });
        this.lineChartData = {
          labels: labels,
          actualData: []
        }
        let that = this;
        setTimeout(() => {
          that.lineChartData['actualData'] = chartData;
        }, 100);

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
          return exportBootUpExcel();
        }).then(response => {
          this.$download.excel(response, '启动数据明细.xls');
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
      justify-content:flex-start;
      height: 100%;
      .divider{
        background-color: #CCCCCC;
        height: 50px;
        width: 2px;
        margin: 0 80px;
      }
    }

    .table-header{
      background-color: #F0F2F5;
    }
  }

</style>