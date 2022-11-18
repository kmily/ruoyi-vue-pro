package cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel("管理后台 - 店面分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SysShopPageReqVO extends PageParam {

    @ApiModelProperty(value = "店面名称")
    private String shopName;

    @ApiModelProperty(value = "店面城市")
    private String shopCity;

    @ApiModelProperty(value = "店面地址")
    private String shopAddress;

    @ApiModelProperty(value = "门牌号")
    private String shopAddressNum;

    @ApiModelProperty(value = "店面分组")
    private String shopGroup;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
