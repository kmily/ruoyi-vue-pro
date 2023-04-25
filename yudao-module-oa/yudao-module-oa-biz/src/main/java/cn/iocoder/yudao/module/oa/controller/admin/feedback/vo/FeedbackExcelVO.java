package cn.iocoder.yudao.module.oa.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 产品反馈 Excel VO
 *
 * @author 管理员
 */
@Data
public class FeedbackExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("客户名称")
    private String customerName;

    @ExcelProperty("反馈内容")
    private String feedbackContent;

    @ExcelProperty("联系人")
    private String contactName;

    @ExcelProperty("联系电话")
    private String contactPhone;

    @ExcelProperty("附件")
    private String appendFiles;

    @ExcelProperty("创建者")
    private String createBy;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updateBy;

}
