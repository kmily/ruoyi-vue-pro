package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 设备和用户绑定 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppDeviceUserExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("用户ID")
    private Long userId;

    //@ExcelProperty("设备ID")
    private Long deviceId;

    //@ExcelProperty("家庭ID")
    private Long familyId;

    //@ExcelProperty("房间ID")
    private Long roomId;

    //@ExcelProperty("自定义设备名称")
    private String customName;


   // @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
