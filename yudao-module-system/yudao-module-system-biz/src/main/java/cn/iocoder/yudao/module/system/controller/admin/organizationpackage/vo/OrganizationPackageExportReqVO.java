package cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 机构套餐 Excel 导出 Request VO，参数和 OrganizationPackagePageReqVO 是一致的")
@Data
public class OrganizationPackageExportReqVO {

    @Schema(description = "套餐名", example = "王五")
    private String name;

    @Schema(description = "租户状态（0正常 1停用）", example = "1")
    private Byte status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "关联的菜单编号")
    private Set<Long> menuIds;

    @Schema(description = "是否为默认")
    private Boolean isDefault;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
