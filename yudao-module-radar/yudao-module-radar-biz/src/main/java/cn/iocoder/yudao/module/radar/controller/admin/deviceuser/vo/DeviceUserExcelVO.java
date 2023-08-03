package cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 设备和用户绑定 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class DeviceUserExcelVO {

    @ExcelProperty("自增编号")
    private Long id;

    @ExcelProperty("用户ID")
    private Long userId;

    @ExcelProperty("设备ID")
    private Long deviceId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
