package com.cw.module.trade.controller.admin.notifymsg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 账号通知记录 Excel VO
 *
 * @author chengjiale
 */
@Data
public class NotifyMsgExcelVO {

    @ExcelProperty("主键ID")
    private Long id;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("接受时间")
    private LocalDateTime acceptTime;

    @ExcelProperty("接受内容")
    private String acceptInfo;

    @ExcelProperty("关联交易账号")
    private Long accountId;

}
