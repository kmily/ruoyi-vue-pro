<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="csgoid" prop="appid">
                      <el-input v-model="formData.appid" placeholder="请输入csgoid" />
                    </el-form-item>
                    <el-form-item label="资产id(饰品唯一)" prop="assetid">
                      <el-input v-model="formData.assetid" placeholder="请输入资产id(饰品唯一)" />
                    </el-form-item>
                    <el-form-item label="classid" prop="classid">
                      <el-input v-model="formData.classid" placeholder="请输入classid" />
                    </el-form-item>
                    <el-form-item label="instanceid" prop="instanceid">
                      <el-input v-model="formData.instanceid" placeholder="请输入instanceid" />
                    </el-form-item>
                    <el-form-item label="amount" prop="amount">
                      <el-input v-model="formData.amount" placeholder="请输入amount" />
                    </el-form-item>
                    <el-form-item label="状态" prop="status">
                      <el-radio-group v-model="formData.status">
                            <el-radio label="1">请选择字典生成</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="出售价格单价分" prop="price">
                      <el-input v-model="formData.price" placeholder="请输入出售价格单价分" />
                    </el-form-item>
                    <el-form-item label="发货状态(0代表未出售，1代表出售中，2代表已出售 )" prop="transferStatus">
                      <el-input v-model="formData.transferStatus" placeholder="请输入发货状态(0代表未出售，1代表出售中，2代表已出售 )" />
                    </el-form-item>
                    <el-form-item label="contextid" prop="contextid">
                      <el-input v-model="formData.contextid" placeholder="请输入contextid" />
                    </el-form-item>
                    <el-form-item label="图片地址" prop="iconUrl">
                      <el-input v-model="formData.iconUrl" placeholder="请输入图片地址" />
                    </el-form-item>
                    <el-form-item label="商品名称" prop="marketName">
                      <el-input v-model="formData.marketName" placeholder="请输入商品名称" />
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
                    <el-form-item label="marketHashName" prop="marketHashName">
                      <el-input v-model="formData.marketHashName" placeholder="请输入marketHashName" />
                    </el-form-item>
                    <el-form-item label="在售展示权重" prop="displayWeight">
                      <el-input v-model="formData.displayWeight" placeholder="请输入在售展示权重" />
                    </el-form-item>
                    <el-form-item label="itemInfo" prop="itemInfo">
                      <el-input v-model="formData.itemInfo" placeholder="请输入itemInfo" />
                    </el-form-item>
                    <el-form-item label="short_name" prop="shortName">
                      <el-input v-model="formData.shortName" placeholder="请输入short_name" />
                    </el-form-item>
                    <el-form-item label="出售用户名字" prop="sellingUserName">
                      <el-input v-model="formData.sellingUserName" placeholder="请输入出售用户名字" />
                    </el-form-item>
                    <el-form-item label="出售用户头像" prop="sellingAvator">
                      <el-input v-model="formData.sellingAvator" placeholder="请输入出售用户头像" />
                    </el-form-item>
                    <el-form-item label="出售用户id" prop="sellingUserId">
                      <el-input v-model="formData.sellingUserId" placeholder="请输入出售用户id" />
                    </el-form-item>
                    <el-form-item label="出售平台id" prop="platformIdentity">
                      <el-input v-model="formData.platformIdentity" placeholder="请输入出售平台id" />
                    </el-form-item>
                    <el-form-item label="steamId" prop="steamId">
                      <el-input v-model="formData.steamId" placeholder="请输入steamId" />
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
  import * as OtherSellingApi from '@/api/steam/otherselling';
      export default {
    name: "OtherSellingForm",
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
                            appid: undefined,
                            assetid: undefined,
                            classid: undefined,
                            instanceid: undefined,
                            amount: undefined,
                            status: undefined,
                            price: undefined,
                            transferStatus: undefined,
                            contextid: undefined,
                            iconUrl: undefined,
                            marketName: undefined,
                            selQuality: undefined,
                            selItemset: undefined,
                            selWeapon: undefined,
                            selExterior: undefined,
                            selRarity: undefined,
                            selType: undefined,
                            marketHashName: undefined,
                            displayWeight: undefined,
                            itemInfo: undefined,
                            shortName: undefined,
                            sellingUserName: undefined,
                            sellingAvator: undefined,
                            sellingUserId: undefined,
                            platformIdentity: undefined,
                            steamId: undefined,
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
            const res = await OtherSellingApi.getOtherSelling(id);
            this.formData = res.data;
            this.title = "修改其他平台在售";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增其他平台在售";
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
            await OtherSellingApi.updateOtherSelling(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await OtherSellingApi.createOtherSelling(data);
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
                            appid: undefined,
                            assetid: undefined,
                            classid: undefined,
                            instanceid: undefined,
                            amount: undefined,
                            status: undefined,
                            price: undefined,
                            transferStatus: undefined,
                            contextid: undefined,
                            iconUrl: undefined,
                            marketName: undefined,
                            selQuality: undefined,
                            selItemset: undefined,
                            selWeapon: undefined,
                            selExterior: undefined,
                            selRarity: undefined,
                            selType: undefined,
                            marketHashName: undefined,
                            displayWeight: undefined,
                            itemInfo: undefined,
                            shortName: undefined,
                            sellingUserName: undefined,
                            sellingAvator: undefined,
                            sellingUserId: undefined,
                            platformIdentity: undefined,
                            steamId: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>