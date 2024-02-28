<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="price" prop="price">
                      <el-input v-model="formData.price" placeholder="请输入price" />
                    </el-form-item>
                    <el-form-item label="quantity" prop="quantity">
                      <el-input v-model="formData.quantity" placeholder="请输入quantity" />
                    </el-form-item>
                    <el-form-item label="deals" prop="deals">
                      <el-input v-model="formData.deals" placeholder="请输入deals" />
                    </el-form-item>
                    <el-form-item label="item_id" prop="itemId">
                      <el-input v-model="formData.itemId" placeholder="请输入item_id" />
                    </el-form-item>
                    <el-form-item label="app_id" prop="appId">
                      <el-input v-model="formData.appId" placeholder="请输入app_id" />
                    </el-form-item>
                    <el-form-item label="itemName" prop="itemName">
                      <el-input v-model="formData.itemName" placeholder="请输入itemName" />
                    </el-form-item>
                    <el-form-item label="shortName" prop="shortName">
                      <el-input v-model="formData.shortName" placeholder="请输入shortName" />
                    </el-form-item>
                    <el-form-item label="marketHashName" prop="marketHashName">
                      <el-input v-model="formData.marketHashName" placeholder="请输入marketHashName" />
                    </el-form-item>
                    <el-form-item label="imageUrl" prop="imageUrl">
                      <el-input v-model="formData.imageUrl" placeholder="请输入imageUrl" />
                    </el-form-item>
                    <el-form-item label="itemInfo" prop="itemInfo">
                      <el-input v-model="formData.itemInfo" placeholder="请输入itemInfo" />
                    </el-form-item>
                    <el-form-item label="sellType" prop="sellType">
                      <el-input v-model="formData.sellType" placeholder="请输入sellType" />
                    </el-form-item>
                    <el-form-item label="currencyId" prop="currencyId">
                      <el-input v-model="formData.currencyId" placeholder="请输入currencyId" />
                    </el-form-item>
                    <el-form-item label="cnyPrice" prop="cnyPrice">
                      <el-input v-model="formData.cnyPrice" placeholder="请输入cnyPrice" />
                    </el-form-item>
                    <el-form-item label="salePrice" prop="salePrice">
                      <el-input v-model="formData.salePrice" placeholder="请输入salePrice" />
                    </el-form-item>
                    <el-form-item label="subsidyPrice" prop="subsidyPrice">
                      <el-input v-model="formData.subsidyPrice" placeholder="请输入subsidyPrice" />
                    </el-form-item>
                    <el-form-item label="activityTag" prop="activityTag">
                      <el-input v-model="formData.activityTag" placeholder="请输入activityTag" />
                    </el-form-item>
                    <el-form-item label="tagList" prop="tagList">
                      <el-input v-model="formData.tagList" placeholder="请输入tagList" />
                    </el-form-item>
                    <el-form-item label="subsidyTag" prop="subsidyTag">
                      <el-input v-model="formData.subsidyTag" placeholder="请输入subsidyTag" />
                    </el-form-item>
                    <el-form-item label="自动发货价格" prop="autoPrice">
                      <el-input v-model="formData.autoPrice" placeholder="请输入自动发货价格" />
                    </el-form-item>
                    <el-form-item label="自动发货数量" prop="autoQuantity">
                      <el-input v-model="formData.autoQuantity" placeholder="请输入自动发货数量" />
                    </el-form-item>
                    <el-form-item label="参考价" prop="referencePrice">
                      <el-input v-model="formData.referencePrice" placeholder="请输入参考价" />
                    </el-form-item>
                    <el-form-item label="类别选择" prop="selQuality">
                      <el-input v-model="formData.selQuality" placeholder="请输入类别选择" />
                    </el-form-item>
                    <el-form-item label="收藏品选择" prop="selItemset">
                      <el-input v-model="formData.selItemset" placeholder="请输入收藏品选择" />
                    </el-form-item>
                    <el-form-item label="武器选择" prop="selWeapon">
                      <el-input v-model="formData.selWeapon" placeholder="请输入武器选择" />
                    </el-form-item>
                    <el-form-item label="外观选择" prop="selExterior">
                      <el-input v-model="formData.selExterior" placeholder="请输入外观选择" />
                    </el-form-item>
                    <el-form-item label="品质选择" prop="selRarity">
                      <el-input v-model="formData.selRarity" placeholder="请输入品质选择" />
                    </el-form-item>
                    <el-form-item label="类型选择" prop="selType">
                      <el-input v-model="formData.selType" placeholder="请输入类型选择" />
                    </el-form-item>
                    <el-form-item label="是否存在库存" prop="existInv">
                      <el-radio-group v-model="formData.existInv">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
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
  import * as InvPreviewApi from '@/api/steam/invpreview';
      export default {
    name: "InvPreviewForm",
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
                            price: undefined,
                            quantity: undefined,
                            deals: undefined,
                            itemId: undefined,
                            appId: undefined,
                            itemName: undefined,
                            shortName: undefined,
                            marketHashName: undefined,
                            imageUrl: undefined,
                            itemInfo: undefined,
                            sellType: undefined,
                            currencyId: undefined,
                            cnyPrice: undefined,
                            salePrice: undefined,
                            subsidyPrice: undefined,
                            activityTag: undefined,
                            tagList: undefined,
                            subsidyTag: undefined,
                            autoPrice: undefined,
                            autoQuantity: undefined,
                            referencePrice: undefined,
                            selQuality: undefined,
                            selItemset: undefined,
                            selWeapon: undefined,
                            selExterior: undefined,
                            selRarity: undefined,
                            selType: undefined,
                            existInv: undefined,
        },
        // 表单校验
        formRules: {
                        itemId: [{ required: true, message: 'item_id不能为空', trigger: 'blur' }],
                        existInv: [{ required: true, message: '是否存在库存不能为空', trigger: 'blur' }],
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
            const res = await InvPreviewApi.getInvPreview(id);
            this.formData = res.data;
            this.title = "修改饰品在售预览";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增饰品在售预览";
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
            await InvPreviewApi.updateInvPreview(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await InvPreviewApi.createInvPreview(data);
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
                            price: undefined,
                            quantity: undefined,
                            deals: undefined,
                            itemId: undefined,
                            appId: undefined,
                            itemName: undefined,
                            shortName: undefined,
                            marketHashName: undefined,
                            imageUrl: undefined,
                            itemInfo: undefined,
                            sellType: undefined,
                            currencyId: undefined,
                            cnyPrice: undefined,
                            salePrice: undefined,
                            subsidyPrice: undefined,
                            activityTag: undefined,
                            tagList: undefined,
                            subsidyTag: undefined,
                            autoPrice: undefined,
                            autoQuantity: undefined,
                            referencePrice: undefined,
                            selQuality: undefined,
                            selItemset: undefined,
                            selWeapon: undefined,
                            selExterior: undefined,
                            selRarity: undefined,
                            selType: undefined,
                            existInv: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>