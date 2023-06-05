package com.cw.module.trade.controller.admin.syncrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 账号同步记录 Excel VO
 *
 * @author chengjiale
 */
@Data
public class SyncRecordExcelVO {

    @ExcelProperty("主键ID")
    private Long id;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("账户id")
    private Long accountId;

    @ExcelProperty("同步类型")
    private String type;

    @ExcelProperty("第三方数据")
    private String thirdData;

}
