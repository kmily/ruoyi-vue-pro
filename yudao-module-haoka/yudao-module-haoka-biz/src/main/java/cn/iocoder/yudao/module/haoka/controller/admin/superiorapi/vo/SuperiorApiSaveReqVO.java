package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;

@Schema(description = "管理后台 - 上游API接口新增/修改 Request VO")
@Data
public class SuperiorApiSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24472")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "名字不能为空")
    private String name;

    @Schema(description = "是否有选号功能", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否有选号功能不能为空")
    private Boolean enableSelectNum;

    @Schema(description = "异常订单处理方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "异常订单处理方式不能为空")
    private Integer abnormalOrderHandleMethod;

    @Schema(description = "接口状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "接口状态不能为空")
    private Integer status;

    @Schema(description = "发布日期")
    private LocalDateTime publishTime;

    @Schema(description = "API文档")
    private String apiDoc;

    @Schema(description = "是否已配置开发", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否已配置开发不能为空")
    private Boolean isDevConfined;

    @Schema(description = "是否已配置产品", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否已配置产品不能为空")
    private Boolean isSkuConfined;

    @Schema(description = "部门ID", example = "12247")
    private Long deptId;

}