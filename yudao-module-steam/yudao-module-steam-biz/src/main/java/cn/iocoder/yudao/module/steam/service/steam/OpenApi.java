package cn.iocoder.yudao.module.steam.service.steam;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * steam openapi接口数据
 * @author glzaboy
 */
@Data
@ToString
public class OpenApi {
    @JsonProperty("openid.ns")
    @NotEmpty(message = "openid.ns不能为空")
    private String ns;
    @JsonProperty("openid.mode")
    @NotEmpty(message = "openid.mode不能为空")
    private String mode;
    @JsonProperty("openid.op_endpoint")
    @NotEmpty(message = "openid.op_endpoint不能为空")
    private String opEndpoint;
    @JsonProperty("openid.claimed_id")
    @NotEmpty(message = "openid.claimed_id不能为空")
    private String claimedId;
    @JsonProperty("openid.identity")
    @NotEmpty(message = "openid.identity不能为空")
    private String identity;
    @JsonProperty("openid.return_to")
    @NotEmpty(message = "openid.return_to不能为空")
    private String returnTo;
    @JsonProperty("openid.response_nonce")
    @NotEmpty(message = "openid.response_nonce不能为空")
    private String responseNonce;
    @JsonProperty("openid.assoc_handle")
    @NotEmpty(message = "openid.assoc_handle不能为空")
    private String assocHandle;
    @JsonProperty("openid.signed")
    @NotEmpty(message = "openid.signed不能为空")
    private String signed;
    @JsonProperty("openid.sig")
    @NotEmpty(message = "openid.sig不能为空")
    private String sig;
}
