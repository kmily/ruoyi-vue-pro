package cn.iocoder.yudao.module.steam.service.steam;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@TableName(value = "steam_bind_user",autoResultMap = true)
@KeySequence("steam_bind_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamMaFile implements Serializable {

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
