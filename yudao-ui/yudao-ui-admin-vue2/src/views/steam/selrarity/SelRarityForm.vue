<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="中文名称" prop="localizedTagName">
                      <el-input v-model="formData.localizedTagName" placeholder="请输入中文名称" />
                    </el-form-item>
                    <el-form-item label="英文名称" prop="internalName">
                      <el-input v-model="formData.internalName" placeholder="请输入英文名称" />
                    </el-form-item>
                    <el-form-item label="色彩" prop="color">
                      <el-input v-model="formData.color" placeholder="请输入色彩" />
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
  import * as SelRarityApi from '@/api/steam/selrarity';
      export default {
    name: "SelRarityForm",
    components: {
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
                            localizedTagName: undefined,
                            internalName: undefined,
                            color: undefined,
        },
        // 表单校验
        formRules: {},
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
            const res = await SelRarityApi.getSelRarity(id);
            this.formData = res.data;
            this.title = "修改品质选择";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增品质选择";
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
            await SelRarityApi.updateSelRarity(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await SelRarityApi.createSelRarity(data);
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
                            localizedTagName: undefined,
                            internalName: undefined,
                            color: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
