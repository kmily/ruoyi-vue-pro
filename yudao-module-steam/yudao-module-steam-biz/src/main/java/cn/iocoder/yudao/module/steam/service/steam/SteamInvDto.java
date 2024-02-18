package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  SteamInvDto
 *  steam用户库存储表
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamInvDto {

    // steam用户id
    @JsonProperty("appid")
    private Integer appid;

    // 上下文id
    @JsonProperty("contextid")
    private String contextid;

    // 资产id
    @JsonProperty("assetid")
    private String assetid;

    // 类id
    @JsonProperty("classid")
    private String classid;

    // 实例id
    @JsonProperty("instanceid")
    private String instanceid;

    // 数量
    @JsonProperty("amount")
    private String amount;

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getContextid() {
        return contextid;
    }

    public void setContextid(String contextid) {
        this.contextid = contextid;
    }

    public String getAssetid() {
        return assetid;
    }

    public void setAssetid(String assetid) {
        this.assetid = assetid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
