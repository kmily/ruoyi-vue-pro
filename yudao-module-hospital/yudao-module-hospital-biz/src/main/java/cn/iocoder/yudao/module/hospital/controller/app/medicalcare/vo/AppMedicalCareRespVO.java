package cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

/**
 * @author whycode
 * @title: AppMedicalCareRespVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2214:10
 */

@Schema(description = "用户 APP - 医护分页 Response VO")
@Data
@ToString(callSuper = true)
public class AppMedicalCareRespVO {

    @Schema(description = "医护编号")
    private Long id;

    @Schema(description = "组织编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5204")
    private Long orgId;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "职称")
    private Byte title;

    @Schema(description = "所属机构")
    private String organization;

    @Schema(description = "服务人数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "服务时长，单位分钟", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer timeLength;

    @Schema(description = "从业时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate practiceTime;

    @Schema(description = "主要擅长")
    private Set<String> genius;

    @Schema(description = "头像路径")
    private String avatar;


    @Schema(description = "评论分数")
    private Integer commentScore;

    @Schema(description = "评论条数")
    private Integer commentCount;

    @Schema(description = "个人简介")
    private String introduction;

    @Schema(description = "收藏数量")
    private long collect;
}
