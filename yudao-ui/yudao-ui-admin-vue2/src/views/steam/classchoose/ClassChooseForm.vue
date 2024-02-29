<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="分组名称" prop="name">
                      <el-input v-model="formData.name" placeholder="请输入分组名称" />
                    </el-form-item>
                    <el-form-item label="描述">
                      <Editor v-model="formData.description" :min-height="192"/>
                    </el-form-item>
                    <el-form-item label="状态" prop="status">
                      <el-select v-model="formData.status" placeholder="请选择状态">
                            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
                      </el-select>
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
  import * as ClassChooseApi from '@/api/steam/classchoose';
  import Editor from '@/components/Editor';
      export default {
    name: "ClassChooseForm",
    components: {
          Editor,
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
                            name: undefined,
                            description: undefined,
                            status: undefined,
        },
        // 表单校验
        formRules: {
                        name: [{ required: true, message: '分组名称不能为空', trigger: 'blur' }],
                        status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
        },
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
            const res = await ClassChooseApi.getClassChoose(id);
            this.formData = res.data;
            this.title = "修改下拉选择菜单";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增下拉选择菜单";
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
            await ClassChooseApi.updateClassChoose(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await ClassChooseApi.createClassChoose(data);
          this.$modal.msgSuccess("新增成功");
          this.dialogVisible = false;
          this.$emit('success');
        } finally {
          this.formLoading = false;
        }
      },
                      /** 表单重置 */
      reset() {
        this.formData = {
                            id: undefined,
                            name: undefined,
                            description: undefined,
                            status: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
