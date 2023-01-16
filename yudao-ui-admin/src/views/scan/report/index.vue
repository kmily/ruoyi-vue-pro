<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="报告编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入报告编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['scan:report:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['scan:report:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="报告编号" align="center" prop="code" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="sex" />
      <el-table-column label="体型值" align="center" prop="shapeValue" />
      <el-table-column label="模型文件id" align="center" prop="modelFileId" />
      <el-table-column label="报告文件id" align="center" prop="reportFileId" />
      <el-table-column label="扫描用户id" align="center" prop="userId" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['scan:report:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['scan:report:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->

    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="报告编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入报告编号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                      :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="身高" prop="height">
          <el-input v-model="form.height" placeholder="请输入身高" />
        </el-form-item>
        <el-form-item label="身体年龄" prop="age">
          <el-input v-model="form.age" placeholder="请输入身体年龄" />
        </el-form-item>
        <el-form-item label="健康评分" prop="healthScore">
          <el-input v-model="form.healthScore" placeholder="请输入健康评分" />
        </el-form-item>
        <el-form-item label="高低肩" prop="highLowShoulders">
          <el-input v-model="form.highLowShoulders" placeholder="请输入高低肩：左右肩关节Y坐标差值" />
        </el-form-item>
        <el-form-item label="头侧歪" prop="headAskew">
          <el-input v-model="form.headAskew" placeholder="请输入头侧歪：头和脖子" />
        </el-form-item>
        <el-form-item label="左腿" prop="leftLegKokd">
          <el-input v-model="form.leftLegKokd" placeholder="请输入左腿XOKD：X型 O型 K型 D型腿" />
        </el-form-item>
        <el-form-item label="右腿XOKD" prop="rightLegKokd">
          <el-input v-model="form.rightLegKokd" placeholder="请输入右腿XOKD：X型 O型 K型 D型腿" />
        </el-form-item>
        <el-form-item label="长短腿" prop="longShortLeg">
          <el-input v-model="form.longShortLeg" placeholder="请输入长短腿：左右髋关节Y坐标差值" />
        </el-form-item>
        <el-form-item label="头部前倾" prop="pokingChin">
          <el-input v-model="form.pokingChin" placeholder="请输入头部前倾：左耳和脖子" />
        </el-form-item>
        <el-form-item label="骨盆前倾" prop="pelvicAnteversion">
          <el-input v-model="form.pelvicAnteversion" placeholder="请输入骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点" />
        </el-form-item>
        <el-form-item label="左膝盖分析" prop="leftKneeAnalysis">
          <el-input v-model="form.leftKneeAnalysis" placeholder="请输入左膝盖分析：前曲 超伸" />
        </el-form-item>
        <el-form-item label="右膝盖分析" prop="rightKneeAnalysis">
          <el-input v-model="form.rightKneeAnalysis" placeholder="请输入右膝盖分析：前曲 超伸" />
        </el-form-item>
        <el-form-item label="左圆肩" prop="leftRoundShoulder">
          <el-input v-model="form.leftRoundShoulder" placeholder="请输入左圆肩：肩和肩峰" />
        </el-form-item>
        <el-form-item label="右圆肩" prop="rightRoundShoulder">
          <el-input v-model="form.rightRoundShoulder" placeholder="请输入右圆肩：肩和肩峰" />
        </el-form-item>
        <el-form-item label="重心左右倾斜" prop="focusTiltLeftRight">
          <el-input v-model="form.focusTiltLeftRight" placeholder="请输入重心左右倾斜：人体重心和左踝关节与右踝关节中点" />
        </el-form-item>
        <el-form-item label="重心前后倾斜" prop="focusTiltForthBack">
          <el-input v-model="form.focusTiltForthBack" placeholder="请输入重心前后倾斜：人体重心和左踝关节与右踝关节中点" />
        </el-form-item>
        <el-form-item label="体重" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入体重" />
        </el-form-item>
        <el-form-item label="蛋白率" prop="proteinRate">
          <el-input v-model="form.proteinRate" placeholder="请输入蛋白率" />
        </el-form-item>
        <el-form-item label="骨量水分率" prop="boneMassMositureContent">
          <el-input v-model="form.boneMassMositureContent" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="脂肪率" prop="fatPercentage">
          <el-input v-model="form.fatPercentage" placeholder="请输入脂肪率" />
        </el-form-item>
        <el-form-item label="肌肉率" prop="musclePercentage">
          <el-input v-model="form.musclePercentage" placeholder="请输入肌肉率" />
        </el-form-item>
        <el-form-item label="骨格肌量" prop="skeletalMuscleVolume">
          <el-input v-model="form.skeletalMuscleVolume" placeholder="请输入骨格肌量" />
        </el-form-item>
        <el-form-item label="BMI" prop="bmi">
          <el-input v-model="form.bmi" placeholder="请输入BMI" />
        </el-form-item>
        <el-form-item label="内脏脂肪等级" prop="visceralFatGrade">
          <el-input v-model="form.visceralFatGrade" placeholder="请输入内脏脂肪等级" />
        </el-form-item>
        <el-form-item label="理想体重" prop="idealWeight">
          <el-input v-model="form.idealWeight" placeholder="请输入理想体重" />
        </el-form-item>
        <el-form-item label="身体年龄" prop="physicalAge">
          <el-input v-model="form.physicalAge" placeholder="请输入身体年龄" />
        </el-form-item>
        <el-form-item label="基础代谢率" prop="basalMetabolicRate">
          <el-input v-model="form.basalMetabolicRate" placeholder="请输入基础代谢率" />
        </el-form-item>
        <el-form-item label="胸围" prop="bust">
          <el-input v-model="form.bust" placeholder="请输入胸围" />
        </el-form-item>
        <el-form-item label="腰围" prop="waistline">
          <el-input v-model="form.waistline" placeholder="请输入腰围" />
        </el-form-item>
        <el-form-item label="臀围" prop="hipline">
          <el-input v-model="form.hipline" placeholder="请输入臀围" />
        </el-form-item>
        <el-form-item label="左臀围" prop="leftHipline">
          <el-input v-model="form.leftHipline" placeholder="请输入左臀围" />
        </el-form-item>
        <el-form-item label="右臀围" prop="rightHipline">
          <el-input v-model="form.rightHipline" placeholder="请输入右臀围" />
        </el-form-item>
        <el-form-item label="左大腿围" prop="leftThigCircumference">
          <el-input v-model="form.leftThigCircumference" placeholder="请输入左大腿围" />
        </el-form-item>
        <el-form-item label="右大腿围" prop="rightThigCircumference">
          <el-input v-model="form.rightThigCircumference" placeholder="请输入右大腿围" />
        </el-form-item>
        <el-form-item label="左小腿围" prop="leftCalfCircumference">
          <el-input v-model="form.leftCalfCircumference" placeholder="请输入左小腿围" />
        </el-form-item>
        <el-form-item label="右小腿围" prop="rightCalfCircumference">
          <el-input v-model="form.rightCalfCircumference" placeholder="请输入右小腿围" />
        </el-form-item>
        <el-form-item label="模型文件id" prop="modelFileId">
          <el-input v-model="form.modelFileId" placeholder="请输入模型文件id" />
        </el-form-item>
        <el-form-item label="报告文件id" prop="reportFileId">
          <el-input v-model="form.reportFileId" placeholder="请输入报告文件id" />
        </el-form-item>
        <el-form-item label="扫描用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入扫描用户id" />
        </el-form-item>
        <el-form-item label="体型值" prop="shapeValue">
          <el-input v-model="form.shapeValue" placeholder="请输入体型值" />
        </el-form-item>
        <el-form-item label="体型id" prop="shapeId">
          <el-input v-model="form.shapeId" placeholder="请输入体型id" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createReport, updateReport, deleteReport, getReport, getReportPage, exportReportExcel } from "@/api/scan/report";

export default {
  name: "Report",
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
      // 报告列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        createTime: [],
        code: null,
        name: null,
        sex: null,
        height: null,
        age: null,
        healthScore: null,
        highLowShoulders: null,
        headAskew: null,
        leftLegKokd: null,
        rightLegKokd: null,
        longShortLeg: null,
        pokingChin: null,
        pelvicAnteversion: null,
        leftKneeAnalysis: null,
        rightKneeAnalysis: null,
        leftRoundShoulder: null,
        rightRoundShoulder: null,
        focusTiltLeftRight: null,
        focusTiltForthBack: null,
        weight: null,
        proteinRate: null,
        boneMassMositureContent: null,
        fatPercentage: null,
        musclePercentage: null,
        skeletalMuscleVolume: null,
        bmi: null,
        visceralFatGrade: null,
        idealWeight: null,
        physicalAge: null,
        basalMetabolicRate: null,
        bust: null,
        waistline: null,
        hipline: null,
        leftHipline: null,
        rightHipline: null,
        leftThigCircumference: null,
        rightThigCircumference: null,
        leftCalfCircumference: null,
        rightCalfCircumference: null,
        modelFileId: null,
        reportFileId: null,
        userId: null,
        shapeValue: null,
        shapeId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [{ required: true, message: "报告编号不能为空", trigger: "blur" }],
        healthScore: [{ required: true, message: "健康评分不能为空", trigger: "blur" }],
        highLowShoulders: [{ required: true, message: "高低肩：左右肩关节Y坐标差值不能为空", trigger: "blur" }],
        headAskew: [{ required: true, message: "头侧歪：头和脖子不能为空", trigger: "blur" }],
        leftLegKokd: [{ required: true, message: "左腿XOKD：X型 O型 K型 D型腿不能为空", trigger: "blur" }],
        rightLegKokd: [{ required: true, message: "右腿XOKD：X型 O型 K型 D型腿不能为空", trigger: "blur" }],
        longShortLeg: [{ required: true, message: "长短腿：左右髋关节Y坐标差值不能为空", trigger: "blur" }],
        pokingChin: [{ required: true, message: "头部前倾：左耳和脖子不能为空", trigger: "blur" }],
        pelvicAnteversion: [{ required: true, message: "骨盆前倾：脖子和左髋关节与右髋关节中点,左踝关节与右踝关节中点不能为空", trigger: "blur" }],
        leftKneeAnalysis: [{ required: true, message: "左膝盖分析：前曲 超伸不能为空", trigger: "blur" }],
        rightKneeAnalysis: [{ required: true, message: "右膝盖分析：前曲 超伸不能为空", trigger: "blur" }],
        leftRoundShoulder: [{ required: true, message: "左圆肩：肩和肩峰不能为空", trigger: "blur" }],
        rightRoundShoulder: [{ required: true, message: "右圆肩：肩和肩峰不能为空", trigger: "blur" }],
        focusTiltLeftRight: [{ required: true, message: "重心左右倾斜：人体重心和左踝关节与右踝关节中点不能为空", trigger: "blur" }],
        focusTiltForthBack: [{ required: true, message: "重心前后倾斜：人体重心和左踝关节与右踝关节中点不能为空", trigger: "blur" }],
        weight: [{ required: true, message: "体重不能为空", trigger: "blur" }],
        proteinRate: [{ required: true, message: "蛋白率不能为空", trigger: "blur" }],
        boneMassMositureContent: [{ required: true, message: "骨量水分率不能为空", trigger: "blur" }],
        fatPercentage: [{ required: true, message: "脂肪率不能为空", trigger: "blur" }],
        musclePercentage: [{ required: true, message: "肌肉率不能为空", trigger: "blur" }],
        skeletalMuscleVolume: [{ required: true, message: "骨格肌量不能为空", trigger: "blur" }],
        bmi: [{ required: true, message: "BMI不能为空", trigger: "blur" }],
        visceralFatGrade: [{ required: true, message: "内脏脂肪等级不能为空", trigger: "blur" }],
        idealWeight: [{ required: true, message: "理想体重不能为空", trigger: "blur" }],
        physicalAge: [{ required: true, message: "身体年龄不能为空", trigger: "blur" }],
        basalMetabolicRate: [{ required: true, message: "基础代谢率不能为空", trigger: "blur" }],
        bust: [{ required: true, message: "胸围不能为空", trigger: "blur" }],
        waistline: [{ required: true, message: "腰围不能为空", trigger: "blur" }],
        hipline: [{ required: true, message: "臀围不能为空", trigger: "blur" }],
        leftHipline: [{ required: true, message: "左臀围不能为空", trigger: "blur" }],
        rightHipline: [{ required: true, message: "右臀围不能为空", trigger: "blur" }],
        leftThigCircumference: [{ required: true, message: "左大腿围不能为空", trigger: "blur" }],
        rightThigCircumference: [{ required: true, message: "右大腿围不能为空", trigger: "blur" }],
        leftCalfCircumference: [{ required: true, message: "左小腿围不能为空", trigger: "blur" }],
        rightCalfCircumference: [{ required: true, message: "右小腿围不能为空", trigger: "blur" }],
        modelFileId: [{ required: true, message: "模型文件id不能为空", trigger: "blur" }],
        reportFileId: [{ required: true, message: "报告文件id不能为空", trigger: "blur" }],
        userId: [{ required: true, message: "扫描用户id不能为空", trigger: "blur" }],
        shapeValue: [{ required: true, message: "体型值不能为空", trigger: "blur" }],
        shapeId: [{ required: true, message: "体型id不能为空", trigger: "blur" }],
      }
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
      getReportPage(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        code: undefined,
        name: undefined,
        sex: undefined,
        height: undefined,
        age: undefined,
        healthScore: undefined,
        highLowShoulders: undefined,
        headAskew: undefined,
        leftLegKokd: undefined,
        rightLegKokd: undefined,
        longShortLeg: undefined,
        pokingChin: undefined,
        pelvicAnteversion: undefined,
        leftKneeAnalysis: undefined,
        rightKneeAnalysis: undefined,
        leftRoundShoulder: undefined,
        rightRoundShoulder: undefined,
        focusTiltLeftRight: undefined,
        focusTiltForthBack: undefined,
        weight: undefined,
        proteinRate: undefined,
        boneMassMositureContent: undefined,
        fatPercentage: undefined,
        musclePercentage: undefined,
        skeletalMuscleVolume: undefined,
        bmi: undefined,
        visceralFatGrade: undefined,
        idealWeight: undefined,
        physicalAge: undefined,
        basalMetabolicRate: undefined,
        bust: undefined,
        waistline: undefined,
        hipline: undefined,
        leftHipline: undefined,
        rightHipline: undefined,
        leftThigCircumference: undefined,
        rightThigCircumference: undefined,
        leftCalfCircumference: undefined,
        rightCalfCircumference: undefined,
        modelFileId: undefined,
        reportFileId: undefined,
        id: undefined,
        userId: undefined,
        shapeValue: undefined,
        shapeId: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加报告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getReport(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报告";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.id != null) {
          updateReport(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createReport(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除报告编号为"' + id + '"的数据项?').then(function() {
        return deleteReport(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有报告数据项?').then(() => {
        this.exportLoading = true;
        return exportReportExcel(params);
      }).then(response => {
        this.$download.excel(response, '报告.xls');
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
