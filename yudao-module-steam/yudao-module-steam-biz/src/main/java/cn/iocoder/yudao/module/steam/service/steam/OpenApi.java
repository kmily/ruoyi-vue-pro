package cn.iocoder.yudao.module.steam.service.steam;


import lombok.Data;

/**
 * steam openapi接口数据
 * @author glzaboy
 */
@Data
public class OpenApi {
    private String ns;
    private String mode;
    private String opEndpoint;
    private String claimedId;
    private String identity;
    private String returnTo;
    private String responseNonce;
    private String assocHandle;
    private String signed;
    private String sig;
}
