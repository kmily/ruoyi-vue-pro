package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateReqOrderStatus implements Serializable {
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
}
