package cn.iocoder.yudao.module.steam.controller.app.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ApiGoodsListReqVo implements Serializable {
    @Schema(description = "商品模版ID", example = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为0")
//    @NotNull(message = "商品模版ID")
    private String templateId;

    @Schema(description = "模板hashname", example = "templateId和hashName二选一必传递，同时传入以templateId为准，如传入不可为空")
//    @NotNull(message = "模板hashname")
    private String templateHashName;
}
