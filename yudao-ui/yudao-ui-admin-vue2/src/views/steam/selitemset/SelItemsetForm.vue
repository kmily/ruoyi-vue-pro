<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="父级编号" prop="parentId">
                      <TreeSelect
                        v-model="formData.parentId"
                        :options="selItemsetTree"
                        :normalizer="normalizer"
                        placeholder="请选择父级编号"
                      />
                    </el-form-item>
                    <el-form-item label="英文名称" prop="internalName">
                      <el-input v-model="formData.internalName" placeholder="请输入英文名称" />
                    </el-form-item>
                    <el-form-item label="中文名称" prop="localizedTagName">
                      <el-input v-model="formData.localizedTagName" placeholder="请输入中文名称" />
                    </el-form-item>
      </el-form>
              <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="formLoading">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as SelItemsetApi from '@/api/steam/selitemset';
    import TreeSelect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
    export default {
    name: "SelItemsetForm",
    components: {
                  TreeSelect,
            },
    data() {
      return {
        // 弹出层标题
        dialogTitle: "",
        // 是否显示弹出层
        dialogVisible: false,
        // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
        formLoading: false,
        // 表单参数
        formData: {
                            id: undefined,
                            parentId: undefined,
                            internalName: undefined,
                            localizedTagName: undefined,
        },
        // 表单校验
        formRules: {
                        parentId: [{ required: true, message: '父级编号不能为空', trigger: 'blur' }],
                        internalName: [{ required: true, message: '英文名称不能为空', trigger: 'blur' }],
                        localizedTagName: [{ required: true, message: '中文名称不能为空', trigger: 'blur' }],
        },
                       selItemsetTree: [], // 树形结构
              };
    },
    methods: {
      /** 打开弹窗 */
     async open(id) {
        this.dialogVisible = true;
        this.reset();
        // 修改时，设置数据
        if (id) {
          this.formLoading = true;
          try {
            const res = await SelItemsetApi.getSelItemset(id);
            this.formData = res.data;
            this.title = "修改收藏品选择";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增收藏品选择";
                await this.getSelItemsetTree();
      },
      /** 提交按钮 */
      async submitForm() {
        // 校验主表
        await this.$refs["formRef"].validate();
                  this.formLoading = true;
        try {
          const data = this.formData;
                  // 修改的提交
          if (data.id) {
            await SelItemsetApi.updateSelItemset(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await SelItemsetApi.createSelItemset(data);
          this.$modal.msgSuccess("新增成功");
          this.dialogVisible = false;
          this.$emit('success');
        } finally {
          this.formLoading = false;
        }
      },
                  /** 获得收藏品选择树 */
         async getSelItemsetTree() {
            this.selItemsetTree = [];
            const res = await SelItemsetApi.getSelItemsetList();
            const root = { id: 0, name: '顶级收藏品选择', children: [] };
            root.children = this.handleTree(res.data, 'id', 'parentId')
            this.selItemsetTree.push(root)
          },
                  /** 转换收藏品选择数据结构 */
          normalizer(node) {
            if (node.children && !node.children.length) {
              delete node.children;
            }
                return {
                  id: node.id,
                  label: node['internalName'],
                  children: node.children
                };
          },
      /** 表单重置 */
      reset() {
        this.formData = {
                            id: undefined,
                            parentId: undefined,
                            internalName: undefined,
                            localizedTagName: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>