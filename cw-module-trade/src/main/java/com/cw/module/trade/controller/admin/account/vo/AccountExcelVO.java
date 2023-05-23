package com.cw.module.trade.controller.admin.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 交易账号 Excel VO
 *
 * @author chengjiale
 */
@Data
public class AccountExcelVO {

    @ExcelProperty("主键ID")
    private Long id;

    @ExcelProperty("账号名称")
    private String name;

    @ExcelProperty("api访问key")
    private String appKey;

    @ExcelProperty("api访问秘钥")
    private String appSecret;

    @ExcelProperty("余额（第三方查询返回信息）")
    private String balance;

    @ExcelProperty("最后一次余额查询时间(时间戳)")
    private Long lastBalanceQueryTime;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("账号管理用户ID")
    private Long relateUser;

}
