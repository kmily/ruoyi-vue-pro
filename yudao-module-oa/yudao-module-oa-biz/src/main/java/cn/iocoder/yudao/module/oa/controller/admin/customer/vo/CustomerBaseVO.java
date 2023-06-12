package cn.iocoder.yudao.module.oa.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 客户 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class CustomerBaseVO {

    @Schema(description = "名称", required = true, example = "张三")
    @NotNull(message = "名称不能为空")
    private String customerName;

    @Schema(description = "类型", example = "2")
    private Byte customerType;

    @Schema(description = "联系人", example = "张三")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区/县")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "开户行", example = "张三")
    private String bankName;

    @Schema(description = "账户", example = "23186")
    private String bankAccount;

    @Schema(description = "税号")
    private String taxNumber;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
