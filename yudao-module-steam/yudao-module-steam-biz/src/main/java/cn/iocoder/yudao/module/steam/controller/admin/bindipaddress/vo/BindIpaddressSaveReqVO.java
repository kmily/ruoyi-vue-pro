package cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

@Schema(description = "管理后台 - 绑定用户IP地址池新增/修改 Request VO")
@Data
public class BindIpaddressSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15530")
    private Long id;

    @Schema(description = "端口")
    private Long port;

    @Schema(description = "地址池id", example = "5584")
    private Long addressId;

    @Schema(description = "ip地址")
    private String ipAddress;

}