package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 用户启动数据 Excel VO
 *
 * @author 和尘同光
 */
@Data
public class BootUpExcelVO {

    @ExcelProperty("日期")
    private String date;

    @ExcelProperty("启动次数")
    private Integer times;

    @ExcelProperty("启动次数占比")
    private String percentage;

}
