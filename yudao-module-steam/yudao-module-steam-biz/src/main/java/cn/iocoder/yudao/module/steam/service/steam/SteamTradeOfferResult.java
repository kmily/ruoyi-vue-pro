package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamTradeOfferResult {

    @JsonProperty("tradeofferid")
    private String tradeofferid;
    @JsonProperty("needs_mobile_confirmation")
    private Boolean needsMobileConfirmation;
    @JsonProperty("needs_email_confirmation")
    private Boolean needsEmailConfirmation;
    @JsonProperty("email_domain")
    private String emailDomain;

    public String getTradeofferid() {
        return tradeofferid;
    }

    public void setTradeofferid(String tradeofferid) {
        this.tradeofferid = tradeofferid;
    }

    public Boolean isNeedsMobileConfirmation() {
        return needsMobileConfirmation;
    }

    public void setNeedsMobileConfirmation(Boolean needsMobileConfirmation) {
        this.needsMobileConfirmation = needsMobileConfirmation;
    }

    public Boolean isNeedsEmailConfirmation() {
        return needsEmailConfirmation;
    }

    public void setNeedsEmailConfirmation(Boolean needsEmailConfirmation) {
        this.needsEmailConfirmation = needsEmailConfirmation;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }
}
