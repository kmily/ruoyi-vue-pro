package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoginValue {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("requires_twofactor")
    private Boolean requiresTwofactor;
    @JsonProperty("emailauth_needed")
    private Boolean emailAuthNeeded;
    @JsonProperty("emailsteamid")
    private Boolean emailsTeamId;
    @JsonProperty("message")
    private String message;
    @JsonProperty("clear_password_field")
    private String clearPasswordField;
    @JsonProperty("captcha_needed")
    private String captchaNeeded;
    @JsonProperty("captcha_gid")
    private String captchaGid;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean isRequiresTwofactor() {
        return requiresTwofactor;
    }

    public void setRequiresTwofactor(Boolean requiresTwofactor) {
        this.requiresTwofactor = requiresTwofactor;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Boolean getRequiresTwofactor() {
        return requiresTwofactor;
    }

    public Boolean getEmailAuthNeeded() {
        return emailAuthNeeded;
    }

    public void setEmailAuthNeeded(Boolean emailAuthNeeded) {
        this.emailAuthNeeded = emailAuthNeeded;
    }

    public Boolean getEmailsTeamId() {
        return emailsTeamId;
    }

    public void setEmailsTeamId(Boolean emailsTeamId) {
        this.emailsTeamId = emailsTeamId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClearPasswordField() {
        return clearPasswordField;
    }

    public void setClearPasswordField(String clearPasswordField) {
        this.clearPasswordField = clearPasswordField;
    }

    public String getCaptchaNeeded() {
        return captchaNeeded;
    }

    public void setCaptchaNeeded(String captchaNeeded) {
        this.captchaNeeded = captchaNeeded;
    }

    public String getCaptchaGid() {
        return captchaGid;
    }

    public void setCaptchaGid(String captchaGid) {
        this.captchaGid = captchaGid;
    }
}
