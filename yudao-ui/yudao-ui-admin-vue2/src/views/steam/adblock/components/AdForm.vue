<template>
  <div class="app-container">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        v-loading="formLoading"
        label-width="0px"
        :inline-message="true"
      >
        <el-table :data="formData" class="-mt-10px">
          <el-table-column label="序号" type="index" width="100" />
                      <el-table-column label="文字描述" min-width="150">
                        <template v-slot="{ row, $index }">
                          <el-form-item :prop="`${$index}.text`" :rules="formRules.text" class="mb-0px!">
                            <el-input v-model="row.text" placeholder="请输入文字描述" />
                          </el-form-item>
                        </template>
                      </el-table-column>
                      <el-table-column label="跳转地址" min-width="150">
                        <template v-slot="{ row, $index }">
                          <el-form-item :prop="`${$index}.url`" :rules="formRules.url" class="mb-0px!">
                            <el-input v-model="row.url" placeholder="请输入跳转地址" />
                          </el-form-item>
                        </template>
                      </el-table-column>
                      <el-table-column label="图片地址" min-width="150">
                        <template v-slot="{ row, $index }">
                          <el-form-item :prop="`${$index}.imageUrl`" :rules="formRules.imageUrl" class="mb-0px!">
                            <el-input v-model="row.imageUrl" placeholder="请输入图片地址" />
                          </el-form-item>
                        </template>
                      </el-table-column>
                      <el-table-column label="排序" min-width="150">
                        <template v-slot="{ row, $index }">
                          <el-form-item :prop="`${$index}.sort`" :rules="formRules.sort" class="mb-0px!">
                            <el-input v-model="row.sort" placeholder="请输入排序" />
                          </el-form-item>
                        </template>
                      </el-table-column>
                      <el-table-column label="是否启用" min-width="150">
                        <template v-slot="{ row, $index }">
                          <el-form-item :prop="`${$index}.status`" :rules="formRules.status" class="mb-0px!">
                            <el-radio-group v-model="row.status">
                                  <el-radio v-for="dict in datas"
                                            :key="dict.value" :label="parseInt(dict.value)"
>{{dict.label}}</el-radio>
                            </el-radio-group>
                          </el-form-item>
                          
                        </template>
                      </el-table-column>
                       <el-table-column label="广告名称" min-width="150">
                        <template v-slot="{ row, $index }">
                          <el-form-item :prop="`${$index}.adName`" :rules="formRules.adName" class="mb-0px!">
                            <el-input v-model="row.adName" placeholder="请输入广告名称" />
                          </el-form-item>
                        </template>
                      </el-table-column>
          <el-table-column align="center" fixed="right" label="操作" width="60">
            <template v-slot="{ $index }">
              <el-link @click="handleDelete($index)">—</el-link>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <el-row justify="center" class="mt-3">
        <el-button @click="handleAdd" round>+ 添加广告</el-button>
      </el-row>
  </div>
</template>

<script>
  import * as AdBlockApi from '@/api/steam/adblock';
  export default {
    name: "AdForm",
    components: {
    },
    props:[
      'blockId',
      'datas'
    ],// adID（主表的关联字段）
    data() {
      return {
        // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
        formLoading: false,
        // 表单参数
        formData: [],
        // 表单校验
        formRules: {
        },
      };
    },
    watch:{/** 监听主表的关联字段的变化，加载对应的子表数据 */
      blockId:{
        handler(val) {
          // 1. 重置表单
              this.formData = []
          // 2. val 非空，则加载数据
          if (!val) {
            return;
          }
          try {
            this.formLoading = true;
            // 这里还是需要获取一下 this 的不然取不到 formData
            const that = this;
            AdBlockApi.getAdListByBlockId(val).then(function (res){
              that.formData = res.data;
            })
          } finally {
            this.formLoading = false;
          }
        },
        immediate: true
      }
    },
    methods: {
          /** 新增按钮操作 */
          handleAdd() {
            const row = {
                                id: undefined,
                                text: undefined,
                                url: undefined,
                                imageUrl: undefined,
                                sort: undefined,
                                status: undefined,
                                blockId: undefined,
                                adName: undefined,
            }
            row.blockId = this.blockId;
            this.formData.push(row);
          },
          /** 删除按钮操作 */
          handleDelete(index) {
            this.formData.splice(index, 1);
          },
      /** 表单校验 */
      validate(){
        return this.$refs["formRef"].validate();
      },
      /** 表单值 */
      getData(){
        return this.formData;
      }
    }
  };
</script>
