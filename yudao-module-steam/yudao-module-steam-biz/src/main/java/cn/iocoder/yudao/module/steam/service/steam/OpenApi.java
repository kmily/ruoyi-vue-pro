package cn.iocoder.yudao.module.steam.service.steam;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * steam openapi接口数据
 * @author glzaboy
 */
@Data
@ToString
public class OpenApi {
    @JsonProperty("openid.ns")
    private String ns;
    @JsonProperty("openid.mode")
    private String mode;
    @JsonProperty("openid.op_endpoint")
    private String opEndpoint;
    @JsonProperty("openid.claimed_id")
    private String claimedId;
    @JsonProperty("openid.identity")
    private String identity;
    @JsonProperty("openid.return_to")
    private String returnTo;
    @JsonProperty("openid.response_nonce")
    private String responseNonce;
    @JsonProperty("openid.assoc_handle")
    private String assocHandle;
    @JsonProperty("openid.signed")
    private String signed;
    @JsonProperty("openid.sig")
    private String sig;
}
