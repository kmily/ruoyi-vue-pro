package cn.iocoder.yudao.module.oa.controller.admin.feedback.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

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
    private String creator;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updater;

}
