package cn.iocoder.yudao.module.member.controller.admin.visitpage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 页面访问数据 Excel VO
 *
 * @author 和尘同光
 */
@Data
public class VisitPageExcelVO {

    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("用户ID")
    private Long userId;

    @ExcelProperty("访问页面")
    private String pageName;

    @ExcelProperty("进入时间")
    private LocalDateTime startTime;

    @ExcelProperty("离开时间")
    private LocalDateTime endTime;

    @ExcelProperty("访问用时单位毫秒")
    private Integer useTime;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
