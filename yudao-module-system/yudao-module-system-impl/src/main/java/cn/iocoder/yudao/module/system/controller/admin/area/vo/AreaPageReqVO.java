package cn.iocoder.yudao.module.system.controller.admin.area.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel("管理后台 - 行政区域分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AreaPageReqVO extends PageParam {

    @ApiModelProperty(value = "数据版本")
    private String dataVersion;

    @ApiModelProperty(value = "省直辖市编码")
    private String provinceCode;

    @ApiModelProperty(value = "省直辖市")
    private String province;

    @ApiModelProperty(value = "市编码")
    private String cityCode;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区县编码")
    private String countyCode;

    @ApiModelProperty(value = "区县")
    private String county;

    @ApiModelProperty(value = "乡镇编码")
    private String townCode;

    @ApiModelProperty(value = "乡镇街道")
    private String town;

    @ApiModelProperty(value = "村社区编码")
    private String villageCode;

    @ApiModelProperty(value = "村,社区")
    private String village;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    @ApiModelProperty(value = "使用标识 1使用 0不使用")
    private Integer useFlag;

}
