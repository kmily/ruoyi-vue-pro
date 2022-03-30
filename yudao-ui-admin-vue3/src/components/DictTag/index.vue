<template>
  <span>
    <template v-for="(dict, index) in getDictDatas2(props.type, props.value)">
      <!-- 默认样式 -->
      <span v-if="dict.colorType === 'default' || dict.colorType === '' || dict.colorType === undefined" :key="dict.value" :index="index"
            :class="dict.cssClass">{{ dict.label }}</span>
      <!-- Tag 样式 -->
      <el-tag v-else :disable-transitions="true" :key="dict.value" :index="index" :type="dict.colorType === 'primary' ? '' : dict.colorType" :class="dict.cssClass">
        {{ dict.label }}
      </el-tag>
    </template>
  </span>
</template>

<script setup>
import {getDictDatas2} from "@/utils/dict"

const props = defineProps({
  type: String,
  value: [Number, String, Boolean, Array],
})
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
