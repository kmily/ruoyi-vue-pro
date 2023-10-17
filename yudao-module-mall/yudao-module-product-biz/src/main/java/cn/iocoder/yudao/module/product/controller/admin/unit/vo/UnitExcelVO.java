package cn.iocoder.yudao.module.product.controller.admin.unit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 计量单位 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class UnitExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("单位名称")
    private String name;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
