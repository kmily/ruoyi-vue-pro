package cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 体型解决方案 Excel VO
 *
 * @author lyz
 */
@Data
public class ShapeSolutionExcelVO {

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("scan_solution_suggest_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("内容")
    private String content;

    @ExcelProperty("体型id")
    private Integer shapeShapeId;

    @ExcelProperty("排序")
    private Integer sort;

}
