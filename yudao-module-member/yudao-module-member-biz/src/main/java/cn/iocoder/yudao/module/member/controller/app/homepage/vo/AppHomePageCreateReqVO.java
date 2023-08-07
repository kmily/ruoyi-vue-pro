package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 首页配置创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppHomePageCreateReqVO extends AppHomePageBaseVO {

}
