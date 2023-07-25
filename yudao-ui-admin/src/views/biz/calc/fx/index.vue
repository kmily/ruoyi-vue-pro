<template>
  <div class="app-container">
    <div style="width: 880px; margin: 0 auto;">
      <el-form :model="data" ref="dataForm" size="small"  label-position="left" label-width="120px">
        <el-row>
          <el-col :span="14">
            <el-form-item label="未履行债务总额" >
              <el-input v-model="data.leftAmount" style="width:250px" placeholder="请输入内容" clearable /> 万元
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="利率类型" >
              <el-select v-model="data.rateType" placeholder="请选择">
                <el-option
                  v-for="item in lvList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="data.rateType==1">
          <el-col :span="14">
            <el-form-item label="利息类型" >
              <el-radio-group v-model="data.fixType">
                <el-radio :label="1">固定数值</el-radio>
                <el-radio :label="2">LPR倍数</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="考虑闰年" >
              <el-switch
                v-model="data.leapYear"
                :active-value="1"
                :inactive-value="0">
              </el-switch>
            </el-form-item>
          </el-col>
        </el-row> 
        <el-row v-if="data.rateType==1">
          <el-col :span="14">
            <el-form-item label="利息周期" >
              <el-radio-group v-model="data.fixSectionType">
                <el-radio :label="1">年</el-radio>
                <el-radio :label="2">月</el-radio>
                <el-radio :label="3">日</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item v-if="data.fixType==1"  label="利率%" >
              <el-input v-model="data.fixRate" style="width:220px" placeholder="请输入内容" clearable />
            </el-form-item>
            <el-form-item v-if="data.fixType==2"  label="LRP倍数" >
              <el-input v-model="data.fixLPRs" style="width:220px" placeholder="请输入内容" clearable />
            </el-form-item>
          </el-col>
        </el-row> 
        <el-row>
          <el-col :span="14">
            <el-form-item label="开始时间" >
              <el-date-picker v-model="data.startDate" type="date" placeholder="选择日期"  value-format="timestamp"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="结束时间" >
              <el-date-picker v-model="data.endDate" type="date" placeholder="选择日期"  value-format="timestamp"></el-date-picker>              
            </el-form-item>
          </el-col>
        </el-row> 
        <el-row>
          <el-col :span="14">            
            <el-form-item label="考虑闰年" v-if="data.rateType==2" >
              <el-switch
                v-model="data.leapYear"
                :active-value="1"
                :inactive-value="0">
              </el-switch>
            </el-form-item>
            <span v-if="data.rateType==1"> &nbsp;</span>
          </el-col>
          <el-col :span="10">
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="count">计算</el-button>
            </el-form-item>
          </el-col>
        </el-row>        
      </el-form>
      <div>
        <el-table v-loading="loading" :data="list" :height="500">
          <el-table-column label="时间段" align="center"  >
            <template v-slot="scope">
              <span>{{ scope.row.startDate }}至{{ scope.row.endDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="天数" align="center" prop="days" />
          <el-table-column label="基准利率%" align="center" prop="suiteRate" />
          <el-table-column label="金额" align="center" prop="sectionAmount" />
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { execCalcInterestLxData,execCalcInterestFxData } from "@/api/biz/calcInterestRateData.js";


export default {
  name: "CalcInterestRateData",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 利率数据列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      data: {
        leftAmount:null,
        leapYear: 0, //是否考虑闰年，0不考虑，1考虑。默认0
        processId: 10,
        startDate:null,
        endDate:null,
        rateType:1,
        fixSectionType:null, //约定周期 1年2月3日
        fixType:1,  //利息类型 1指定利率值 2LPR倍数 默认1
        fixRate:null,
        fixLPRs:null,
      },
      lvList:[
        {value: 1,label: '分段'},
        {value: 2,label: '约定'}        
      ],
      // 表单参数
      form: {},
      // 表单校验
    };
  },
  created() {
    
  },
  methods: {
    count(){
      this.execCalcInterestFxData();
    },
    execCalcInterestFxData(){
      execCalcInterestFxData(this.data).then((res)=>{
        this.list=res.data.sectionList;
      })
    },
  }
};
</script>
