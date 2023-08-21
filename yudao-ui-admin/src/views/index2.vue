<template>
  <div style="width: 880px; margin: 0 auto;">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="执行利息" name="first">
        <fx/>
      </el-tab-pane>
      <el-tab-pane label="延迟履行利息" name="second">
        <lx/>
      </el-tab-pane>
      <el-tab-pane label="执行费" name="third">
        <zxf/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import fx from '@/views/biz/calc/fx/index';
import lx from '@/views/biz/calc/lx/index';
import zxf from '@/views/biz/calc/zxf/index';

export default {
  name: 'Index2',
  components: {
    fx,
    lx,
    zxf
  },
  data() {
    return {
      activeName:'first'
    }
  },
  methods: {
    handleClick(tab, event){

    },
  }
}
</script>
<style>
.el-tabs__header.is-top{
  background: #eee;
  padding: 0 20px;
}
</style>