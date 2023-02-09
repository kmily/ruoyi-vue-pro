package cn.iocoder.yudao.module.system.controller.admin.mqtttopic.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * mqtt主题 Excel VO
 *
 * @author ZanGe
 */
@Data
public class MqttTopicExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("主题名称")
    private String topicName;

    @ExcelProperty("订阅主题")
    private String subTopic;

    @ExcelProperty("发布主题")
    private String pusTopic;

    @ExcelProperty("状态0启用1未启用")
    private Integer status;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
