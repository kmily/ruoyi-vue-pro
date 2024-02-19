package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamMaFile {

    @JsonProperty("shared_secret")
    private String sharedSecret;
    @JsonProperty("serial_number")
    private String serialNumber;
    @JsonProperty("revocation_code")
    private String revocationCode;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("server_time")
    private Integer serverTime;
    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("token_gid")
    private String tokenGid;
    @JsonProperty("identity_secret")
    private String identitySecret;
    @JsonProperty("secret_1")
    private String secret1;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("fully_enrolled")
    private Boolean fullyEnrolled;
    @JsonProperty("Session")
    private SessionDTO Session;

    public String getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getRevocationCode() {
        return revocationCode;
    }

    public void setRevocationCode(String revocationCode) {
        this.revocationCode = revocationCode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getServerTime() {
        return serverTime;
    }

    public void setServerTime(Integer serverTime) {
        this.serverTime = serverTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTokenGid() {
        return tokenGid;
    }

    public void setTokenGid(String tokenGid) {
        this.tokenGid = tokenGid;
    }

    public String getIdentitySecret() {
        return identitySecret;
    }

    public void setIdentitySecret(String identitySecret) {
        this.identitySecret = identitySecret;
    }

    public String getSecret1() {
        return secret1;
    }

    public void setSecret1(String secret1) {
        this.secret1 = secret1;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean isFullyEnrolled() {
        return fullyEnrolled;
    }

    public void setFullyEnrolled(Boolean fullyEnrolled) {
        this.fullyEnrolled = fullyEnrolled;
    }

    public SessionDTO getSession() {
        return Session;
    }

    public void setSession(SessionDTO Session) {
        this.Session = Session;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SessionDTO {
        @JsonProperty("SteamID")
        private Long SteamID;
        @JsonProperty("AccessToken")
        private String AccessToken;
        @JsonProperty("RefreshToken")
        private String RefreshToken;
        @JsonProperty("SessionID")
        private Object SessionID;

        public Long getSteamID() {
            return SteamID;
        }

        public void setSteamID(Long SteamID) {
            this.SteamID = SteamID;
        }

        public String getAccessToken() {
            return AccessToken;
        }

        public void setAccessToken(String AccessToken) {
            this.AccessToken = AccessToken;
        }

        public String getRefreshToken() {
            return RefreshToken;
        }

        public void setRefreshToken(String RefreshToken) {
            this.RefreshToken = RefreshToken;
        }

        public Object getSessionID() {
            return SessionID;
        }

        public void setSessionID(Object SessionID) {
            this.SessionID = SessionID;
        }
    }
}
