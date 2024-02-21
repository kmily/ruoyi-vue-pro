<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="appid" prop="appid">
                      <el-input v-model="formData.appid" placeholder="请输入appid" />
                    </el-form-item>
                    <el-form-item label="assetid" prop="assetid">
                      <el-input v-model="formData.assetid" placeholder="请输入assetid" />
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
                    <el-form-item label="steamId" prop="steamId">
                      <el-input v-model="formData.steamId" placeholder="请输入steamId" />
                    </el-form-item>
                    <el-form-item label="启用" prop="status">
                      <el-radio-group v-model="formData.status">
                            <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.INFRA_BOOLEAN_STRING)"
                                      :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="出售价格单价分" prop="price">
                      <el-input v-model="formData.price" placeholder="请输入出售价格单价分" />
                    </el-form-item>
                    <el-form-item label="发货状态" prop="transferStatus">
                      <el-radio-group v-model="formData.transferStatus">
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
  import * as InvApi from '@/api/steam/inv';
      export default {
    name: "InvForm",
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
                            steamId: undefined,
                            status: undefined,
                            price: undefined,
                            transferStatus: undefined,
        },
        // 表单校验
        formRules: {
                        steamId: [{ required: true, message: 'steamId不能为空', trigger: 'blur' }],
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
            const res = await InvApi.getInv(id);
            this.formData = res.data;
            this.title = "修改用户库存储";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增用户库存储";
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
            await InvApi.updateInv(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await InvApi.createInv(data);
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
                            steamId: undefined,
                            status: undefined,
                            price: undefined,
                            transferStatus: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
