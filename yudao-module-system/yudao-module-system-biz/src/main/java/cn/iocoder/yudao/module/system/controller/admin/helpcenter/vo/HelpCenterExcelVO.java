package cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 常见问题 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class HelpCenterExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("帮助类型")
    private Byte type;

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("问题描述")
    private String description;

    @ExcelProperty("状态（0正常 1禁用）")
    private Byte status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
