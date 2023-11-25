package cn.iocoder.yudao.module.wms.controller.admin.ro.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 收料单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoPageReqVO extends PageParam {

    @Schema(description = "收料单号")
    private String roCode;

    @Schema(description = "收料类型")
    private String roType;

    @Schema(description = "收料状态")
    private String roStatus;

    @Schema(description = "加急")
    private String isUrgent;

    @Schema(description = "ASN")
    private String asn;

    @Schema(description = "供应商ID", example = "3418")
    private String supId;

    @Schema(description = "客户ID", example = "31016")
    private String custId;

    @Schema(description = "部门ID", example = "22020")
    private String deptId;

    @Schema(description = "员工ID", example = "5408")
    private String empId;

    @Schema(description = "来源")
    private String rosrcType;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkTime;

    @Schema(description = "关闭")
    private String closed;

    @Schema(description = "组织ID", example = "31660")
    private Long orgId;

    @Schema(description = "审核人ID")
    private String checker;

    @Schema(description = "关闭人")
    private String closer;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}