package cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QgDictTreeKindTopVO {
    @ApiModelProperty(value = "分类id", required = true)
    private Integer id;

    @ApiModelProperty(value = "名称", example = "芋道", notes = "模糊匹配")
    private String name;

    @ApiModelProperty(value = "上级id", example = "1", notes = "")
    private Integer parentId;

    @ApiModelProperty(value = "类型", example = "2", notes = "")
    private String Type;

}
