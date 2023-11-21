package cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 医护资质 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppCareAptitudeExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("资质编号")
    private Long aptitudeId;

    @ExcelProperty("资质名称")
    private String aptitudeName;

    @ExcelProperty("医护编号")
    private Long careId;

    @ExcelProperty("证书正面")
    private String imageFront;

    @ExcelProperty("证书反面")
    private String imageBack;

    @ExcelProperty("最终审核人")
    private String checkName;

    @ExcelProperty("最终审核时间")
    private LocalDateTime checkTime;

    @ExcelProperty("最终审核状态")
    private String checkStatus;

    @ExcelProperty("审核意见")
    private String opinion;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
