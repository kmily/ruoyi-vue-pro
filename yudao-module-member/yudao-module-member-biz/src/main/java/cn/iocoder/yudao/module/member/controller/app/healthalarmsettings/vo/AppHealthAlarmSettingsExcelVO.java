package cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 体征检测雷达设置 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppHealthAlarmSettingsExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("设备ID")
    private Long deviceId;

    //@ExcelProperty("通知方式 0-不通知,1-短信,2-电话,3-电话短信,4-微信通知")
    private Byte notice;

    //@ExcelProperty("心率异常告警")
    private Boolean heart;

    //@ExcelProperty("正常心率范围")
    private String heartRange;

    //@ExcelProperty("呼吸告警设置")
    private Boolean breathe;

    //@ExcelProperty("正常心率范围 [16,24]")
    private String breatheRange;

    //@ExcelProperty("体动检测告警")
    private Boolean move;

    //@ExcelProperty("坐起告警")
    private Boolean sitUp;

    //@ExcelProperty("离床告警")
    private Boolean outBed;

    //@ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
