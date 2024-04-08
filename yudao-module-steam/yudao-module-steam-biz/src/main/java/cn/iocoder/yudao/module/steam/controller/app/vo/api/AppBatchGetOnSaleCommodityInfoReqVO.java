package cn.iocoder.yudao.module.steam.controller.app.vo.api;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ExcelIgnoreUnannotated
public class AppBatchGetOnSaleCommodityInfoReqVO implements Serializable {

    @JsonProperty("requestList")
    private List<RequestListDTO> requestList;

    public List<RequestListDTO> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestListDTO> requestList) {
        this.requestList = requestList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestListDTO {
        @JsonProperty("marketHashName")
        private String marketHashName;

        public String getMarketHashName() {
            return marketHashName;
        }

        public void setMarketHashName(String marketHashName) {
            this.marketHashName = marketHashName;
        }
    }
}