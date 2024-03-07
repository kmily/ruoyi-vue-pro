package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiCommodityReSpVo  implements Serializable {

    @Schema(description = "商品模板id", example = "798 templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为0")
    private Integer templateId;

    @Schema(description = "模板hashname", example = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为空")
    private String templateHashName;

}
