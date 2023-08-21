<template>
  <div class="app-container">
    <el-card v-for="item in familyData" class="family-devices" :key="item.id">
      <div slot="header" class="clearfix">
        <span class="family">{{item.name}}</span>
      </div>
      <div v-if="item.deviceUserVOS && item.deviceUserVOS.length > 0" class="device-list">
          <el-card shadow="hover" v-for="item in item.deviceUserVOS" :key="item.id" class="divice-item">
            <div class="pic-online">
              <div class="pic">
                <img :src="radarImg[item.type]"/>
              </div>
              <div v-if="radarStatus[item.deviceId] == 1" class="online">
                在线
              </div>
              <div v-else class="offline">
                离线
              </div>
            </div>
            <div class="room-name">{{item.roomName}}</div>
            <div class="device-name">{{item.deviceName}}</div>
          </el-card>
      </div>
      <div v-else>未绑定设备</div>
    </el-card>
  </div>
</template>

<script>
import {getFamilyList, getFamilyDevicesList} from '@/api/member/family';
import {getDeviceStatus} from '@/api/radar/device';
import Radar1 from '@/assets/images/radar/1.png';
import Radar2 from '@/assets/images/radar/2.png';
import Radar3 from '@/assets/images/radar/3.png';
import Radar4 from '@/assets/images/radar/4.png';

export default {
  name: "Detail",
  components: {
  },
  data() {
    return {
      familyData: [],
      radarImg: [1, Radar1, Radar2, Radar3, Radar4],
      radarStatus: {}
    };
  },
  created() {
    const userId = this.$route.params && this.$route.params.userId;
    console.log('查询用户', userId);
    this.familyList(userId);
  },

  // watch:{
  //   $route(to, from){
  //     if(to.path.startsWith('/member/detail/')){
  //         const userId = this.$route.params && this.$route.params.userId;
  //         console.log('查询用户', userId);
  //         this.familyList(userId);
  //     }
  //   }
  // },

  methods: {
    
    /** 查询用户家庭 */
    familyList(usrId){
      this.loading = true;
      // 执行查询
      getFamilyDevicesList(usrId).then(response => {
        let familyData = response.data || [];

        let deviceIds = [];

        familyData.forEach(item => {
          (item.deviceUserVOS || []).forEach(dev => {
            deviceIds.push(dev.deviceId);
          });
        });
        this.getDeviceStatus(deviceIds, familyData);
      });
    },
    
    // 查询设备状态
    getDeviceStatus(ids, familyData){
      getDeviceStatus(ids).then(response => {
        let data = response.data || [];
        data.forEach(item => {
          this.radarStatus[item.deviceId] = item.status;
        });

        this.familyData = familyData;
        this.loading = false;
      });;
    }

  }
};
</script>
<style scoped lang="scss">
//

.family-devices{
  margin-top: 10px;
  .family{
    font-size: 16px;
    font-weight: 600;
  }

  .device-list{
    display: flex;
    flex-flow: wrap;
    justify-content: flex-start;
    flex-wrap: wrap;
    .divice-item{
      margin-left: 10px;
      .pic-online{
        display: flex;
        justify-content: space-between;
        align-items: center;
        .online{
          color: #0079FF;
        }
        .offline{
          color: #BBBBBB;
        }
      }
      .room-name{
        padding: 10px 0;
        color: #BBBBBB;
      }
      .device-name{
        color: #101010;
        font-weight: 600;
      }
    }
  }

}

</style>