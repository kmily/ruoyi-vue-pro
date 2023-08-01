package cn.iocoder.yudao.module.radar.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by l09655 on 2022/7/27.
 * WebSocket返回消息体封装实体类
 */
@Data
@Getter
@Setter
public class WebsocketResponse {

    @JSONField(name = "ResponseURL")
    private String responseURL;

    @JSONField(name = "ResponseCode")
    private long responseCode;

    @JSONField(name = "ResponseString")
    private String responseString;

    @JSONField(name = "Cseq")
    private long cseq;

    @JSONField(name = "Data")
    private Object data;

}
