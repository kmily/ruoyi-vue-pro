package cn.iocoder.yudao.module.steam.controller.admin.adblock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.ad.AdDO;

@Schema(description = "管理后台 - 广告位新增/修改 Request VO")
@Data
public class AdBlockSaveReqVO {

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "17543")
    private Long id;

    @Schema(description = "广告名称", example = "李四")
    private String adName;

    @Schema(description = "广告列表")
    private List<AdDO> ads;

}