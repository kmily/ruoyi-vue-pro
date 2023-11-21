package cn.iocoder.yudao.module.system.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 组织机构 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class OrganizationBaseVO {

    @Schema(description = "负责人编号", example = "22738")
    private Long userId;

    @Schema(description = "负责人名称", example = "赵六")
    private String userName;

    @Schema(description = "是否自营", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否自营不能为空")
    private Boolean selfOperated;

    @Schema(description = "是否允许员工提现", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否允许员工提现不能为空")
    private Byte cashOut;

    @Schema(description = "机构状态")
    private String disable;

    @Schema(description = "机构logo")
    private String logo;

    @Schema(description = "机构名称", example = "芋艿")
    private String name;

    @Schema(description = "机构地址")
    private String addressDetail;

    @Schema(description = "地址id")
    private Long addressIdPath;

    @Schema(description = "地址名称")
    private String addressPath;

    @Schema(description = "经纬度")
    private String center;

    @Schema(description = "机构简介")
    private String introduction;

    @Schema(description = "物流评分")
    private Double deliveryScore;

    @Schema(description = "描述评分")
    private Double descriptionScore;

    @Schema(description = "服务评分")
    private Double serviceScore;

    @Schema(description = "员工数量")
    private Integer staffNum;

    @Schema(description = "商品数量")
    private Integer goodsNum;

    @Schema(description = "收藏数量")
    private Integer collectionNum;

    @Schema(description = "宣传图册")
    private List<String> images;

    @Schema(description = "套餐编号")
    private Long packageId;
}
