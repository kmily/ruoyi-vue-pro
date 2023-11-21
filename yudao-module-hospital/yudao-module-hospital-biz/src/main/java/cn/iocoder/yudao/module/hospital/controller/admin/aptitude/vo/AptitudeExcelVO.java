package cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 资质信息 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AptitudeExcelVO {

    @ExcelProperty("收藏编号")
    private Long id;

    @ExcelProperty("资质名称")
    private String name;

    @ExcelProperty("资质图标")
    private String picUrl;

    @ExcelProperty("状态（0正常 1停用）")
    private Byte status;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
