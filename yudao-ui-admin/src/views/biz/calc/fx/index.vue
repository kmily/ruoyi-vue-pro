<template>
  <div class="app-container">

  </div>
</template>

<script>
import { createCalcInterestRateData, updateCalcInterestRateData, deleteCalcInterestRateData, getCalcInterestRateData, getCalcInterestRateDataPage, exportCalcInterestRateDataExcel } from "@/api/biz/calcInterestRateData";

export default {
  name: "CalcInterestRateData",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 利率数据列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        startDate: [],
        rateHalfYear: null,
        rateOneYear: null,
        rateThreeYear: null,
        rateFiveYear: null,
        rateOverFiveYear: null,
        createTime: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {

  }
};
</script>
