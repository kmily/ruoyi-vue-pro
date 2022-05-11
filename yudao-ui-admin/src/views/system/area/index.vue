<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="数据版本" prop="dataVersion">
        <el-select label="数据版本" v-model="queryParams.dataVersion" placeholder="请输入数据版本" @change="loadVersionData" >
          <el-option label="2021" value="2021"/>
        </el-select>
      </el-form-item>
      <el-form-item label="省直辖市" prop="provinceCode">
      <el-select label="省直辖市" v-model="queryParams.provinceCode" placeholder="请选择省直辖市" @change="loadCities">
        <el-option v-for="data in provincesData" :key="data.id" :label="data.province" :value="data.provinceCode"/>
      </el-select>
      </el-form-item>
      <el-form-item label="市" prop="cityCode">
        <el-select label="市" v-model="queryParams.cityCode" placeholder="请选择市" @change="loadCountries">
          <el-option v-for="data in citiesData" :key="data.id" :label="data.city" :value="data.cityCode"/>
        </el-select>
      </el-form-item>
      <el-form-item label="区县" prop="countyCode">
        <el-select label="区县" v-model="queryParams.countyCode" placeholder="请选择区县" @change="loadTowns">
          <el-option v-for="data in countriesData" :key="data.id" :label="data.county" :value="data.countyCode"/>
        </el-select>
      </el-form-item>
      <el-form-item label="乡镇街道" prop="townCode">
        <el-select label="乡镇街道" v-model="queryParams.townCode" placeholder="请选择乡镇街道" @change="loadVillages">
          <el-option v-for="data in townsData" :key="data.id" :label="data.town" :value="data.townCode"/>
        </el-select>
      </el-form-item>
      <el-form-item label="村社区" prop="villageCode">
        <el-select label="村社区" v-model="queryParams.villageCode" placeholder="请选择村社区" @change="loadVillage">
          <el-option v-for="data in villagesData" :key="data.id" :label="data.village" :value="data.villageCode"/>
        </el-select>
      </el-form-item>
<!--      <el-form-item label="使用标识 1使用 0不使用" prop="useFlag">-->
<!--        <el-input v-model="queryParams.useFlag" placeholder="请输入使用标识 1使用 0不使用" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['system:area:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:area:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="数据版本" align="center" prop="dataVersion" />
      <el-table-column label="省直辖市" align="center" prop="province" />
      <el-table-column label="省直辖市编码" align="center" prop="provinceCode" />
      <el-table-column label="市" align="center" prop="city" />
      <el-table-column label="市编码" align="center" prop="cityCode" />
      <el-table-column label="区县" align="center" prop="county" />
      <el-table-column label="区县编码" align="center" prop="countyCode" />
      <el-table-column label="乡镇街道" align="center" prop="town" />
      <el-table-column label="乡镇编码" align="center" prop="townCode" />
      <el-table-column label="村,社区" align="center" prop="village" />
      <el-table-column label="村社区编码" align="center" prop="villageCode" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用标识" align="center" prop="useFlag" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:area:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:area:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="数据版本" prop="dataVersion">
          <el-input v-model="form.dataVersion" placeholder="请输入数据版本" />
        </el-form-item>
        <el-form-item label="省直辖市编码" prop="provinceCode">
          <el-input v-model="form.provinceCode" placeholder="请输入省直辖市编码" />
        </el-form-item>
        <el-form-item label="省直辖市" prop="province">
          <el-input v-model="form.province" placeholder="请输入省直辖市" />
        </el-form-item>
        <el-form-item label="市编码" prop="cityCode">
          <el-input v-model="form.cityCode" placeholder="请输入市编码" />
        </el-form-item>
        <el-form-item label="市" prop="city">
          <el-input v-model="form.city" placeholder="请输入市" />
        </el-form-item>
        <el-form-item label="区县编码" prop="countyCode">
          <el-input v-model="form.countyCode" placeholder="请输入区县编码" />
        </el-form-item>
        <el-form-item label="区县" prop="county">
          <el-input v-model="form.county" placeholder="请输入区县" />
        </el-form-item>
        <el-form-item label="乡镇编码" prop="townCode">
          <el-input v-model="form.townCode" placeholder="请输入乡镇编码" />
        </el-form-item>
        <el-form-item label="乡镇街道" prop="town">
          <el-input v-model="form.town" placeholder="请输入乡镇街道" />
        </el-form-item>
        <el-form-item label="村社区编码" prop="villageCode">
          <el-input v-model="form.villageCode" placeholder="请输入村社区编码" />
        </el-form-item>
        <el-form-item label="村,社区" prop="village">
          <el-input v-model="form.village" placeholder="请输入村,社区" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="使用标识 1使用 0不使用" prop="useFlag">
          <el-input v-model="form.useFlag" placeholder="请输入使用标识 1使用 0不使用" />
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
import { createArea, updateArea, deleteArea, getArea, getAreaPage, exportAreaExcel,provinces ,cities,counties,towns,villages} from "@/api/system/area";

export default {
  name: "Area",
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
      // 行政区域列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      dateRangeCreateTime: [],
      provincesData:[],
      citiesData:[],
      countriesData:[],
      townsData:[],
      villagesData:[],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        dataVersion: 2021,
        provinceCode: null,
        province: null,
        cityCode: null,
        city: null,
        countyCode: null,
        county: null,
        townCode: null,
        town: null,
        villageCode: null,
        village: null,
        longitude: null,
        latitude: null,
        useFlag: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dataVersion: [{ required: true, message: "数据版本不能为空", trigger: "blur" }],
        provinceCode: [{ required: true, message: "省直辖市编码不能为空", trigger: "blur" }],
        province: [{ required: true, message: "省直辖市不能为空", trigger: "blur" }],
        cityCode: [{ required: true, message: "市编码不能为空", trigger: "blur" }],
        city: [{ required: true, message: "市不能为空", trigger: "blur" }],
        countyCode: [{ required: true, message: "区县编码不能为空", trigger: "blur" }],
        county: [{ required: true, message: "区县不能为空", trigger: "blur" }],
        townCode: [{ required: true, message: "乡镇编码不能为空", trigger: "blur" }],
        town: [{ required: true, message: "乡镇街道不能为空", trigger: "blur" }],
        villageCode: [{ required: true, message: "村社区编码不能为空", trigger: "blur" }],
        village: [{ required: true, message: "村,社区不能为空", trigger: "blur" }],
        longitude: [{ required: true, message: "经度不能为空", trigger: "blur" }],
        latitude: [{ required: true, message: "纬度不能为空", trigger: "blur" }],
        useFlag: [{ required: true, message: "使用标识 1使用 0不使用不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
    this.loadProvince();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 处理查询参数
      let params = {...this.queryParams};
      this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行查询
      getAreaPage(params).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    loadVersionData(){
      this.queryParams.provinceCode = null;
      this.queryParams.cityCode = null;
      this.queryParams.countyCode = null;
      this.queryParams.townCode = null;
      this.queryParams.villageCode = null;
      this.getList();
    },

    loadProvince(){
      provinces(this.queryParams).then(response => {
        this.provincesData = response.data
      })
    },
    loadCities(provinceCode) {
      this.queryParams.provinceCode = provinceCode;
      cities(this.queryParams).then(response => {
        this.citiesData  = response.data;
        this.queryParams.cityCode = null;
        this.queryParams.countyCode = null;
        this.queryParams.townCode = null;
        this.queryParams.villageCode = null;
        this.getList();
      })
    },
    loadCountries(cityCode) {
      this.queryParams.cityCode = cityCode;
      counties(this.queryParams).then(response => {
        this.countriesData = response.data;
        this.queryParams.countyCode = null;
        this.queryParams.townCode = null;
        this.queryParams.villageCode = null;
        this.getList();
      })
    },
    loadTowns(countryCode){
      this.queryParams.countryCode = countryCode;
      towns(this.queryParams).then(response => {
        this.townsData = response.data;
        this.queryParams.townCode = null;
        this.queryParams.villageCode = null;
        this.getList();
      })
    },
    loadVillages(townCode){
      this.queryParams.townCode = townCode;
      villages(this.queryParams).then(response => {
        this.villagesData = response.data;
        this.queryParams.villageCode = null;
        this.getList();
      })
    },
    loadVillage(villageCode){
      this.queryParams.villageCode = villageCode;
      this.getList();
    },


    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        dataVersion: undefined,
        provinceCode: undefined,
        province: undefined,
        cityCode: undefined,
        city: undefined,
        countyCode: undefined,
        county: undefined,
        townCode: undefined,
        town: undefined,
        villageCode: undefined,
        village: undefined,
        longitude: undefined,
        latitude: undefined,
        useFlag: undefined,
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
      this.dateRangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加行政区域";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getArea(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改行政区域";
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
          updateArea(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createArea(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除行政区域编号为"' + id + '"的数据项?').then(function() {
          return deleteArea(id);
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
      this.addBeginAndEndTime(params, this.dateRangeCreateTime, 'createTime');
      // 执行导出
      this.$modal.confirm('是否确认导出所有行政区域数据项?').then(() => {
          this.exportLoading = true;
          return exportAreaExcel(params);
        }).then(response => {
          this.$download.excel(response, '${table.classComment}.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
