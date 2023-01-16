package cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;
import javax.validation.constraints.*;

/**
* 体型解决方案 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ShapeSolutionBaseVO {

    @ApiModelProperty(value = "标题", required = true)
    @NotNull(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "类型", required = true)
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "内容", required = true)
    @NotNull(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "体型id", required = true)
    @NotNull(message = "体型id不能为空")
    private Integer shapeShapeId;

    @ApiModelProperty(value = "排序", required = true, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

}
