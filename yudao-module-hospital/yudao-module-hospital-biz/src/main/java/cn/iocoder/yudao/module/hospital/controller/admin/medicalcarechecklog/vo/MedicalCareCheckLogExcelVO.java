package cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 医护审核记录 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class MedicalCareCheckLogExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("医护编号")
    private Long careId;

    @ExcelProperty("审核人")
    private String checkName;

    @ExcelProperty("审核时间")
    private LocalDateTime checkTime;

    @ExcelProperty("审核意见")
    private String opinion;

    @ExcelProperty("审核状态")
    private String checkStatus;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
