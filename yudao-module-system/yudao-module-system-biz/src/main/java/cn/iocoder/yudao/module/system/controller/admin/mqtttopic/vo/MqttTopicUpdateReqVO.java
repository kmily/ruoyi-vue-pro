package cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - mqtt主题更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MqttTopicUpdateReqVO extends MqttTopicBaseVO {

    @ApiModelProperty(value = "编号", required = true, example = "5497")
    @NotNull(message = "编号不能为空")
    private Long id;

}
