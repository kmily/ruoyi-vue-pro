package cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 跌倒雷达设置 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppFallAlarmSettingsExcelVO {

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

    //@ExcelProperty("跌倒告警")
    private Boolean fall;

    //@ExcelProperty("起身消警")
    private Boolean getUp;

    //@ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
