package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 会员用户 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberUserRespVO extends MemberUserBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23788")
    private Long id;

    @Schema(description = "注册 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    private String registerIp;

    @Schema(description = "最后登录IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    private String loginIp;

    @Schema(description = "最后登录时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime loginDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    // ========== 其它信息 ==========

    @Schema(description = "积分", requiredMode  = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer point;

    @Schema(description = "总积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "2000")
    private Integer totalPoint;

    @Schema(description = "会员标签", example = "[红色, 快乐]")
    private List<String> tagNames;

    @Schema(description = "会员等级", example = "黄金会员")
    private String levelName;

    @Schema(description = "用户分组", example = "购物达人")
    private String groupName;

    @Schema(description = "用户经验值", requiredMode  = Schema.RequiredMode.REQUIRED, example = "200")
    private Integer experience;

    @Schema(description = "治疗进度", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer progress;

    @Schema(description = "第几周", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer weekNum;
    // ========== 扩展信息 ==========
    /**
     * 父母教育程序
     */
    @Schema(description = "父母教育程序",  example = "1")
    private Integer parentEduType;
    /**
     * 学业情况
     */
    @Schema(description = "学业情况",  example = "2")
    private Integer studyState;
    /**
     * 婚姻情况
     */
    @Schema(description = "婚姻情况",  example = "2")
    private Integer maritalState;
    /**
     * 生活状况
     */
    @Schema(description = "生活状况",  example = "3")
    private Integer livingState;
    /**
     * 居住地类型
     */
    @Schema(description = "细居住地类型",  example = "3")
    private Integer liveAreaType;

    // ========== 治疗信息信息 ==========
    /**
     * 最新治疗流程id
     */
    @Schema(description = "最新治疗流程id",  example = "3")
    private Long treatmentInstanceId;

    @Schema(description = "治疗进度状态",  example = "3")
    private Integer instanceState;

    @Schema(description = "LLM分类",  example = "3")
    private List<String> llm;

}
