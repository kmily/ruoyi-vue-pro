package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityList implements Serializable {

//    @JsonProperty("code")
//    private Integer code;
//    @JsonProperty("msg")
//    private String msg;
//    @JsonProperty("timestamp")
//    private Long timestamp;
    @JsonProperty("data")
    private List<ApiUUCommodityDO> data;

}