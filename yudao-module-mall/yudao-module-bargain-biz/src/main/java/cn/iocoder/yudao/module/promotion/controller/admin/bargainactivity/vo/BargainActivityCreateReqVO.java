package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 砍价创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BargainActivityCreateReqVO extends BargainActivityBaseVO {

}
