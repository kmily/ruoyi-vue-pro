package cn.iocoder.yudao.module.system.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 客户管理 Excel 导出 Request VO，参数和 CustomerPageReqVO 是一致的")
@Data
public class CustomerExportReqVO {

    @Schema(description = "联系人", example = "王五")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "名称", example = "张三")
    private String customerName;

    @Schema(description = "类型", example = "1")
    private String customerType;

    @Schema(description = "市")
    private String city;

}
