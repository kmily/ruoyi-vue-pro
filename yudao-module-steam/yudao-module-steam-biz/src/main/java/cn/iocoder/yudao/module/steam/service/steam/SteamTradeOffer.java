package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamTradeOffer {

    @JsonProperty("newversion")
    private Boolean newversion;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("me")
    private MeDTO me;
    @JsonProperty("them")
    private ThemDTO them;

    public Boolean isNewversion() {
        return newversion;
    }

    public void setNewversion(Boolean newversion) {
        this.newversion = newversion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public MeDTO getMe() {
        return me;
    }

    public void setMe(MeDTO me) {
        this.me = me;
    }

    public ThemDTO getThem() {
        return them;
    }

    public void setThem(ThemDTO them) {
        this.them = them;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MeDTO {
        @JsonProperty("ready")
        private Boolean ready;
        @JsonProperty("assets")
        private List<AssetsDTO> assets;
        @JsonProperty("currency")
        private List<?> currency;

        public Boolean isReady() {
            return ready;
        }

        public void setReady(Boolean ready) {
            this.ready = ready;
        }

        public List<AssetsDTO> getAssets() {
            return assets;
        }

        public void setAssets(List<AssetsDTO> assets) {
            this.assets = assets;
        }

        public List<?> getCurrency() {
            return currency;
        }

        public void setCurrency(List<?> currency) {
            this.currency = currency;
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AssetsDTO {
            @JsonProperty("appid")
            private Integer appid;
            @JsonProperty("contextid")
            private String contextid;
            @JsonProperty("amount")
            private Integer amount;
            @JsonProperty("assetid")
            private String assetid;

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

            public Integer getAmount() {
                return amount;
            }

            public void setAmount(Integer amount) {
                this.amount = amount;
            }

            public String getAssetid() {
                return assetid;
            }

            public void setAssetid(String assetid) {
                this.assetid = assetid;
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ThemDTO {
        @JsonProperty("ready")
        private Boolean ready;
        @JsonProperty("assets")
        private List<AssetsDTO> assets;
        @JsonProperty("currency")
        private List<?> currency;

        public Boolean isReady() {
            return ready;
        }

        public void setReady(Boolean ready) {
            this.ready = ready;
        }

        public List<AssetsDTO> getAssets() {
            return assets;
        }

        public void setAssets(List<AssetsDTO> assets) {
            this.assets = assets;
        }

        public List<?> getCurrency() {
            return currency;
        }

        public void setCurrency(List<?> currency) {
            this.currency = currency;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AssetsDTO {
            @JsonProperty("appid")
            private Integer appid;
            @JsonProperty("contextid")
            private String contextid;
            @JsonProperty("amount")
            private Integer amount;
            @JsonProperty("assetid")
            private String assetid;

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

            public Integer getAmount() {
                return amount;
            }

            public void setAmount(Integer amount) {
                this.amount = amount;
            }

            public String getAssetid() {
                return assetid;
            }

            public void setAssetid(String assetid) {
                this.assetid = assetid;
            }
        }
    }
}
