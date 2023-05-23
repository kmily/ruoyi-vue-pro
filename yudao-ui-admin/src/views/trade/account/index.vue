<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账号名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入账号名称" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['trade:account:create']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="账号名称" align="center" prop="name" />
      <el-table-column label="余额-USDT" align="center" prop="balance" :formatter="USDTformat"/>
      <el-table-column label="余额同步时间" align="center" prop="lastBalanceQueryTime" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关联用户" align="center" prop="relateUser" :formatter="userFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['trade:account:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-watch" @click="balance(scope.row)" >资产</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账号名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入账号名称" />
        </el-form-item>
        <el-form-item v-show="showSecret" label="api访问key" prop="appKey">
          <el-input v-model="form.appKey" placeholder="请输入api访问key" />
        </el-form-item>
        <el-form-item v-show="showSecret" label="api访问秘钥" prop="appSecret">
          <el-input v-model="form.appSecret" placeholder="请输入api访问秘钥" />
        </el-form-item>
        <el-form-item label="关联用户" prop="relateUser">
          <el-select style="width:100%" v-model="form.relateUser" placeholder="请输入账号管理用户ID" >
            <el-option v-for="item in sysUsers" :label="item.nickname" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟随账号" prop="relateCfg">
          <el-input v-model="relateCfg.relateAccount" placeholder="请输入账号管理用户ID" />
        </el-form-item>
        <el-form-item label="跟随模式" prop="mode" >
          <el-select style="width:100%" v-model="relateCfg.mode" placeholder="请选择关联模式" >
             <el-option label="仓位比例" value="position" />
             <el-option label="数量比例" value="count" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟随比例" prop="ratio">
          <el-input-number style="width:100%" :max="100" :min="0" :step="1" :controls="false" v-model="relateCfg.ratio" placeholder="请输入0-100,百分比数值" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 对话框(资产) -->
    <el-dialog title="资产详情" :visible.sync="balanceShow" width="800px" style="height:600px" close-on-click-modal append-to-body>
      <el-table v-loading="loading" :data="accountBalance" border>
        <el-table-column label="资产类型" align="center" prop="asset" />
        <el-table-column label="余额" align="center" prop="balance" />
        <el-table-column label="可提取余额" align="center" prop="withdrawAvailable" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="syncBalance">同 步</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createAccount, updateAccount, deleteAccount, getAccount, getAccountPage, exportAccountExcel,syncBalanceApi } from "@/api/trade/account";
import { listUser } from "@/api/system/user";
export default {
  name: "Account",
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
      // 交易账号列表
      list: [],
      sysUsers : [],
      // 账号资产
      accountBalance : [],
      operateAccountId: undefined,
      relateCfg: {},
      balanceShow : false,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      showSecret:true,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        name: null,
        appKey: null,
        appSecret: null,
        balance: null,
        lastBalanceQueryTime: [],
        createTime: [],
        relateUser: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    listUser({pageSize:-1}).then(response => {
      this.sysUsers = response.data.list;
    });
    this.getList();
  },
  methods: {
    userFormat(row){
      if(row.relateUser) {
        let relateUser = this.sysUsers.filter(item => item.id === row.relateUser)[0];
        if(relateUser) {
          return relateUser.nickname;
        }
        return row.relateUser;
      }
    },
    USDTformat(row) {
      if(row.balance) {
        let balanceArr = JSON.parse(row.balance);
        let USDTBalance = balanceArr.filter(item => item.asset === 'USDT')[0];
        return `余额：${USDTBalance.balance}\t 可提现：${USDTBalance.withdrawAvailable}`;
      }
      return '未同步';
    },
    balance(data) {
      this.operateAccountId = data.id;
      this.balanceShow = true;
      if(data.balance) {
        this.accountBalance = JSON.parse(data.balance);
      }
    },
    syncBalance() {
      syncBalanceApi(this.operateAccountId).then(response => {
        if(response.data) {
          this.accountBalance = JSON.parse(response.data);
        }
      });
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getAccountPage(this.queryParams).then(response => {
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
        id: undefined,
        name: undefined,
        appKey: undefined,
        appSecret: undefined,
        balance: undefined,
        lastBalanceQueryTime: undefined,
        relateUser: undefined,
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
      this.showSecret = true;
      this.reset();
      this.open = true;
      this.relateCfg = {};
      this.title = "添加交易账号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.showSecret = false;
      this.reset();
      const id = row.id;
      getAccount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.relateCfg = this.form.relateCfg ? JSON.parse(this.form.relateCfg) : {};
        this.title = "修改交易账号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        this.form.relateCfg = JSON.stringify(this.relateCfg);
        // 修改的提交
        if (this.form.id != null) {
          updateAccount(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createAccount(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除交易账号编号为"' + id + '"的数据项?').then(function() {
          return deleteAccount(id);
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
      this.$modal.confirm('是否确认导出所有交易账号数据项?').then(() => {
          this.exportLoading = true;
          return exportAccountExcel(params);
        }).then(response => {
          this.$download.excel(response, '交易账号.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
<style>
  .margin-top {
    margin-top:20px;
  }
</style>