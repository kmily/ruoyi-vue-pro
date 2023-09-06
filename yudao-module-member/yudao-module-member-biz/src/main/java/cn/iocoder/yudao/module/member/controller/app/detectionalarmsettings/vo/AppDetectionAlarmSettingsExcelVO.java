package cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 人体检测雷达设置 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppDetectionAlarmSettingsExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("设备ID")
    private Long deviceId;

    //@ExcelProperty("通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;

    //@ExcelProperty("进入告警")
    private Boolean enter;

    //@ExcelProperty("离开告警")
    private Boolean goOut;

    //@ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
