package cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 商机-跟进日志 Excel VO
 *
 * @author 东海
 */
@Data
public class OpportunityFollowLogExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("商机id")
    private Long businessId;

    @ExcelProperty("跟进日志内容")
    private String logContent;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
