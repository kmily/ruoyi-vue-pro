package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 会员用户更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberUserUpdateReqVO extends MemberUserBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23788")
    @NotNull(message = "编号不能为空")
    private Long id;

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
     * 父母婚姻情况
     */
    @Schema(description = "父母婚姻情况",  example = "2")
    private Integer parentMaritalState;
    /**
     * 生活状况
     */
    @Schema(description = "生活状况",  example = "3")
    private Integer livingState;
    /**
     * 父母详细居住类型
     */
    @Schema(description = "父母详细居住类型",  example = "3")
    private Integer parentLiveAreaType;



}
