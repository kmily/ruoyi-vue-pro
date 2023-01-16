package cn.iocoder.yudao.module.scan.controller.admin.device.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 设备 Excel 导出 Request VO", description = "参数和 DevicePageReqVO 是一致的")
@Data
public class DeviceExportReqVO {

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @ApiModelProperty(value = "设备名称", example = "张三")
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String code;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "激活序号")
    private Integer serialNo;

    @ApiModelProperty(value = "省名称", example = "张三")
    private String provinceName;

    @ApiModelProperty(value = "省编号")
    private String provinceCode;

    @ApiModelProperty(value = "市名称", example = "张三")
    private String cityName;

    @ApiModelProperty(value = "市编号")
    private String cityCode;

    @ApiModelProperty(value = "区名称", example = "赵六")
    private String areaName;

    @ApiModelProperty(value = "区编号")
    private String areaCode;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "设备管理密码")
    private String password;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

}
