<template>
  <view class="container">
    <uni-section title="报价" type="line" style="padding: 10px">
      <uni-forms label-width="100px" ref="form" :rules="formRules" :modelValue="formData">
        <uni-forms-item label="物料名称" name="materialName">
          <uni-easyinput v-model="formData.materialName" disabled placeholder="请输入物料名称"/>
        </uni-forms-item>
        <uni-forms-item label="供应商名称" name="supplierName">
          <uni-easyinput v-model="formData.supplierName" disabled placeholder="请输入供应商名称"/>
        </uni-forms-item>
        <uni-forms-item label="税率%" name="taxRate">
          <uni-easyinput v-model="formData.taxRate" placeholder="请输入税率%"/>
        </uni-forms-item>
        <uni-forms-item label="单价" name="evaluatePrice">
          <uni-easyinput v-model="formData.evaluatePrice" placeholder="请输入单价"/>
        </uni-forms-item>
        <uni-forms-item label="单位" name="priceUnit">
          <uni-easyinput v-model="formData.priceUnit" disabled/>
        </uni-forms-item>
        <uni-forms-item label="数量" name="priceUnitQty">
          <uni-easyinput v-model="formData.priceUnitQty" placeholder="请输入计价数量"/>
        </uni-forms-item>
        <uni-forms-item label="付款方式" name="quotationType">

          <uni-data-select
              v-model="formData.quotationType"
              :localdata="range"
          ></uni-data-select>
        </uni-forms-item>
      </uni-forms>
      <button type="primary" @click="submitForm('valiForm')">提交</button>

    </uni-section>
  </view>
  <!--    <div>-->
  <!--      总价为：{{ new Decimal(formData.evaluatePrice).times(new Decimal(formData.priceUnitQty)).toDecimalPlaces(3) }}-->
  <!--&lt;!&ndash;    </div>&ndash;&gt;-->
  <!--      <uni-button @click="submitForm" type="primary" :disabled="formLoading">确定</uni-button>-->
  <!--      <uni-button @click="dialogVisible = false">取消</uni-button>-->
</template>

<script>
// import Decimal from 'decimal.js'
import {getRequirementsQuotationDetail, reportRequirementsQuotation} from "../../../api/chain/requirement_quotaion";

export default {
  data() {
    return {
      dialogVisible: false,
      range: [
        {value: 0, text: "电汇"},
        {value: 1, text: "承兑汇票"},
        {value: 2, text: "电汇（50%）汇票（50%)"},
      ],
      dialogTitle: '',
      formLoading: false,
      formType: '',
      formData: {
        id: undefined,
        purRequirements: undefined,
        supplierId: undefined,
        quotationType: undefined,
        quotationCount: undefined,
        materialName: undefined,
        supplierName: undefined,
        taxRate: 13,
        evaluatePrice: 0,
        priceUnitQty: 0,
        taxPrice: undefined
      },
      formRules: {
        quotationType: {
          rules: [
            {required: true, errorMessage: '付款方式不能为空'}

          ]
        },
        priceUnitQty: {
          rules: [
            {required: true, errorMessage: '数量不能为空'},
            {
              validateFunction: function (rule, value, data, callback) {
                const reg = /^\d+(\.\d{1,3})?$/
                if (!reg.test(value)) {
                  callback('保留小数点后三位')
                } else {
                  callback()
                }
              },
            }
          ]
        },
        evaluatePrice: {
          rules: [
            {required: true, errorMessage: '单价不能为空'},
            {
              validateFunction: function (rule, value, data, callback) {
                const reg = /^\d+(\.\d{1,3})?$/
                if (!reg.test(value)) {
                  callback('单价保留小数点后三位')
                } else {
                  callback()
                }
              },
            }
          ]
        },

        // priceUnitQty: [
        //   {
        //     validator: (rule, value, callback) => {
        //       const reg = /^\d+(\.\d{1,3})?$/
        //       if (!reg.test(value)) {
        //         callback(new Error('保留小数点后三位'))
        //       } else {
        //         callback()
        //       }
        //     },
        //     trigger: 'blur'
        //   }
        // ],

      }
    }
  },
  onReady() {
    this.$refs.form.setRules(this.formRules)
  },
  onLoad(option) {
    getRequirementsQuotationDetail(option.id)
        .then(res => {
          this.formData = res.data
          this.formData.taxRate = 13
        })
        .finally(() => {
          this.formLoading = false
        })
  },
  methods: {
    submitForm() {
      this.$refs.form.validate().then(res => {
        reportRequirementsQuotation(this.formData)
            .then(() => {
              this.dialogVisible = false
              uni.showToast({
                title: '提交成功',
                icon: 'none'
              })
              //返回上個頁面
              uni.navigateBack()
            })
            .finally(() => {
              this.formLoading = false
            })
      }).catch(err => {
        console.log('表单错误信息：', err);
      })

      //   if (valid) {
      //     this.formLoading = true
      //     const data = {...this.formData}
      //     data.taxRate = this.taxRate.value
      //     //   reportRequirementsQuotation(data)
      //     //       .then(() => {
      //     //         this.dialogVisible = false
      //     //       })
      //     //       .finally(() => {
      //     //         this.formLoading = false
      //     //       })
      //   }
      // }
      //
    },
    resetForm() {
      this.formData = {
        id: undefined,
        purRequirements: undefined,
        supplierId: undefined,
        quotationType: undefined,
        quotationCount: undefined,
        materialName: undefined,
        supplierName: undefined,
        taxRate: 13,
        evaluatePrice: 0,
        priceUnitQty: 0,
        taxPrice: undefined
      }
      this.$nextTick(() => {
        this.$refs.formRef.resetFields()
      })
    }
  }
}
</script>