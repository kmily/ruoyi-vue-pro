<template>
  <el-button :type="type" plain :icon="icon" size="mini" @click="enableHandler" v-show="finish"
                   v-hasPermi="permissions">{{content}}</el-button>
</template>

<script>
import { updateValueByKey } from "@/api/infra/config";

export default {
  name: 'LogSwitch',
  data() {
      return {
         enable: false,
         finish: false,  
      }
  },
  props: {
    configKey: {
      required: true,
      type: String
    },
    closeText: {
      type: String,
      default: '关闭'
    },
    openText: {
      type: String,
      default: '开启'
    },
    permissions: {
      type: Array,
      default: []
    }
  },
  computed: {
    type() {
      return this.enable?'info':'primary'
    },
    icon() {
      return this.enable?'el-icon-close':'el-icon-check'
    },
    content() {
      return this.enable?this.closeText:this.openText
    },
  },
  created() {
      this.getEnableConfig();
  },
  methods: {
    //开启关闭日志
    enableHandler(){
        let data = new FormData();
        data.append("key",this.configKey);
        data.append("value",this.enable?false:true);
        updateValueByKey(data).then(response => {
           this.msgSuccess(this.content+"成功");
           this.getEnableConfig();
        })
    },
    getEnableConfig(){
        this.getConfigKey(this.configKey).then(response => {
          this.enable = (response.data == "true");
          this.finish = true
        })
    },
  }
}
</script>
