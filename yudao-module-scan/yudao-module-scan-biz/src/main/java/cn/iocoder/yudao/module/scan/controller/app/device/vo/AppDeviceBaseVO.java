package cn.iocoder.yudao.module.scan.controller.app.device.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 设备 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppDeviceBaseVO {

    @ApiModelProperty(value = "设备名称", required = true, example = "张三")
    @NotNull(message = "设备名称不能为空")
    private String name;

    @ApiModelProperty(value = "设备编号", required = true)
    @NotNull(message = "设备编号不能为空")
    private String code;

    @ApiModelProperty(value = "联系人", required = true)
    @NotNull(message = "联系人不能为空")
    private String contact;

    @ApiModelProperty(value = "联系电话", required = true)
    @NotNull(message = "联系电话不能为空")
    private String phone;

    @ApiModelProperty(value = "激活序号", required = true)
    @NotNull(message = "激活序号不能为空")
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

    @ApiModelProperty(value = "设备管理密码", required = true)
    @NotNull(message = "设备管理密码不能为空")
    private String password;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotNull(message = "门店名称不能为空")
    private String storeName;

}
