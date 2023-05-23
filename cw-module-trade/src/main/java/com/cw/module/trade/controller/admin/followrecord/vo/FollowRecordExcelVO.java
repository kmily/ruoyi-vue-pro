package com.cw.module.trade.controller.admin.followrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 账号跟随记录 Excel VO
 *
 * @author chengjiale
 */
@Data
public class FollowRecordExcelVO {

    @ExcelProperty("主键ID")
    private Long id;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("账号通知信息")
    private Long accountNotifyId;

    @ExcelProperty("跟随账号")
    private Long followAccount;

    @ExcelProperty("操作账号")
    private Long operateAccount;

    @ExcelProperty("操作时间")
    private LocalDateTime operateTime;

    @ExcelProperty("操作内容")
    private String operateInfo;

    @ExcelProperty("操作结果")
    private Boolean operateSuccess;

    @ExcelProperty("操作结果响应数据")
    private String operateResult;

    @ExcelProperty("跟单操作描述")
    private String operateDesc;

}
