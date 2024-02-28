<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="广告名称" prop="adName">
                      <el-input v-model="formData.adName" placeholder="请输入广告名称" />
                    </el-form-item>
      </el-form>
                  <!-- 子表的表单 -->
          <el-tabs v-model="subTabsName">
                <el-tab-pane label="广告" name="ad">
                  <AdForm ref="adFormRef" :block-id="formData.id" :datas="this.getDictDatas(DICT_TYPE.COMMON_STATUS)" />
                </el-tab-pane>
          </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="formLoading">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as AdBlockApi from '@/api/steam/adblock';
          import AdForm from './components/AdForm.vue'
  export default {
    name: "AdBlockForm",
    components: {
                               AdForm,
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
                            adName: undefined,
        },
        // 表单校验
        formRules: {
        },
                              /** 子表的表单 */
             subTabsName: 'ad'
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
            const res = await AdBlockApi.getAdBlock(id);
            this.formData = res.data;
            this.title = "修改广告位";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增广告位";
              },
      /** 提交按钮 */
      async submitForm() {
        // 校验主表
        await this.$refs["formRef"].validate();
                          // 校验子表
                    try {
                                            await this.$refs['adFormRef'].validate();
                    } catch (e) {
                      this.subTabsName = 'ad';
                      return;
                    }
        this.formLoading = true;
        try {
          const data = this.formData;
                    // 拼接子表的数据
              data.ads = this.$refs['adFormRef'].getData();
          // 修改的提交
          if (data.id) {
            await AdBlockApi.updateAdBlock(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await AdBlockApi.createAdBlock(data);
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
                            adName: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
