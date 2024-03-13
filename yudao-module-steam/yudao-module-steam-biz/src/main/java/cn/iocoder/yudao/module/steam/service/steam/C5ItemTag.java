
package cn.iocoder.yudao.module.steam.service.steam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class C5ItemTag {

    @JsonProperty("recommendTag")
    private String recommendTag;
    @JsonProperty("detailTag")
    private String detailTag;
    @JsonProperty("tagColour")
    private String tagColour;
    @JsonProperty("urlType")
    private Integer urlType;
    @JsonProperty("detailUrl")
    private String detailUrl;
}
