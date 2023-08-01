package cn.iocoder.yudao.module.radar.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by l09655 on 2022/7/28.
 * WebSocket请求消息体封装实体类
 */
@Data
@Getter
@Setter
public class WebsocketRequest {

    @JSONField(name = "RequestURL")
    private String requestURL;

    @JSONField(name = "Method")
    private String method;

    @JSONField(name = "Cseq")
    private long cseq;

    @JSONField(name = "Data")
    private Object data;

}
