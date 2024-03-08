package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 指定商品购买入参
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCancelVo implements Serializable {
    /**
     * 订单号
     */
    @JsonProperty("orderNo")
    @NotNull(message = "订单号不能为空")
    private String orderNo;
}
