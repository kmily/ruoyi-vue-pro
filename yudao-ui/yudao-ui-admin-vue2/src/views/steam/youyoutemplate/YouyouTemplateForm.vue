<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="武器全称" prop="name">
                      <el-input v-model="formData.name" placeholder="请输入武器全称" />
                    </el-form-item>
                    <el-form-item label="武器英文全称" prop="hashName">
                      <el-input v-model="formData.hashName" placeholder="请输入武器英文全称" />
                    </el-form-item>
                    <el-form-item label="类型编号" prop="typeId">
                      <el-input v-model="formData.typeId" placeholder="请输入类型编号" />
                    </el-form-item>
                    <el-form-item label="类型名称" prop="typeName">
                      <el-input v-model="formData.typeName" placeholder="请输入类型名称" />
                    </el-form-item>
                    <el-form-item label="类型英文名称" prop="typeHashName">
                      <el-input v-model="formData.typeHashName" placeholder="请输入类型英文名称" />
                    </el-form-item>
                    <el-form-item label="武器编号" prop="weaponId">
                      <el-input v-model="formData.weaponId" placeholder="请输入武器编号" />
                    </el-form-item>
                    <el-form-item label="武器名称" prop="weaponName">
                      <el-input v-model="formData.weaponName" placeholder="请输入武器名称" />
                    </el-form-item>
                    <el-form-item label="武器英文名称" prop="weaponHashName">
                      <el-input v-model="formData.weaponHashName" placeholder="请输入武器英文名称" />
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
  import * as YouyouTemplateApi from '@/api/steam/youyoutemplate';
      export default {
    name: "YouyouTemplateForm",
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
                            name: undefined,
                            hashName: undefined,
                            typeId: undefined,
                            typeName: undefined,
                            typeHashName: undefined,
                            weaponId: undefined,
                            weaponName: undefined,
                            weaponHashName: undefined,
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
            const res = await YouyouTemplateApi.getYouyouTemplate(id);
            this.formData = res.data;
            this.title = "修改悠悠商品数据";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增悠悠商品数据";
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
            await YouyouTemplateApi.updateYouyouTemplate(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await YouyouTemplateApi.createYouyouTemplate(data);
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
                            hashName: undefined,
                            typeId: undefined,
                            typeName: undefined,
                            typeHashName: undefined,
                            weaponId: undefined,
                            weaponName: undefined,
                            weaponHashName: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>
