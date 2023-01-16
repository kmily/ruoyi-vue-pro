package cn.iocoder.yudao.module.scan.controller.admin.shape.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 体型 Excel VO
 *
 * @author lyz
 */
@Data
public class ShapeExcelVO {

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("体型名称")
    private String shapeName;

    @ExcelProperty("体型值")
    private String shapeValue;

    @ExcelProperty("id")
    private Long id;

}
