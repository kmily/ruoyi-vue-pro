package cn.iocoder.yudao.module.scan.controller.app.shape.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 体型 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppShapeBaseVO {

    @ApiModelProperty(value = "体型名称", required = true, example = "赵六")
    @NotNull(message = "体型名称不能为空")
    private String shapeName;

    @ApiModelProperty(value = "体型值", required = true)
    @NotNull(message = "体型值不能为空")
    private String shapeValue;

}
