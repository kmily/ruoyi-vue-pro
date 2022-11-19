package cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 业务字典分类 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class QgDictTreeBaseVO {

    @ApiModelProperty(value = "名称", required = true,example="节点1")
    @NotNull(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "父id", required = true,example="1")
    @NotNull(message = "父id不能为空")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序", required = true)
    private Integer sort;

    @ApiModelProperty(value = "状态（新增不用传入）", required = true)
    private Integer status;

    @ApiModelProperty(value = "层级（新增传入父节点level）", required = true,example="1")
    private Integer level;

    @ApiModelProperty(value = "类型（代表节点所处的范围）", required = true,example="1（学习形式）")
    private String type;

    @ApiModelProperty(value = "只读（0正常 1只读）", required = true,example="1")
    private Integer isRead;

}
