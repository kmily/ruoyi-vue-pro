package cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 热词搜索新增/修改 Request VO")
@Data
public class HotWordsSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer id;

    @Schema(description = "热词", example = "龙狙")
    private String hotWords;

    @Schema(description = "实际昵称", example = "123")
    private String marketName;

}