package cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理后台 - 租户精简 Response VO
 *
 * @author owen
 * @since 2023-03-08
 */
@Schema(description = "管理后台 - 租户精简 Response VO")
@Data
public class TenantSimpleRespVO {

    @Schema(description = "租户编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "租户名", required = true, example = "芋道")
    private String name;

//    @Schema(description = "站点名称", required = true, example = "芋道管理系统")
//    private String title;
//
//    @Schema(description = "租户Logo", required = true, example = "logo.pn")
//    private String logo;
//
//    @Schema(description = "ico图标", required = true, example = "favicon.ico")
//    private String ico;

}
