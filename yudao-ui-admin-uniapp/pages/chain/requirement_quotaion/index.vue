<template>
  <view>
    <scroll-view scroll-y="true" class="scroll-Y"
                 @scrolltolower="loadMore"
    >

      <view class="uni-col" v-for="(quote, index) in quoteList" :key="index">
        <uni-card class="uni-card">
          <view class="uni-col card-header">
            <view class="uni-col-6">
              <text class="title">需求:</text>
              <text class="content">{{ quote.materialName }}</text>
            </view>
            <view class="uni-col-6">
              <text class="title">第{{ quote.quotationCount }}轮报价</text>
            </view>
          </view>
          <view class="card-body">
            <uni-row>
              <uni-col :span="8">
                <text class="title">付款方式:</text>
              </uni-col>
              <uni-col :span="12">
                <text class="content">{{ quote.quotationType }}</text>
              </uni-col>
            </uni-row>
            <uni-row>
              <uni-col :span="8">
                <text class="title">单价:</text>
              </uni-col>
              <uni-col :span="12">
                <text class="content">{{ quote.evaluatePrice }}元</text>
              </uni-col>
            </uni-row>
            <uni-row>
              <uni-col :span="8">
                <text class="title">数量:</text>
              </uni-col>
              <uni-col :span="12">
                <text class="content">{{ quote.priceUnitQty }}kg</text>
              </uni-col>
            </uni-row>
            <uni-row>
              <uni-col :span="8">
                <text class="title">税率:</text>
              </uni-col>
              <uni-col :span="12">
                <text class="content"> {{ quote.taxRate }}%</text>
              </uni-col>
            </uni-row>
            <uni-row>
              <uni-col :span="8">
                <text class="title">报价期限:</text>
              </uni-col>
              <uni-col :span="12">
                <text class="content">{{ formatTime(quote.quotationStartTime) }} - {{ formatTime(quote.quotationEndTime) }}</text>
              </uni-col>
            </uni-row>
            <uni-row>
              <uni-col :span="8">
                <text class="title">报价状态:</text>
              </uni-col>
              <uni-col :span="12">
                <text class="content">{{ quotationStatusDesc(quote.quotationStatus) }}</text>
              </uni-col>
            </uni-row>
            <view class="uni-row button-container" v-if="quote.quotationStatus === 0">

              <button  class="uni-col-6" type="button" @click="quoteClick(quote)">报价</button>
<!--              <button v-if="quote.quotationStatus === 0" class="uni-col-6" type="button" @click="cancelClick(quote)">取消</button>-->
            </view>
          </view>
        </uni-card>
      </view>

    </scroll-view>
  </view>
</template>
<script>
import {getRequirementsQuotationPage} from "../../../api/chain/requirement_quotaion";
import moment from 'moment';

export default {
  data() {
    return {
      quoteList: [], //报价列表数据
      total:0, //总条数
      isTriggered: false, //下拉刷新状态
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        purRequirements: null,
        supplierId: null,
        quotationCount: null,
        createTime: [],
        materialName: null,
        supplierName: null,
        taxRate: null,
        evaluatePrice: null,
        priceUnitQty: null,
        taxPrice: null,
        quotationStartTime: null,
        quotationEndTime: null,
        quotationStatus: null
      }

    };
  },
  onPullDownRefresh() {
    // 下拉刷新
    this.queryParams.pageNo = 1

    this.quoteList = []
    this.getQuoteList();
    uni.stopPullDownRefresh();
  },

  onLoad(){
    this.quoteList=[]
    this.getQuoteList();
  },
  methods: {
    async getQuoteList() {
      getRequirementsQuotationPage(this.queryParams).then(res => {
        console.log(res)
        this.total = res.data.total
        this.quoteList.push(...res.data.list)
      })
    },
    goToQuoteDetail(quote) {
      // 实现点击报价跳转到报价详情页
      uni.navigateTo({
        url: `/pages/quoteDetail/quoteDetail?id=${quote.id}`
      });
    },
    quoteClick(quote) {
      // 处理报价
      uni.navigateTo({
        url: `/pages/chain/requirement_quotaion/create?id=${quote.id}`
      });
    },
    cancelClick(id) {
      // 处理删除报价

    },
    // ...
    quotationStatusDesc(status) {
      switch (status) {
        case 0:
          return '等待报价';
        case 1:
          return '已经报价';
        case 2:
          return '已经拒绝';
        default:
          return '';
      }
    },
    onReachBottom() {
      this.queryParams.pageNo++;

      //判斷總條數，是否都刷新出來
      console.log(this.quoteList.length)
      if(this.quoteList.length >= this.total){
        return
      }

      this.getQuoteList();
    },
    formatTime(timestamp){
      // 使用moment.js进行时间格式化和转换
      return moment(timestamp).format('YYYY-MM-DD HH:mm:ss');
    }
  }
};
</script>
<style>
.uni-card {
  margin: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.card-body {
  padding: 10px;
}

.title {
  font-weight: bold;
  width: 100px;

  color: gray;
  margin-bottom: 5px;
}

.content {
  color: black;
}
</style>