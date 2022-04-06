<template>
  <el-scrollbar height="calc(100vh - 84px - 40px)" class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span>基本信息</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
              <tr>
                <td><div class="cell">Redis版本</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.redis_version }}</div></td>
                <td><div class="cell">运行模式</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.redis_mode == "standalone" ? "单机" : "集群" }}</div></td>
                <td><div class="cell">端口</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.tcp_port }}</div></td>
                <td><div class="cell">客户端数</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.connected_clients }}</div></td>
              </tr>
              <tr>
                <td><div class="cell">运行时间(天)</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.uptime_in_days }}</div></td>
                <td><div class="cell">使用内存</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.used_memory_human }}</div></td>
                <td><div class="cell">使用CPU</div></td>
                <td><div class="cell" v-if="cache.info">{{ parseFloat(cache.info.used_cpu_user_children).toFixed(2) }}</div></td>
                <td><div class="cell">内存配置</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.maxmemory_human }}</div></td>
              </tr>
              <tr>
                <td><div class="cell">AOF是否开启</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.aof_enabled == "0" ? "否" : "是" }}</div></td>
                <td><div class="cell">RDB是否成功</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.rdb_last_bgsave_status }}</div></td>
                <td><div class="cell">Key数量</div></td>
                <td><div class="cell" v-if="cache.dbSize">{{ cache.dbSize }} </div></td>
                <td><div class="cell">网络入口/出口</div></td>
                <td><div class="cell" v-if="cache.info">{{ cache.info.instantaneous_input_kbps }}kps/{{cache.info.instantaneous_output_kbps}}kps</div></td>
              </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header"><span>命令统计</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="commandStatsRef" style="height: 420px" />
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>内存信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <div ref="usedmemory" style="height: 420px" />
          </div>
        </el-card>
      </el-col>
    </el-row>


    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <el-table
              v-loading="keyListLoad"
              :data="keyList"
              row-key="id"
          >
            <el-table-column prop="keyTemplate" label="Key 模板" width="200" />
            <el-table-column prop="keyType" label="Key 类型" width="100" />
            <el-table-column prop="valueType" label="Value 类型" />
            <el-table-column prop="timeoutType" label="超时时间" width="200">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.INFRA_REDIS_TIMEOUT_TYPE" :value="scope.row.timeoutType" />
                <span v-if="scope.row.timeout > 0">({{ scope.row.timeout / 1000 }} 秒)</span>
              </template>
            </el-table-column>
            <el-table-column prop="memo" label="备注" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </el-scrollbar>

</template>

<script setup>
import {onMounted, ref} from "vue"
import * as RedisApi from "@/api/infra/redis.js"
import DictTag from "@/components/DictTag"
import {DICT_TYPE} from "@/utils/dict.js"
import {ElMessage} from "element-plus"
import * as echarts from "echarts"

const cache = ref({});
const keyListLoad = ref(true)
const keyList = ref([])

async function readRedisInfo() {
  try {
    let response = await RedisApi.redisMonitorInfo()
    cache.value = response.data
    console.log(cache.value)

    // 加载失败：Cannot set properties of undefined (setting 'commandstats')
    loadEchartOptions(cache.value.commandStats)

    response = await RedisApi.redisKeysInfo()
    keyList.value = response.data
    keyListLoad.value = false //加载完成
  } catch (e) {
    ElMessage.error(`加载失败：${e.message}`)
    console.log(e)
  }
}

/**
 * @type {Ref<HTMLDivElement>}
 */
const commandStatsRef = ref(null)
const commandstats = ref([])

/**
 * @type {Ref<HTMLDivElement>}
 */
const usedmemory = ref(null)

function loadEchartOptions(stats) {
  const commandStats = [];

  stats.forEach(row => {
    commandStats.push({
      name: row.command,
      value: row.calls
    });
  })

  const commandStatsInstance = echarts.init(commandStatsRef.value, "macarons");

  commandStatsInstance.setOption({
    tooltip: {
      trigger: "item",
      formatter: "{a} <br/>{b} : {c} ({d}%)",
    },
    series: [
      {
        name: "命令",
        type: "pie",
        roseType: "radius",
        radius: [15, 95],
        center: ["50%", "38%"],
        data: commandStats,
        animationEasing: "cubicInOut",
        animationDuration: 1000,
      },
    ],
  })

  const usedMemoryInstance = echarts.init(usedmemory.value, "macarons");
  usedMemoryInstance.setOption({
    tooltip: {
      formatter: "{b} <br/>{a} : " + cache.value.info.used_memory_human,
    },
    series: [
      {
        name: "峰值",
        type: "gauge",
        min: 0,
        max: 1000,
        detail: {
          formatter: cache.value.info.used_memory_human,
        },
        data: [
          {
            value: parseFloat(cache.value.info.used_memory_human),
            name: "内存消耗",
          },
        ],
      },
    ],
  })

}

onMounted(() => {
  readRedisInfo()
})

</script>

<style scoped>

</style>