package cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 区域数据 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AreaRuleDataBaseVO {

    @Schema(description = "设备ID", required = true, example = "16768")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "上报时间")
    private Long timeStamp;

    @Schema(description = "消息序号。用于判定数据连续性")
    private Integer seq;

    @Schema(description = "绊线规则数量")
    private Integer areaNum;

    @Schema(description = "区域统计数据")
    private String areaData;

}
