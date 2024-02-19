package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 发起交易确认列表
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileConfList {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("conf")
    private List<ConfDTO> conf;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ConfDTO> getConf() {
        return conf;
    }

    public void setConf(List<ConfDTO> conf) {
        this.conf = conf;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConfDTO {
        @JsonProperty("type")
        private Integer type;
        @JsonProperty("type_name")
        private String typeName;
        @JsonProperty("id")
        private String id;
        @JsonProperty("creator_id")
        private String creatorId;
        @JsonProperty("nonce")
        private String nonce;
        @JsonProperty("creation_time")
        private Integer creationTime;
        @JsonProperty("cancel")
        private String cancel;
        @JsonProperty("accept")
        private String accept;
        @JsonProperty("icon")
        private String icon;
        @JsonProperty("multi")
        private Boolean multi;
        @JsonProperty("headline")
        private String headline;
        @JsonProperty("warn")
        private Object warn;
        @JsonProperty("summary")
        private List<String> summary;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public Integer getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Integer creationTime) {
            this.creationTime = creationTime;
        }

        public String getCancel() {
            return cancel;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }

        public String getAccept() {
            return accept;
        }

        public void setAccept(String accept) {
            this.accept = accept;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Boolean isMulti() {
            return multi;
        }

        public void setMulti(Boolean multi) {
            this.multi = multi;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public Object getWarn() {
            return warn;
        }

        public void setWarn(Object warn) {
            this.warn = warn;
        }

        public List<String> getSummary() {
            return summary;
        }

        public void setSummary(List<String> summary) {
            this.summary = summary;
        }
    }
}
