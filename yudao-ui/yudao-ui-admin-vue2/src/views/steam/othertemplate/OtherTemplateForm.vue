<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="出售平台id" prop="platformIdentity">
                      <el-input v-model="formData.platformIdentity" placeholder="请输入出售平台id" />
                    </el-form-item>
                    <el-form-item label="外观" prop="exterior">
                      <el-input v-model="formData.exterior" placeholder="请输入外观" />
                    </el-form-item>
                    <el-form-item label="外观名称" prop="exteriorName">
                      <el-input v-model="formData.exteriorName" placeholder="请输入外观名称" />
                    </el-form-item>
                    <el-form-item label="饰品id" prop="itemId">
                      <el-input v-model="formData.itemId" placeholder="请输入饰品id" />
                    </el-form-item>
                    <el-form-item label="饰品名称" prop="itemName">
                      <el-input v-model="formData.itemName" placeholder="请输入饰品名称" />
                    </el-form-item>
                    <el-form-item label="marketHashName" prop="marketHashName">
                      <el-input v-model="formData.marketHashName" placeholder="请输入marketHashName" />
                    </el-form-item>
                    <el-form-item label="自动发货在售最低价" prop="autoDeliverPrice">
                      <el-input v-model="formData.autoDeliverPrice" placeholder="请输入自动发货在售最低价" />
                    </el-form-item>
                    <el-form-item label="品质" prop="quality">
                      <el-input v-model="formData.quality" placeholder="请输入品质" />
                    </el-form-item>
                    <el-form-item label="稀有度" prop="rarity">
                      <el-input v-model="formData.rarity" placeholder="请输入稀有度" />
                    </el-form-item>
                    <el-form-item label="steam类型" prop="type">
                      <el-input v-model="formData.type" placeholder="请输入steam类型" />
                    </el-form-item>
                    <el-form-item label="图片地址" prop="imageUrl">
                      <el-input v-model="formData.imageUrl" placeholder="请输入图片地址" />
                    </el-form-item>
                    <el-form-item label="自动发货在售数量" prop="autoDeliverQuantity">
                      <el-input v-model="formData.autoDeliverQuantity" placeholder="请输入自动发货在售数量" />
                    </el-form-item>
                    <el-form-item label="品质颜色" prop="qualityColor">
                      <el-input v-model="formData.qualityColor" placeholder="请输入品质颜色" />
                    </el-form-item>
                    <el-form-item label="品质名称" prop="qualityName">
                      <el-input v-model="formData.qualityName" placeholder="请输入品质名称" />
                    </el-form-item>
                    <el-form-item label="稀有度颜色" prop="rarityColor">
                      <el-input v-model="formData.rarityColor" placeholder="请输入稀有度颜色" />
                    </el-form-item>
                    <el-form-item label="稀有度名称" prop="rarityName">
                      <el-input v-model="formData.rarityName" placeholder="请输入稀有度名称" />
                    </el-form-item>
                    <el-form-item label="短名称，去掉前缀" prop="shortName">
                      <el-input v-model="formData.shortName" placeholder="请输入短名称，去掉前缀" />
                    </el-form-item>
                    <el-form-item label="steam类型名称" prop="typeName">
                      <el-input v-model="formData.typeName" placeholder="请输入steam类型名称" />
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
  import * as OtherTemplateApi from '@/api/steam/othertemplate';
      export default {
    name: "OtherTemplateForm",
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
                            platformIdentity: undefined,
                            exterior: undefined,
                            exteriorName: undefined,
                            itemId: undefined,
                            itemName: undefined,
                            marketHashName: undefined,
                            autoDeliverPrice: undefined,
                            quality: undefined,
                            rarity: undefined,
                            type: undefined,
                            imageUrl: undefined,
                            autoDeliverQuantity: undefined,
                            qualityColor: undefined,
                            qualityName: undefined,
                            rarityColor: undefined,
                            rarityName: undefined,
                            shortName: undefined,
                            typeName: undefined,
        },
        // 表单校验
        formRules: {
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
            const res = await OtherTemplateApi.getOtherTemplate(id);
            this.formData = res.data;
            this.title = "修改其他平台模板";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增其他平台模板";
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
            await OtherTemplateApi.updateOtherTemplate(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await OtherTemplateApi.createOtherTemplate(data);
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
                            platformIdentity: undefined,
                            exterior: undefined,
                            exteriorName: undefined,
                            itemId: undefined,
                            itemName: undefined,
                            marketHashName: undefined,
                            autoDeliverPrice: undefined,
                            quality: undefined,
                            rarity: undefined,
                            type: undefined,
                            imageUrl: undefined,
                            autoDeliverQuantity: undefined,
                            qualityColor: undefined,
                            qualityName: undefined,
                            rarityColor: undefined,
                            rarityName: undefined,
                            shortName: undefined,
                            typeName: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>