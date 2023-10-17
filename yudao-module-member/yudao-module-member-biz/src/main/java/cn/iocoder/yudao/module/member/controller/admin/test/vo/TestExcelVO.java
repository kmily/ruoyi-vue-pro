package cn.iocoder.yudao.module.member.controller.admin.test.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 会员标签 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class TestExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("标签名称")
    private String name;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
