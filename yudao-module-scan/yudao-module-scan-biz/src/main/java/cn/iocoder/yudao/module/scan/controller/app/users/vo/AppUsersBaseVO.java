package cn.iocoder.yudao.module.scan.controller.app.users.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 扫描用户 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppUsersBaseVO {

    @ApiModelProperty(value = "姓名", example = "芋艿")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "设备id", required = true, example = "30252")
    @NotNull(message = "设备id不能为空")
    private Integer deviceId;

}
