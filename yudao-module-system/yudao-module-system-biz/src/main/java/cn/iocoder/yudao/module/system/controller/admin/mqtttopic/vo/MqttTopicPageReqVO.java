package cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel("管理后台 - mqtt主题分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MqttTopicPageReqVO extends PageParam {

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
