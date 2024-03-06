package cn.iocoder.yudao.module.steam.service.uu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

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
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private LocalDateTime[] updateTime;

}
