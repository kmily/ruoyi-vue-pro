package cn.iocoder.yudao.module.steam.controller.admin.seltype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selweapon.SelWeaponDO;

@Schema(description = "管理后台 - 类型选择新增/修改 Request VO")
@Data
public class SelTypeSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5767")
    private Long id;

    @Schema(description = "英文名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "英文名字不能为空")
    private String internalName;

    @Schema(description = "中文名称", example = "李四")
    private String localizedTagName;

    @Schema(description = "字体颜色")
    private String color;

}