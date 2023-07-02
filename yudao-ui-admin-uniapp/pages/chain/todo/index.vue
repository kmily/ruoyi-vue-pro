<template>
  <view class="container">
    <uni-card v-for="(task, index) in tasks" :key="index" class="task-card">
      <view class="card-content">
        <view class="task-info">
          <view class="task-item">
            <text class="task-label">任务编号：</text>
            <text class="task-value">{{ task.taskId }}</text>
          </view>
          <view class="task-item">
            <text class="task-label">任务名称：</text>
            <text class="task-value">{{ task.taskName }}</text>
          </view>
          <view class="task-item">
            <text class="task-label">所属流程：</text>
            <text class="task-value">{{ task.processName }}</text>
          </view>
          <view class="task-item">
            <text class="task-label">流程发起人：</text>
            <text class="task-value">{{ task.initiator }}</text>
          </view>
          <view class="task-item">
            <text class="task-label">创建时间：</text>
            <text class="task-value">{{ task.created }}</text>
          </view>
        </view>
        <button class="progress-button" @click="showProgress(index)">审批进度</button>
      </view>
    </uni-card>
  </view>
</template>

<script>
export default {
  data() {
    return {
      tasks: [
        // 在这里添加你的任务数据
        // 例如:
        {
          taskId: '001',
          taskName: '任务1',
          processName: '流程1',
          initiator: '张三',
          created: '2023-06-06'
        }
      ]
    };
  },
  methods: {
    showProgress(index) {
      // 在这里处理你的审批进度逻辑
      console.log('审批进度：', this.tasks[index]);
    }
  }
};
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  padding: 20rpx;
  background-color: #f8f8f8;
}
.task-card {
  margin: 20rpx 0;
  background-color: #ffffff;
  border-radius: 20rpx;
}
.card-content {
  display: flex;
  flex-direction: column;
  padding: 20rpx;
}
.task-info {
  margin-bottom: 20rpx;
}
.task-item {
  display: flex;
  align-items: center;
  margin: 10rpx 0;
}
.task-label {
  color: #666666;
  font-size: 28rpx;
}
.task-value {
  color: #333333;
  font-size: 28rpx;
  margin-left: 10rpx;
}
.progress-button {
  display: inline-block;
  color: #ffffff;
  background-color: #0081ff;
  padding: 10rpx 20rpx;
  border-radius: 20rpx;
  text-align: center;
}
</style>