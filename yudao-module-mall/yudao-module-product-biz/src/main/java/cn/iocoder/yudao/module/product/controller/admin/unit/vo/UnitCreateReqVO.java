package cn.iocoder.yudao.module.product.controller.admin.unit.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 计量单位创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnitCreateReqVO extends UnitBaseVO {

}
