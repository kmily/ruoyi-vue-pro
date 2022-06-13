<template>
<!--
  用户选择框：
  使用方式：

    加载:

    import UserSelect from "@/components/UserSelect/UserSelect";
    export default {
      components: {
        UserSelect
      },
    }

    使用:
    <el-form-item label="用户" prop="user" >
      // 插入在这个地方即可, 注意类型是数组类型 `[1,2,3]`
      <user-select v-model="form.users"/>
    </el-form-item>


   操作方式：点击进入用户选择窗口，选择完成后X掉窗口就可以
   数据类型：数组类型，值为用户的id

-->
  <div>
    <el-input v-model="idVal" readonly="readonly">
      <el-button slot="append" icon="el-icon-search" @click="onClickSelectUser"></el-button>
    </el-input>
    <el-dialog title="用户选择" :visible.sync="open" append-to-body>
      <!--用户数据-->
      <div class="user-select-query-form">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">

<!--          TODO 如果想开启用户名称搜索需要在后端 UserPageReqVO 添加 nickname, 并且在Mapper添加相应的条件-->

<!--          <el-form-item label="用户名称" prop="nickname">-->
<!--            <el-input v-model="queryParams.nickname" placeholder="输入用户名称搜索" clearable style="width: 240px"-->
<!--                      @keyup.enter.native="handleQuery"/>-->
<!--          </el-form-item>-->

          <el-form-item label="手机号码" prop="mobile">
            <el-input v-model="queryParams.mobile" placeholder="输入手机号码搜索" clearable style="width: 240px"
                      @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item>
            <el-button circle type="primary" icon="el-icon-search" @click="handleQuery"></el-button>
            <el-button circle icon="el-icon-refresh" @click="resetQuery"></el-button>
            <el-button circle type="success" icon="el-icon-check " @click="cancel"></el-button>

          </el-form-item>
        </el-form>
      </div>
      <el-row :gutter="20">
        <!--部门数据-->
        <el-col :span="8" :xs="24">
          <div class="head-container">
            <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small" prefix-icon="el-icon-search"
                      style="margin-bottom: 20px"/>
          </div>
          <div class="head-container">
            <el-tree :data="deptOptions" :props="defaultProps" :filter-node-method="filterNode"
                     ref="tree" accordion @node-click="handleNodeClick" height="600px"/>
          </div>
        </el-col>
        <el-col :span="16" :xs="24">
          <el-table border v-loading="loading" :data="userList" height="300px" ref="multipleTable">
            <el-table-column label="用户编号" align="center" key="id" prop="id" v-if="columns[0].visible"/>
            <el-table-column label="用户昵称" align="center" key="nickname" prop="nickname" v-if="columns[2].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="部门" align="center" key="deptName" prop="dept.name" v-if="columns[3].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="手机号码" align="center" key="mobile" prop="mobile" v-if="columns[4].visible"
                             width="120"/>
            <el-table-column label="操作" align="center" width="60" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <span v-if="!isAdd(scope.row.id)">
                  <el-button size="mini" type="text" icon="el-icon-download" @click="handleAdd(scope.row)">添加</el-button>
                </span>
                <span v-else>
                  <el-button size="mini" type="text"  @click="handleDelete(scope.row)">已添加</el-button>
                </span>
              </template>
            </el-table-column>
          </el-table>
          <pagination small v-show="total>0" :total="total" :page.sync="queryParams.pageNo"
                      :limit.sync="queryParams.pageSize"
                      @pagination="getList"/>
          <div></div>
          <el-table border :data="userListSelected" height="300px" ref="multipleTable" class="user-select-select">
            <el-table-column label="用户编号" align="center" key="id" prop="id" v-if="columns[0].visible"/>
            <el-table-column label="用户昵称" align="center" key="nickname" prop="nickname" v-if="columns[2].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="部门" align="center" key="deptName" prop="dept.name" v-if="columns[3].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="手机号码" align="center" key="mobile" prop="mobile" v-if="columns[4].visible"
                             width="120"/>
            <el-table-column label="操作" align="center" width="60" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>


        </el-col>
      </el-row>

    </el-dialog>

  </div>
</template>

<script>
import {
  listSimpleUserToUserSelect, listSimpleUserToUserSelectByIds,
} from "@/api/system/user";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import {listSimpleDepts} from "@/api/system/dept";

export default {
  name: "User",
  components: {Treeselect},
  props: {
    //数据
    value: {
      type: Array,
      default: function () {
        return [];
      }
    },
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
      // 用户表格数据
      userList: [],
      userListSelected: [],
      idList: [],
      nameList: [],
      idText: "",
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "name"
      },
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        username: undefined,
        mobile: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        {key: 0, label: `用户编号`, visible: true},
        {key: 1, label: `用户名称`, visible: true},
        {key: 2, label: `用户昵称`, visible: true},
        {key: 3, label: `部门`, visible: true},
        {key: 4, label: `手机号码`, visible: true},
        {key: 5, label: `状态`, visible: true},
        {key: 6, label: `创建时间`, visible: true}
      ],
      // 是否显示弹出层（角色权限）
      openRole: false,
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  computed: {
    idVal() {
      if (this.userListSelected.length < 1 ){
        this.updateSelected();
      }

      let str = '';
      let end_index = this.userListSelected.length -1;
      for (let i = 0; i < this.userListSelected.length; i++) {
        str = str + this.userListSelected[i].nickname;
        if (i < end_index ){
          str = str + ','
        }
      }
      return str
    }
  },
  created() {
    this.queryParams.deptId = undefined;
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listSimpleUserToUserSelect(this.queryParams).then(response => {
          this.userList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 查询部门下拉树结构 + 岗位下拉 */
    getTreeSelect() {
      listSimpleDepts().then(response => {
        // 处理 deptOptions 参数
        this.deptOptions = [];
        this.deptOptions.push(...this.handleTree(response.data, "id"));
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.open = false;
    },
    // 格式化部门的下拉框
    normalizer(node) {
      return {
        id: node.id,
        label: node.name,
        children: node.children
      }
    },
    // 点击选择按钮后
    onClickSelectUser() {
      this.queryParams.deptId = undefined;
      this.open = true;
      this.getList();
      this.getTreeSelect();
      this.updateSelected();
    },
    // 更新已选中数据
    updateSelected(){
      if (this.value != null && this.value !== []) {
        listSimpleUserToUserSelectByIds(this.value).then(response => {
          if (response.data != null) {
            this.userListSelected = response.data;
          }
        })
      }
    },
    // 点击添加按钮后
    handleAdd(row) {
      for (let i = 0; i < this.userListSelected.length; i++) {
        if (row.id === this.userListSelected[i].id) {
          return;
        }
      }
      this.userListSelected.push(row)
      this.toIdList();
    },
    // 是否已经添加了
    isAdd(id) {
      for (let i = 0; i <this.userListSelected.length; i++) {
        let user = this.userListSelected[i];
        if (user.id === id){
          return true;
        }
      }
      return false;
    },
    // 删除
    handleDelete(row) {
      for (let i = 0; i < this.userListSelected.length; i++) {
        if (row.id === this.userListSelected[i].id) {
          this.userListSelected.splice(i, 1);
          this.toIdList();
          return;
        }
      }
    },
    toIdList() {
      this.idList = [];
      this.nameList = [];
      for (let i = 0; i < this.userListSelected.length; i++) {
        this.idList.push(this.userListSelected[i].id);
        this.nameList.push(this.userListSelected[i].nickname);
      }
      this.idText = this.nameList.toString();
      this.$emit("input", this.idList)
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
  }
};
</script>
<style scoped>
.user-select-select {
  margin-top: 20px;
}
</style>
