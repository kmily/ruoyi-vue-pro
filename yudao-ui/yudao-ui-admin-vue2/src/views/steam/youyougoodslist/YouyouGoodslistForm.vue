<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="商品模板id" prop="templateId">
                      <el-input v-model="formData.templateId" placeholder="请输入商品模板id" />
                    </el-form-item>
                    <el-form-item label="商品名称" prop="commodityName">
                      <el-input v-model="formData.commodityName" placeholder="请输入商品名称" />
                    </el-form-item>
                    <el-form-item label="商品价格（单位元）" prop="commodityPrice">
                      <el-input v-model="formData.commodityPrice" placeholder="请输入商品价格（单位元）" />
                    </el-form-item>
                    <el-form-item label="商品磨损度" prop="commodityAbrade">
                      <el-input v-model="formData.commodityAbrade" placeholder="请输入商品磨损度" />
                    </el-form-item>
                    <el-form-item label="图案模板" prop="commodityPaintSeed">
                      <el-input v-model="formData.commodityPaintSeed" placeholder="请输入图案模板" />
                    </el-form-item>
                    <el-form-item label="皮肤编号" prop="commodityPaintIndex">
                      <el-input v-model="formData.commodityPaintIndex" placeholder="请输入皮肤编号" />
                    </el-form-item>
                    <el-form-item label="是否有名称标签：0否1是" prop="commodityHaveNameTag">
                      <el-input v-model="formData.commodityHaveNameTag" placeholder="请输入是否有名称标签：0否1是" />
                    </el-form-item>
                    <el-form-item label="是否有布章：0否1是" prop="commodityHaveBuzhang">
                      <el-input v-model="formData.commodityHaveBuzhang" placeholder="请输入是否有布章：0否1是" />
                    </el-form-item>
                    <el-form-item label="是否有印花：0否1是" prop="commodityHaveSticker">
                      <el-input v-model="formData.commodityHaveSticker" placeholder="请输入是否有印花：0否1是" />
                    </el-form-item>
                    <el-form-item label="发货模式：0,卖家直发；1,极速发货" prop="shippingMode">
                      <el-input v-model="formData.shippingMode" placeholder="请输入发货模式：0,卖家直发；1,极速发货" />
                    </el-form-item>
                    <el-form-item label="是否渐变色：0否1是" prop="templateisFade">
                      <el-input v-model="formData.templateisFade" placeholder="请输入是否渐变色：0否1是" />
                    </el-form-item>
                    <el-form-item label="Integer	是否表面淬火：0否1是" prop="templateisHardened">
                      <el-input v-model="formData.templateisHardened" placeholder="请输入Integer	是否表面淬火：0否1是" />
                    </el-form-item>
                    <el-form-item label="是否多普勒：0否1是" prop="templateisDoppler">
                      <el-input v-model="formData.templateisDoppler" placeholder="请输入是否多普勒：0否1是" />
                    </el-form-item>
                    <el-form-item label="印花Id" prop="commodityStickersStickerId">
                      <el-input v-model="formData.commodityStickersStickerId" placeholder="请输入印花Id" />
                    </el-form-item>
                    <el-form-item label="插槽编号" prop="commodityStickersRawIndex">
                      <el-input v-model="formData.commodityStickersRawIndex" placeholder="请输入插槽编号" />
                    </el-form-item>
                    <el-form-item label="印花名称" prop="commodityStickersName">
                      <el-input v-model="formData.commodityStickersName" placeholder="请输入印花名称" />
                    </el-form-item>
                    <el-form-item label="唯一名称" prop="commodityStickersHashName">
                      <el-input v-model="formData.commodityStickersHashName" placeholder="请输入唯一名称" />
                    </el-form-item>
                    <el-form-item label="材料" prop="commodityStickersMaterial">
                      <el-input v-model="formData.commodityStickersMaterial" placeholder="请输入材料" />
                    </el-form-item>
                    <el-form-item label="图片链接地址" prop="commodityStickersImgUrl">
                      <el-input v-model="formData.commodityStickersImgUrl" placeholder="请输入图片链接地址" />
                    </el-form-item>
                    <el-form-item label="印花价格(单位元)" prop="commodityStickersPrice">
                      <el-input v-model="formData.commodityStickersPrice" placeholder="请输入印花价格(单位元)" />
                    </el-form-item>
                    <el-form-item label="磨损值" prop="commodityStickersAbrade">
                      <el-input v-model="formData.commodityStickersAbrade" placeholder="请输入磨损值" />
                    </el-form-item>
                    <el-form-item label="多普勒属性分类名称" prop="commodityDopplerTitle">
                      <el-input v-model="formData.commodityDopplerTitle" placeholder="请输入多普勒属性分类名称" />
                    </el-form-item>
                    <el-form-item label="多普勒属性分类缩写" prop="commodityDopplerAbbrTitle">
                      <el-input v-model="formData.commodityDopplerAbbrTitle" placeholder="请输入多普勒属性分类缩写" />
                    </el-form-item>
                    <el-form-item label="多普勒属性显示颜色" prop="commodityDopplerColor">
                      <el-input v-model="formData.commodityDopplerColor" placeholder="请输入多普勒属性显示颜色" />
                    </el-form-item>
                    <el-form-item label="渐变色属性属性名称" prop="commodityFadeTitle">
                      <el-input v-model="formData.commodityFadeTitle" placeholder="请输入渐变色属性属性名称" />
                    </el-form-item>
                    <el-form-item label="渐变色属性对应数值" prop="commodityFadeNumerialValue">
                      <el-input v-model="formData.commodityFadeNumerialValue" placeholder="请输入渐变色属性对应数值" />
                    </el-form-item>
                    <el-form-item label="渐变色属性显示颜色" prop="commodityFadeColor">
                      <el-input v-model="formData.commodityFadeColor" placeholder="请输入渐变色属性显示颜色" />
                    </el-form-item>
                    <el-form-item label="表面淬火属性分类名称" prop="commodityHardenedTitle">
                      <el-input v-model="formData.commodityHardenedTitle" placeholder="请输入表面淬火属性分类名称" />
                    </el-form-item>
                    <el-form-item label="表面淬火属性分类缩写" prop="commodityHardenedAbbrTitle">
                      <el-input v-model="formData.commodityHardenedAbbrTitle" placeholder="请输入表面淬火属性分类缩写" />
                    </el-form-item>
                    <el-form-item label="表面淬火属性显示颜色" prop="commodityHardenedColor">
                      <el-input v-model="formData.commodityHardenedColor" placeholder="请输入表面淬火属性显示颜色" />
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
  import * as YouyouGoodslistApi from '@/api/steam/youyougoodslist';
      export default {
    name: "YouyouGoodslistForm",
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
                            templateId: undefined,
                            commodityName: undefined,
                            commodityPrice: undefined,
                            commodityAbrade: undefined,
                            commodityPaintSeed: undefined,
                            commodityPaintIndex: undefined,
                            commodityHaveNameTag: undefined,
                            commodityHaveBuzhang: undefined,
                            commodityHaveSticker: undefined,
                            shippingMode: undefined,
                            templateisFade: undefined,
                            templateisHardened: undefined,
                            templateisDoppler: undefined,
                            commodityStickersStickerId: undefined,
                            commodityStickersRawIndex: undefined,
                            commodityStickersName: undefined,
                            commodityStickersHashName: undefined,
                            commodityStickersMaterial: undefined,
                            commodityStickersImgUrl: undefined,
                            commodityStickersPrice: undefined,
                            commodityStickersAbrade: undefined,
                            commodityDopplerTitle: undefined,
                            commodityDopplerAbbrTitle: undefined,
                            commodityDopplerColor: undefined,
                            commodityFadeTitle: undefined,
                            commodityFadeNumerialValue: undefined,
                            commodityFadeColor: undefined,
                            commodityHardenedTitle: undefined,
                            commodityHardenedAbbrTitle: undefined,
                            commodityHardenedColor: undefined,
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
            const res = await YouyouGoodslistApi.getYouyouGoodslist(id);
            this.formData = res.data;
            this.title = "修改查询商品列";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增查询商品列";
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
            await YouyouGoodslistApi.updateYouyouGoodslist(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await YouyouGoodslistApi.createYouyouGoodslist(data);
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
                            templateId: undefined,
                            commodityName: undefined,
                            commodityPrice: undefined,
                            commodityAbrade: undefined,
                            commodityPaintSeed: undefined,
                            commodityPaintIndex: undefined,
                            commodityHaveNameTag: undefined,
                            commodityHaveBuzhang: undefined,
                            commodityHaveSticker: undefined,
                            shippingMode: undefined,
                            templateisFade: undefined,
                            templateisHardened: undefined,
                            templateisDoppler: undefined,
                            commodityStickersStickerId: undefined,
                            commodityStickersRawIndex: undefined,
                            commodityStickersName: undefined,
                            commodityStickersHashName: undefined,
                            commodityStickersMaterial: undefined,
                            commodityStickersImgUrl: undefined,
                            commodityStickersPrice: undefined,
                            commodityStickersAbrade: undefined,
                            commodityDopplerTitle: undefined,
                            commodityDopplerAbbrTitle: undefined,
                            commodityDopplerColor: undefined,
                            commodityFadeTitle: undefined,
                            commodityFadeNumerialValue: undefined,
                            commodityFadeColor: undefined,
                            commodityHardenedTitle: undefined,
                            commodityHardenedAbbrTitle: undefined,
                            commodityHardenedColor: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>