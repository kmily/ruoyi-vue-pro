<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="appid" prop="appid">
                      <el-input v-model="formData.appid" placeholder="请输入appid" />
                    </el-form-item>
                    <el-form-item label="classid" prop="classid">
                      <el-input v-model="formData.classid" placeholder="请输入classid" />
                    </el-form-item>
                    <el-form-item label="instanceid" prop="instanceid">
                      <el-input v-model="formData.instanceid" placeholder="请输入instanceid" />
                    </el-form-item>
                    <el-form-item label="currency" prop="currency">
                      <el-input v-model="formData.currency" placeholder="请输入currency" />
                    </el-form-item>
                    <el-form-item label="background_color" prop="backgroundColor">
                      <el-input v-model="formData.backgroundColor" placeholder="请输入background_color" />
                    </el-form-item>
                    <el-form-item label="icon_url" prop="iconUrl">
                      <el-input v-model="formData.iconUrl" placeholder="请输入icon_url" />
                    </el-form-item>
                    <el-form-item label="icon_url_large" prop="iconUrlLarge">
                      <el-input v-model="formData.iconUrlLarge" placeholder="请输入icon_url_large" />
                    </el-form-item>
                    <el-form-item label="descriptions" prop="descriptions">
                      <el-input v-model="formData.descriptions" placeholder="请输入descriptions" />
                    </el-form-item>
                    <el-form-item label="tradable" prop="tradable">
                      <el-input v-model="formData.tradable" placeholder="请输入tradable" />
                    </el-form-item>
                    <el-form-item label="actions" prop="actions">
                      <el-input v-model="formData.actions" placeholder="请输入actions" />
                    </el-form-item>
                    <el-form-item label="fraudwarnings" prop="fraudwarnings">
                      <el-input v-model="formData.fraudwarnings" placeholder="请输入fraudwarnings" />
                    </el-form-item>
                    <el-form-item label="name" prop="name">
                      <el-input v-model="formData.name" placeholder="请输入name" />
                    </el-form-item>
                    <el-form-item label="name_color" prop="nameColor">
                      <el-input v-model="formData.nameColor" placeholder="请输入name_color" />
                    </el-form-item>
                    <el-form-item label="type" prop="type">
                      <el-select v-model="formData.type" placeholder="请选择type">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="market_name" prop="marketName">
                      <el-input v-model="formData.marketName" placeholder="请输入market_name" />
                    </el-form-item>
                    <el-form-item label="market_hash_name" prop="marketHashName">
                      <el-input v-model="formData.marketHashName" placeholder="请输入market_hash_name" />
                    </el-form-item>
                    <el-form-item label="market_actions" prop="marketActions">
                      <el-input v-model="formData.marketActions" placeholder="请输入market_actions" />
                    </el-form-item>
                    <el-form-item label="commodity" prop="commodity">
                      <el-input v-model="formData.commodity" placeholder="请输入commodity" />
                    </el-form-item>
                    <el-form-item label="market_tradable_restriction" prop="marketTradableRestriction">
                      <el-input v-model="formData.marketTradableRestriction" placeholder="请输入market_tradable_restriction" />
                    </el-form-item>
                    <el-form-item label="marketable" prop="marketable">
                      <el-input v-model="formData.marketable" placeholder="请输入marketable" />
                    </el-form-item>
                    <el-form-item label="描述" prop="tags">
                      <el-input v-model="formData.tags" placeholder="请输入描述" />
                    </el-form-item>
                    <el-form-item label="主键" prop="id">
                      <el-input v-model="formData.id" placeholder="请输入主键" />
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
                      <el-select v-model="formData.selType" placeholder="请选择类型选择">
                            <el-option label="请选择字典生成" value="" />
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
  import * as InvDescApi from '@/api/steam/invdesc';
      export default {
    name: "InvDescForm",
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
                            appid: undefined,
                            classid: undefined,
                            instanceid: undefined,
                            currency: undefined,
                            backgroundColor: undefined,
                            iconUrl: undefined,
                            iconUrlLarge: undefined,
                            descriptions: undefined,
                            tradable: undefined,
                            actions: undefined,
                            fraudwarnings: undefined,
                            name: undefined,
                            nameColor: undefined,
                            type: undefined,
                            marketName: undefined,
                            marketHashName: undefined,
                            marketActions: undefined,
                            commodity: undefined,
                            marketTradableRestriction: undefined,
                            marketable: undefined,
                            tags: undefined,
                            id: undefined,
                            selQuality: undefined,
                            selItemset: undefined,
                            selWeapon: undefined,
                            selExterior: undefined,
                            selRarity: undefined,
                            selType: undefined,
        },
        // 表单校验
        formRules: {
                        id: [{ required: true, message: '主键不能为空', trigger: 'blur' }],
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
            const res = await InvDescApi.getInvDesc(id);
            this.formData = res.data;
            this.title = "修改库存信息详情";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增库存信息详情";
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
            await InvDescApi.updateInvDesc(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await InvDescApi.createInvDesc(data);
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
                            appid: undefined,
                            classid: undefined,
                            instanceid: undefined,
                            currency: undefined,
                            backgroundColor: undefined,
                            iconUrl: undefined,
                            iconUrlLarge: undefined,
                            descriptions: undefined,
                            tradable: undefined,
                            actions: undefined,
                            fraudwarnings: undefined,
                            name: undefined,
                            nameColor: undefined,
                            type: undefined,
                            marketName: undefined,
                            marketHashName: undefined,
                            marketActions: undefined,
                            commodity: undefined,
                            marketTradableRestriction: undefined,
                            marketable: undefined,
                            tags: undefined,
                            id: undefined,
                            selQuality: undefined,
                            selItemset: undefined,
                            selWeapon: undefined,
                            selExterior: undefined,
                            selRarity: undefined,
                            selType: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
