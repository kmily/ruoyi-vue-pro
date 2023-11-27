package cn.iocoder.yudao.module.member.controller.app.serverperson.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 被服务人创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppServerPersonCreateReqVO extends AppServerPersonBaseVO {

}
