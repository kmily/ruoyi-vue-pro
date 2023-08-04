package cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 人员存在感知雷达设置 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppExistAlarmSettingsExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("设备ID")
    private Long deviceId;

    //@ExcelProperty("进入告警")
    private Boolean enter;

    //@ExcelProperty("离开告警")
    private Boolean leave;

    //@ExcelProperty("人员滞留报警")
    private Boolean stop;

    //@ExcelProperty("无人报警")
    private Boolean nobody;

    //@ExcelProperty("滞留时间 单位分钟")
    private Byte stopTime;

    //@ExcelProperty("无人时间 单位分钟")
    private Byte nobodyTime;

   // @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
