package cn.iocoder.yudao.module.steam.service.uu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模块类
 */
@NoArgsConstructor
@Data
public class ApiUUTemplateVO {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("hashName")
    private String hashName;
    @JsonProperty("typeId")
    private Integer typeId;
    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("typeHashName")
    private String typeHashName;
    @JsonProperty("weaponId")
    private Integer weaponId;
    @JsonProperty("weaponName")
    private String weaponName;
    @JsonProperty("weaponHashName")
    private String weaponHashName;
    @JsonProperty("updateTime")
    private String updateTime;
}
