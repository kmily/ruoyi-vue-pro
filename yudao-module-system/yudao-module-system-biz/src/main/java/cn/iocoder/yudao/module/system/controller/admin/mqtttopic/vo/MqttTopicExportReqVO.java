package cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - mqtt主题 Excel 导出 Request VO", description = "参数和 MqttTopicPageReqVO 是一致的")
@Data
public class MqttTopicExportReqVO {

    @ApiModelProperty(value = "主题名称", example = "李四")
    private String topicName;

    @ApiModelProperty(value = "订阅主题")
    private String subTopic;

    @ApiModelProperty(value = "发布主题")
    private String pusTopic;

    @ApiModelProperty(value = "状态0启用1未启用", example = "1")
    private Integer status;

    @ApiModelProperty(value = "备注", example = "随便")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
