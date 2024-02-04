package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 拉取库存接口
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryDto {

    @JsonProperty("total_inventory_count")
    private Integer totalInventoryCount;
    @JsonProperty("success")
    private Integer success;
    @JsonProperty("rwgrsn")
    private Integer rwgrsn;
    @JsonProperty("assets")
    private List<AssetsDTO> assets;
    @JsonProperty("descriptions")
    private List<DescriptionsDTOX> descriptions;

    public Integer getTotalInventoryCount() {
        return totalInventoryCount;
    }

    public void setTotalInventoryCount(Integer totalInventoryCount) {
        this.totalInventoryCount = totalInventoryCount;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getRwgrsn() {
        return rwgrsn;
    }

    public void setRwgrsn(Integer rwgrsn) {
        this.rwgrsn = rwgrsn;
    }

    public List<AssetsDTO> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsDTO> assets) {
        this.assets = assets;
    }

    public List<DescriptionsDTOX> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<DescriptionsDTOX> descriptions) {
        this.descriptions = descriptions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AssetsDTO {
        @JsonProperty("appid")
        private Integer appid;
        @JsonProperty("contextid")
        private String contextid;
        @JsonProperty("assetid")
        private String assetid;
        @JsonProperty("classid")
        private String classid;
        @JsonProperty("instanceid")
        private String instanceid;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DescriptionsDTOX {
        @JsonProperty("appid")
        private Integer appid;
        @JsonProperty("classid")
        private String classid;
        @JsonProperty("instanceid")
        private String instanceid;
        @JsonProperty("currency")
        private Integer currency;
        @JsonProperty("background_color")
        private String backgroundColor;
        @JsonProperty("icon_url")
        private String iconUrl;
        @JsonProperty("icon_url_large")
        private String iconUrlLarge;
        @JsonProperty("tradable")
        private Integer tradable;
        @JsonProperty("name")
        private String name;
        @JsonProperty("name_color")
        private String nameColor;
        @JsonProperty("type")
        private String type;
        @JsonProperty("market_name")
        private String marketName;
        @JsonProperty("market_hash_name")
        private String marketHashName;
        @JsonProperty("commodity")
        private Integer commodity;
        @JsonProperty("market_tradable_restriction")
        private Integer marketTradableRestriction;
        @JsonProperty("marketable")
        private Integer marketable;
        @JsonProperty("descriptions")
        private List<DescriptionsDTO> descriptions;
        @JsonProperty("actions")
        private List<ActionsDTO> actions;
        @JsonProperty("market_actions")
        private List<MarketActionsDTO> marketActions;
        @JsonProperty("tags")
        private List<TagsDTO> tags;

        public Integer getAppid() {
            return appid;
        }

        public void setAppid(Integer appid) {
            this.appid = appid;
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

        public Integer getCurrency() {
            return currency;
        }

        public void setCurrency(Integer currency) {
            this.currency = currency;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIconUrlLarge() {
            return iconUrlLarge;
        }

        public void setIconUrlLarge(String iconUrlLarge) {
            this.iconUrlLarge = iconUrlLarge;
        }

        public Integer getTradable() {
            return tradable;
        }

        public void setTradable(Integer tradable) {
            this.tradable = tradable;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameColor() {
            return nameColor;
        }

        public void setNameColor(String nameColor) {
            this.nameColor = nameColor;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMarketName() {
            return marketName;
        }

        public void setMarketName(String marketName) {
            this.marketName = marketName;
        }

        public String getMarketHashName() {
            return marketHashName;
        }

        public void setMarketHashName(String marketHashName) {
            this.marketHashName = marketHashName;
        }

        public Integer getCommodity() {
            return commodity;
        }

        public void setCommodity(Integer commodity) {
            this.commodity = commodity;
        }

        public Integer getMarketTradableRestriction() {
            return marketTradableRestriction;
        }

        public void setMarketTradableRestriction(Integer marketTradableRestriction) {
            this.marketTradableRestriction = marketTradableRestriction;
        }

        public Integer getMarketable() {
            return marketable;
        }

        public void setMarketable(Integer marketable) {
            this.marketable = marketable;
        }

        public List<DescriptionsDTO> getDescriptions() {
            return descriptions;
        }

        public void setDescriptions(List<DescriptionsDTO> descriptions) {
            this.descriptions = descriptions;
        }

        public List<ActionsDTO> getActions() {
            return actions;
        }

        public void setActions(List<ActionsDTO> actions) {
            this.actions = actions;
        }

        public List<MarketActionsDTO> getMarketActions() {
            return marketActions;
        }

        public void setMarketActions(List<MarketActionsDTO> marketActions) {
            this.marketActions = marketActions;
        }

        public List<TagsDTO> getTags() {
            return tags;
        }

        public void setTags(List<TagsDTO> tags) {
            this.tags = tags;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DescriptionsDTO {
            @JsonProperty("type")
            private String type;
            @JsonProperty("value")
            private String value;
            @JsonProperty("color")
            private String color;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ActionsDTO {
            @JsonProperty("link")
            private String link;
            @JsonProperty("name")
            private String name;

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class MarketActionsDTO {
            @JsonProperty("link")
            private String link;
            @JsonProperty("name")
            private String name;

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TagsDTO {
            @JsonProperty("category")
            private String category;
            @JsonProperty("internal_name")
            private String internalName;
            @JsonProperty("localized_category_name")
            private String localizedCategoryName;
            @JsonProperty("localized_tag_name")
            private String localizedTagName;
            @JsonProperty("color")
            private String color;

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getInternalName() {
                return internalName;
            }

            public void setInternalName(String internalName) {
                this.internalName = internalName;
            }

            public String getLocalizedCategoryName() {
                return localizedCategoryName;
            }

            public void setLocalizedCategoryName(String localizedCategoryName) {
                this.localizedCategoryName = localizedCategoryName;
            }

            public String getLocalizedTagName() {
                return localizedTagName;
            }

            public void setLocalizedTagName(String localizedTagName) {
                this.localizedTagName = localizedTagName;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }
}
