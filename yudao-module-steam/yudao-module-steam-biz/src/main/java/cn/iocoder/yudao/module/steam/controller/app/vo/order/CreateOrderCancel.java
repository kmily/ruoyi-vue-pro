package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderCancel {
    /**
     * 取消结果：
     *  1、取消成功；
     *  2、处理中；
     *  3、取消失败。
     */
    @JsonProperty("result")
    private Integer result;
}
