package cn.iocoder.yudao.module.therapy.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 会员用户 Response VO")
@Data
@ToString(callSuper = true)
public class UserRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23788")
    private Long id;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "用户昵称", example = "李四")
    private String name;

    @Schema(description = "用户性别", example = "1")
    private Byte sex;

    @Schema(description = "最新治疗流程id",  example = "3")
    private Long treatmentInstanceId;

    @Schema(description = "治疗进度状态",  example = "3")
    private Integer instanceState;

    @Schema(description = "LLM分类",  example = "3")
    private List<String> llm;

    @Schema(description = "预约时间",example = "2024-05-23")
    private Date appointmentDate;

    @Schema(description = "预约时间段,字典:booking_time", example = "1")
    private Integer appointmentTimeRange;

    private LocalDateTime createTime;

    /**
     * ab测试组
     * 字典类型:user_test_group
     */
    private Integer testGroup;

}
