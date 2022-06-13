<template>
  <div>
    <!--所有用户通用 - 通用数据区 (开始)-->
    <el-row class="index-item-bg">
      <el-card class="head-welcome">
        <el-row>
          <el-col :xs="24" :md="16" class="welcome-avatar">
            <div>
              <el-avatar :src="avatar" :size="80"></el-avatar>
            </div>
            <div class="welcome-avatar-title">
              <p class="welcome-avatar-text">Hi，{{ nickName }}，{{ greeting }}</p>
              <p class="welcome-avatar-weather">
                <!-- TODO @芋艿 想在这里加上天气，不知道用什么方法-->
                <!-- <i class="el-icon-cloudy"></i>-->
<!--                {{ weather }}-->
                 今日多云转阴，18℃ - 22℃，出门记得穿外套哦~
              </p>
            </div>
          </el-col>

          <el-col :xs="24" :md="8" class="">
            <!-- TODO @芋艿 这里打算吧系统公告和通知加上去【想改进一下做个消息盒子】  -->
            <el-badge class="welcome-button" :value="1">
              <el-tooltip class="item" effect="dark" content="系统公告" placement="top">
                <el-button circle type="primary" icon="el-icon-s-cooperation"></el-button>
              </el-tooltip>
            </el-badge>
            <el-badge class="welcome-button" :value="1">
              <el-tooltip class="item" effect="dark" content="系统通知" placement="top">
                <el-button circle type="primary" icon="el-icon-bell"></el-button>
              </el-tooltip>
            </el-badge>
          </el-col>

        </el-row>
      </el-card>


    </el-row>

    <!--所有用户通用 - 通用数据区 (结束)-->


    <!--待办事项区域(开始)-->
    <div class="index-item-bg">
      <el-row :gutter="20" class="matter-body">
        <el-col :xs="24" :md="12">
          <el-card class="matter-body-card">
            <div slot="header" class="clearfix">
              <span>我的事项</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="toOpenTagsMy" v-hasPermi="['bpm:process-instance:query']">查看详情</el-button>
            </div>
            <MyFlowable></MyFlowable>

          </el-card>
        </el-col>
        <el-col :xs="24" :md="12">
          <el-card class="matter-body-card">
            <div slot="header" class="clearfix">
              <span>我的待办</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="toOpenTagsToDo" v-hasPermi="['bpm:task:query']">查看详情</el-button>
            </div>
            <ToDoFlowable></ToDoFlowable>
          </el-card>
        </el-col>


      </el-row>
    </div>

    <!--待办事项区域(结束)-->


    <!--专属角色区域 (开始)-->
    <el-row class="index-item-bg">
<!--      TODO 这里应该写每个不同的角色， 拥有什么样的快捷功能 -->

<!--      <div v-hasRole="['super_admin']">-->
<!--        <el-button >管理员才能看到</el-button>-->
<!--      </div>-->

    </el-row>
    <!--专属角色区域 (结束)-->
  </div>
</template>
<script>

import MyFlowable from "@/views/bpm/template/MyFlowable"
import ToDoFlowable from "@/views/bpm/template/ToDoFlowable"
import {getGreeting} from "@/api/indexApi";

export default {
  components: {
    MyFlowable, ToDoFlowable
  },
  data() {
    return {
      avatar: this.$store.getters.avatar,
      nickName: this.$store.getters.nickname,
      weather: "",
      greeting: ""
    }
  },
  created() {
    this.greeting = getGreeting();
  },
  methods: {
    // 我的事项 - 详情
    toOpenTagsMy() {
      this.$tab.openPage("我的流程", "/bpm/task/my");

    },
    // 我的待办 - 详情
    toOpenTagsToDo() {
      this.$tab.openPage("待办任务", "/bpm/task/todo");

    }
  }
}
</script>
<style lang="scss" scoped>
.index-item-bg {
  //border: red solid 1px;
  //height: 200px;
  margin: 25px;
}

.head-welcome {
  padding-top: 10px;

  .welcome-avatar {
    display: flex;


    .welcome-avatar-title {
      padding-left: 15px;
      padding-top: 10px;

      p {
        padding: 0;
        margin: 0;
      }

      .welcome-avatar-text {
        //padding-top: 5px;
        font-size: 25px;
      }

      .welcome-avatar-weather {
        padding-top: 5px;
        font-size: 15px;
        color: #757575;
      }

    }
  }

  .welcome-button {
    float: right;
    margin: 20px 12px 12px;
  }
}

.matter-body {
  .matter-body-card {
    height: 500px;
  }

}



</style>
