package cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 店面 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class SysShopBaseVO {

    @ApiModelProperty(value = "店面名称", required = true)
    @NotNull(message = "店面名称不能为空")
    private String shopName;

    @ApiModelProperty(value = "店面城市", required = true)
    @NotNull(message = "店面城市不能为空")
    private String shopCity;

    @ApiModelProperty(value = "店面地址", required = true)
    @NotNull(message = "店面地址不能为空")
    private String shopAddress;

    @ApiModelProperty(value = "门牌号", required = true)
    @NotNull(message = "门牌号不能为空")
    private String shopAddressNum;

    @ApiModelProperty(value = "店面分组")
    private String shopGroup;

    @ApiModelProperty(value = "状态（0正常 1停用）", required = true)
    @NotNull(message = "状态（0正常 1停用）不能为空")
    private Integer status;

}
