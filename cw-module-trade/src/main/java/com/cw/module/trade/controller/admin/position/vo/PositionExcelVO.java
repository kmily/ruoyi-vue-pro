package com.cw.module.trade.controller.admin.position.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 账户持仓信息 Excel VO
 *
 * @author chengjiale
 */
@Data
public class PositionExcelVO {

    @ExcelProperty("主键ID")
    private Long id;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("账户id")
    private Long accountId;

    @ExcelProperty("交易对")
    private String symbol;

    @ExcelProperty("持仓数量")
    private Object quantity;

    @ExcelProperty("第三方数据")
    private String thirdData;

}
