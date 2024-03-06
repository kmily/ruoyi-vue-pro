package cn.iocoder.yudao.module.steam.controller.app.vo;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description = "开发平台 -  开发平台接口")
@Data
@ToString(callSuper = true)
public class OpenApiReqVo<T extends Serializable> {
    @Schema(description = "分配给大客户的AppKey", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分配给大客户的AppKey")
    private String appKey;
    @Schema(description = "时间戳", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "时间戳不能为空,格式为yyyy-MM-dd HH:mm:ss")
    private String timestamp;
    @Schema(description = "API输入参数签名结果", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "API输入参数签名结果不能为空")
    private String sign;
    @JsonUnwrapped
    @Valid
    private T data;


}

