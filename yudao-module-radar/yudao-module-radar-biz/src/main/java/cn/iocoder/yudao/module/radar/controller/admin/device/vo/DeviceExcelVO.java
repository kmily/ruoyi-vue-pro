package cn.iocoder.yudao.module.radar.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 设备信息 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class DeviceExcelVO {

    @ExcelProperty("设备ID")
    private Long id;

    @ExcelProperty("设备sn编号")
    private String sn;

    @ExcelProperty("设备名称")
    private String name;

    @ExcelProperty("设备类别")
    private Integer type;

    @ExcelProperty("部门状态（0正常 1停用）")
    private Byte status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
