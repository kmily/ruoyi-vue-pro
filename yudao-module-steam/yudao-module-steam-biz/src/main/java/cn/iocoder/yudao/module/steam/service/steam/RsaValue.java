package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RsaValue {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("publickey_mod")
    private String publickeyMod;
    @JsonProperty("publickey_exp")
    private String publickeyExp;
    @JsonProperty("timestamp")
    private String timestamp;
}
