package cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 用户查询明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class YouyouDetailsRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14503")
    @ExcelProperty("id")
    private Integer id;

    @Schema(description = "通过申请获取的AppKey", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @ExcelProperty("通过申请获取的AppKey")
    private String appkey;

    @Schema(description = "时间戳", example = "2016-01-01 12:00:00")
    @ExcelProperty("时间戳")
    private LocalDateTime timestamp;

    @Schema(description = "API输入参数签名结果")
    @ExcelProperty("API输入参数签名结果")
    private String sign;

    @Schema(description = "明细类型，1=订单明细，2=资金流水", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "明细类型，1=订单明细，2=资金流水", converter = DictConvert.class)
    @DictFormat("infra_config_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer dataType;

    @Schema(description = "开始时间", example = "2016-01-01 12:00:00")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2016-01-01 12:00:00")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "申请标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请标识")
    private String applyCode;

    @Schema(description = "查询明细结果返回的url")
    @ExcelProperty("查询明细结果返回的url")
    private String data;

}