package cn.iocoder.yudao.module.steam.service.uu.vo.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class NotifyReq implements Serializable {

    @JsonProperty("messageNo")
    private String messageNo;
    @JsonProperty("callBackInfo")
    private String callBackInfo;
    @JsonProperty("sign")
    private String sign;
}
