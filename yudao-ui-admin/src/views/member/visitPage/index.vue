<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 搜索工作栏 -->
      <el-date-picker v-model="startTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                    @change="getList" size="small" :picker-options="isDisabled" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span style="color: #9999CC">数据明细</span>
        <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading">导出</el-button>
      </div>
      <div>
        <!-- 列表 -->
        <el-table v-loading="loading" :data="list.slice((pageNo-1)*pageSize,pageNo*pageSize)">
          <el-table-column label="页面名称" align="center" prop="pageName" />
          <el-table-column label="访问次数" align="center" prop="count">
          </el-table-column>
          <el-table-column label="访问次数占比" align="center" prop="percentage">
          </el-table-column>
          <el-table-column label="平均访问时长" align="center" prop="average" />
        </el-table>
        <!-- 分页组件 -->
        <pagination v-show="total > 0"  :total="total" :page.sync="pageNo" :limit.sync="pageSize" @pagination="handleCurrentChange"/>
      </div>
    </el-card>
  </div>
</template>

<script>
import { parseTime} from "@/utils/ruoyi";
import { getVisitPagePage, exportVisitPageExcel } from "@/api/member/visitPage";

export default {
  name: "VisitPage",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      exportLoading: false,
      // 总条数
      total: 0,
      // 页面访问数据列表
      list: [],
      // 弹出层标题
      title: "",
      pageNo: 1,
      pageSize: 10,
      // 查询参数
      startTime: [],
      pickDate:'',
      isDisabled: {
        onPick: ({maxDate, minDate}) => {
            this.pickDate = minDate.getTime()
            if (maxDate) {
                this.pickDate = ''
            }
        },
        disabledDate: time => {
            // console.log('this.pickDate', this.pickDate);
            // let choiceDateTime = new Date(this.pickDate).getTime()
            // const minTime = new Date(choiceDateTime).setMonth(new Date(choiceDateTime).getMonth() - 1);
            // const maxTime = new Date(choiceDateTime).setMonth(new Date(choiceDateTime).getMonth() + 1);
            // const min = minTime;
            // const max = maxTime
            // if (this.pickDate) {
              let now = new Date().getTime();
              const maxTime = new Date(now).setDate(new Date(now).getDate() + 1);
              return time.getTime() > maxTime; // || time.getTime() > max;
          //  }
        }
      }
    };
  },
  created() {
    let now1 = new Date();
    let end = now1.getTime() - 3600 * 1000 * 24 * 30;

    let start = parseTime(end, '{y}-{m}-{d}') + ' 00:00:00';
    let endStr = parseTime(now1, '{y}-{m}-{d}') + ' 23:59:59';
    this.startTime = [start, endStr];
  
    console.log('参数', this.startTime);
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getVisitPagePage({startTime: this.startTime}).then(response => {
        this.list = response.data;
        this.total = this.list.length;
        this.loading = false;
      });
    },
    handleCurrentChange(data){
      this.pageNo = data.page;
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有页面访问数据数据项?').then(() => {
          this.exportLoading = true;
          return exportVisitPageExcel(params);
        }).then(response => {
          this.$download.excel(response, '页面访问数据.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
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