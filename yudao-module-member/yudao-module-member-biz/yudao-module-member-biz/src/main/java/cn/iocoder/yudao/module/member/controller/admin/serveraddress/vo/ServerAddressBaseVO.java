package cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 服务地址 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ServerAddressBaseVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14625")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "地区编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "28471")
    @NotNull(message = "地区编码不能为空")
    private Long areaId;

    @Schema(description = "省市县/区", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "省市县/区不能为空")
    private String address;

    @Schema(description = "收件详细地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收件详细地址不能为空")
    private String detailAddress;

    @Schema(description = "是否默认", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否默认不能为空")
    private Boolean defaultStatus;

    @Schema(description = "经纬度")
    private String coordinate;

}
