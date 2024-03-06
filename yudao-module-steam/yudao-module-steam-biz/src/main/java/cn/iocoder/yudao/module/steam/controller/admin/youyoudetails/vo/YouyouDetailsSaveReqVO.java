package cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户查询明细新增/修改 Request VO")
@Data
public class YouyouDetailsSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14503")
    private Integer id;

    @Schema(description = "通过申请获取的AppKey", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "通过申请获取的AppKey不能为空")
    private String appkey;

    @Schema(description = "时间戳", example = "2016-01-01 12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "API输入参数签名结果")
    private String sign;

    @Schema(description = "明细类型，1=订单明细，2=资金流水", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "明细类型，1=订单明细，2=资金流水不能为空")
    private Integer dataType;

    @Schema(description = "开始时间", example = "2016-01-01 12:00:00")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2016-01-01 12:00:00")
    private LocalDateTime endTime;

    @Schema(description = "申请标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请标识不能为空")
    private String applyCode;

    @Schema(description = "查询明细结果返回的url")
    private String data;

}