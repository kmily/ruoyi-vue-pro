package cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* mqtt主题 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class MqttTopicBaseVO {

    @ApiModelProperty(value = "主题名称", example = "李四")
    private String topicName;

    @ApiModelProperty(value = "订阅主题")
    private String subTopic;

    @ApiModelProperty(value = "发布主题")
    private String pusTopic;

    @ApiModelProperty(value = "状态0启用1未启用", required = true, example = "1")
    @NotNull(message = "状态0启用1未启用不能为空")
    private Integer status;

    @ApiModelProperty(value = "备注", example = "随便")
    private String remark;

}
