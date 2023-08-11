package cn.iocoder.yudao.module.member.controller.app.devicenotice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 设备通知 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppDeviceNoticeExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("用户ID")
    private Long userId;

    //@ExcelProperty("家庭ID")
    private Long familyId;

    //@ExcelProperty("设备ID")
    private Long deviceId;

    //@ExcelProperty("卡片名称")
    private String content;

    //@ExcelProperty("设备类型")
    private Byte type;

    //@ExcelProperty("状态（0未读 1已读）")
    private Byte status;

    //@ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
