<template>
  <div class="app-container">
    <div style="width: 910px; margin: 0 auto;">
      <el-row>
        <el-col :span="12" style="border-right: 1px solid #e6e1e1;">
          <el-form :model="data" ref="dataForm" size="small"  label-position="left" label-width="120px">
            <el-form-item label="未履行债务总额" >
              <el-input v-model="data.totalAmount" style="width:250px" placeholder="请输入内容" clearable /> 万元
            </el-form-item>
            <el-form-item label="执行费用" >
              <el-input v-model="data.zxfAmount" style="width:250px" disabled /> 元
            </el-form-item>
            <el-form-item style="margin-top: 68px;">
              <el-button type="primary" icon="el-icon-search" @click="execCalcZxfData1">计算</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12" style="padding-left: 52px;">
          <el-form :model="data2" ref="dataForm2" size="small"  label-position="left" label-width="120px">
            <el-form-item label="本次执行总额" >
              <el-input v-model="data2.totalAmount" style="width:250px" placeholder="请输入内容" clearable /> 万元
            </el-form-item>
            <el-form-item label="执行费用" >
              <el-input v-model="data2.zxfAmount" style="width:250px" disabled /> 元
            </el-form-item>
            <el-form-item label="应发放执行金额" >
              <el-input v-model="data2.leftAmount" style="width:250px" disabled /> 元
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="execCalcZxfData2">计算</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { execCalcZxfData } from "@/api/biz/calcInterestRateData.js";


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
        totalAmount:null,
        zxfType:1,
        zxfAmount:null,
      },
      data2: {
        totalAmount:null,
        zxfType:2,
        zxfAmount:null,
        leftAmount:null,
      },
      // 表单参数
      form: {},
      // 表单校验
    };
  },
  created() {

  },
  methods: {
    execCalcZxfData1(){
      execCalcZxfData(this.data).then((res)=>{
        this.data.zxfAmount=res.data.zxfAmount;
      })
    },
    execCalcZxfData2(){
      execCalcZxfData(this.data2).then((res)=>{
        this.data2.zxfAmount=res.data.zxfAmount;
        this.data2.leftAmount=res.data.leftAmount;
      })
    },
  }
};
</script>
