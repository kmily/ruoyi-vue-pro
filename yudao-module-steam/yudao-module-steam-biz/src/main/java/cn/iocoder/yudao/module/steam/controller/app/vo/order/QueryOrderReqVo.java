package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryOrderReqVo  extends PageParam implements Serializable {
    /**
     * UU订单号
     */
    @JsonProperty("orderNo")
    private String orderNo;

    /**
     * 商户订单号
     * orderNo 和 merchantNo 二选一
     * 同时传入以 orderNo 为准
     */
    @JsonProperty("merchantNo")
    private String merchantNo;

    /**
     * ID
     */
    @JsonProperty("id")
    private Long id;
}
