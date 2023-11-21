package cn.iocoder.yudao.module.system.controller.admin.organization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 组织机构 Excel 导出 Request VO，参数和 OrganizationPageReqVO 是一致的")
@Data
public class OrganizationExportReqVO {

    @Schema(description = "会员Id", example = "22738")
    private String userId;

    @Schema(description = "会员名称", example = "赵六")
    private String userName;

    @Schema(description = "是否自营")
    private Boolean selfOperated;

    @Schema(description = "是否允许员工提现")
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
    private String addressIdPath;

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
    private String images;

    @Schema(description = "联系方式")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
