package com.cw.module.trade.controller.admin.notifymsg.vo;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 账号通知记录 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class NotifyMsgBaseVO {

    @Schema(description = "接受时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date acceptTime;

    @Schema(description = "接受内容")
    private String acceptInfo;

    @Schema(description = "关联交易账号", example = "31122")
    private Long accountId;

}
