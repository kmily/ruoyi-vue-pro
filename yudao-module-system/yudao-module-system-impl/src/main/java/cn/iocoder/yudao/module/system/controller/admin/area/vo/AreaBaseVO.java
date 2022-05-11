package cn.iocoder.yudao.module.system.controller.admin.area.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 行政区域 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AreaBaseVO {

    @ApiModelProperty(value = "数据版本", required = true)
    @NotNull(message = "数据版本不能为空")
    private String dataVersion;

    @ApiModelProperty(value = "省直辖市编码", required = true)
    @NotNull(message = "省直辖市编码不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "省直辖市", required = true)
    @NotNull(message = "省直辖市不能为空")
    private String province;

    @ApiModelProperty(value = "市编码", required = true)
    @NotNull(message = "市编码不能为空")
    private String cityCode;

    @ApiModelProperty(value = "市", required = true)
    @NotNull(message = "市不能为空")
    private String city;

    @ApiModelProperty(value = "区县编码", required = true)
    @NotNull(message = "区县编码不能为空")
    private String countyCode;

    @ApiModelProperty(value = "区县", required = true)
    @NotNull(message = "区县不能为空")
    private String county;

    @ApiModelProperty(value = "乡镇编码", required = true)
    @NotNull(message = "乡镇编码不能为空")
    private String townCode;

    @ApiModelProperty(value = "乡镇街道", required = true)
    @NotNull(message = "乡镇街道不能为空")
    private String town;

    @ApiModelProperty(value = "村社区编码", required = true)
    @NotNull(message = "村社区编码不能为空")
    private String villageCode;

    @ApiModelProperty(value = "村,社区", required = true)
    @NotNull(message = "村,社区不能为空")
    private String village;

    @ApiModelProperty(value = "经度", required = true)
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度", required = true)
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @ApiModelProperty(value = "使用标识 1使用 0不使用", required = true)
    @NotNull(message = "使用标识 1使用 0不使用不能为空")
    private Integer useFlag;

}
