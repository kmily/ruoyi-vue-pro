package cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 商机 Excel VO
 *
 * @author 管理员
 */
@Data
public class OpportunityExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("商机标题")
    private String businessTitle;

    @ExcelProperty("商机详情")
    private String detail;

    @ExcelProperty("上报时间")
    private LocalDateTime reportTime;

    @ExcelProperty("跟进用户id")
    private Long followUserId;

    @ExcelProperty("商机状态")
    private String status;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建者")
    private String creator;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updater;

}
