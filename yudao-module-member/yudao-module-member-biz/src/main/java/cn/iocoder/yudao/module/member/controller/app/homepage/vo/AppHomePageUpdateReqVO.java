package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 首页配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppHomePageUpdateReqVO extends AppHomePageBaseVO {

    @Schema(description = "自增编号", required = true, example = "5597")
    @NotNull(message = "自增编号不能为空")
    private Long id;

}
