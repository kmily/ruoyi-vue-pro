package cn.iocoder.yudao.module.radar.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 设备信息 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class DeviceBaseVO {

    @Schema(description = "设备sn编号", required = true)
    @NotNull(message = "设备sn编号不能为空")
    private String sn;

    @Schema(description = "设备名称", required = true, example = "李四")
    @NotNull(message = "设备名称不能为空")
    private String name;

    @Schema(description = "设备类别", required = true, example = "1")
    @NotNull(message = "设备类别不能为空")
    private Integer type;

    @Schema(description = "状态（0正常 1停用）", required = true, example = "2")
    @NotNull(message = "部门状态（0正常 1停用）不能为空")
    private Byte status;

    @Schema(description = "绑定状态（0未 1已）", required = true, example = "1")
    private Byte bind;

    @Schema(description = "保活时间", required = true)
    private LocalDateTime keepalive;


    @Schema(description = "绑定时间", required = true)
    private LocalDateTime bindTime;
}
