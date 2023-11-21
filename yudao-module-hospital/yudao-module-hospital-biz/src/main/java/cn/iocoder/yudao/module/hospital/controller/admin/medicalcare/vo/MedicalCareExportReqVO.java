package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 医护管理 Excel 导出 Request VO，参数和 MedicalCarePageReqVO 是一致的")
@Data
public class MedicalCareExportReqVO {

    @Schema(description = "组织编号", example = "5204")
    private Long orgId;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "身份证图片[正,反]")
    private String cardPath;

    @Schema(description = "联系方式")
    private String mobile;

    @Schema(description = "出生日期")
    private LocalDateTime birthday;

    @Schema(description = "用户性别")
    private Byte sex;

    @Schema(description = "紧急联系人")
    private String critical;

    @Schema(description = "是否实名 NO-未实名，YES-实名", example = "赵六")
    private String realname;

    @Schema(description = "职称")
    private Byte title;

    @Schema(description = "所属机构")
    private String organization;

    @Schema(description = "服务人数")
    private Integer quantity;

    @Schema(description = "服务时长，单位分钟")
    private Integer timeLength;

    @Schema(description = "从业时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] practiceTime;

    @Schema(description = "主要擅长")
    private String genius;

    @Schema(description = "是否完善 NO-未实名，YES-实名")
    private String perfect;

    @Schema(description = "是否资质认证 NO-未实名，YES-实名")
    private String aptitude;

    @Schema(description = "个人简介")
    private String introduction;

    @Schema(description = "最终审核状态", example = "1")
    private String status;

    @Schema(description = "最终审核人", example = "王五")
    private String checkName;

    @Schema(description = "最终审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkTime;

    @Schema(description = "审核意见")
    private String opinion;

    @Schema(description = "是否是店主")
    private Boolean shopkeeper;

    @Schema(description = "是否是超级管理员 超级管理员/普通管理员")
    private Boolean isSuper;

    @Schema(description = "是否可以提现")
    private Byte cashOut;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
