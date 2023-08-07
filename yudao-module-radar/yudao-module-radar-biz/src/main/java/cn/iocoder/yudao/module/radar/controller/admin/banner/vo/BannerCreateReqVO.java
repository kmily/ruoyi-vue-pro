package cn.iocoder.yudao.module.radar.controller.admin.banner.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 雷达模块banner图创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BannerCreateReqVO extends BannerBaseVO {

}
