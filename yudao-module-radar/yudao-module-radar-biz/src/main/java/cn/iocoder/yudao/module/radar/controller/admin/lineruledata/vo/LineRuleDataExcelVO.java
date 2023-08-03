package cn.iocoder.yudao.module.radar.controller.admin.lineruledata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 绊线数据 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class LineRuleDataExcelVO {

    @ExcelProperty("自增编号")
    private Long id;

    @ExcelProperty("设备ID")
    private Long deviceId;

    @ExcelProperty("设备编号")
    private String deviceCode;

    @ExcelProperty("上报时间")
    private Long timeStamp;

    @ExcelProperty("消息序号。用于判定数据连续性")
    private Integer seq;

    @ExcelProperty("绊线规则数量")
    private Integer lineNum;

    @ExcelProperty("绊线统计数据")
    private String lineData;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
