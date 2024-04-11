package cn.iocoder.yudao.module.steam.dal.dataobject.otherselling;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AvatarResponse {
    @JsonProperty("code")
    private int code;

    @JsonProperty("imgurl")
    private String imgUrl;
}