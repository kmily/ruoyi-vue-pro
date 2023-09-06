package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author whycode
 * @title: AppHomePageCopyReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3016:23
 */
@Schema(description = "用户 APP - 首页配置复制 Request VO")
@Data
public class AppHomePageCopyReqVO {

    @Schema(description = "家庭ID", required = true, example = "23700")
    @NotNull(message = "家庭ID不能为空")
    private Long familyId;

    @Schema(description = "数据类型 0-睡眠,1-如厕,2-跌倒,3-离/回家", example = "1")
    @NotNull(message = "数据类型不能为空")
    @Range(min = 0, max = 3, message = "数据类型必须为 0, 1, 2, 3")
    private Byte type;

}
