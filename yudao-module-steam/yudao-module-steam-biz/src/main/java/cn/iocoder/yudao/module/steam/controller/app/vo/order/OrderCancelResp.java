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
public class OrderCancelResp implements Serializable {
    /**
     * 取消结果：
     * 1、取消成功；
     * 2、处理中；
     * 3、取消失败。
     */
    @JsonProperty("result")
    private Integer result;
}
