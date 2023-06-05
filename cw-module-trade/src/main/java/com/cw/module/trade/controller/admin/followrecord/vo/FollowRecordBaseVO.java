package com.cw.module.trade.controller.admin.followrecord.vo;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 账号跟随记录 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class FollowRecordBaseVO {

    @Schema(description = "账号通知信息", example = "4307")
    private Long accountNotifyId;

    @Schema(description = "跟随账号", example = "25533")
    private Long followAccount;
    
    @Schema(description = "第三方订单id", example = "25533")
    private Long thirdOrderId;
    
    @Schema(description = "跟随第三方订单id", example = "25533")
    private Long followThridOrderId;

    @Schema(description = "操作账号", example = "15154")
    private Long operateAccount;

    @Schema(description = "操作时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date operateTime;

    @Schema(description = "操作内容")
    private String operateInfo;

    @Schema(description = "操作结果")
    private Boolean operateSuccess;

    @Schema(description = "操作结果响应数据")
    private String operateResult;

    @Schema(description = "跟单操作描述")
    private String operateDesc;

}
