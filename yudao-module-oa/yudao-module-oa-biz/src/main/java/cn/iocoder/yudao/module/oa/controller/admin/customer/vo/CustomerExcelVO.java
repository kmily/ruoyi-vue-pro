package cn.iocoder.yudao.module.oa.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 客户 Excel VO
 *
 * @author 东海
 */
@Data
public class CustomerExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("名称")
    private String customerName;

    @ExcelProperty("类型")
    private Byte customerType;

    @ExcelProperty("联系人")
    private String contactName;

    @ExcelProperty("联系电话")
    private String contactPhone;

    @ExcelProperty("省")
    private String province;

    @ExcelProperty("市")
    private String city;

    @ExcelProperty("区/县")
    private String district;

    @ExcelProperty("详细地址")
    private String address;

    @ExcelProperty("开户行")
    private String bankName;

    @ExcelProperty("账户")
    private String bankAccount;

    @ExcelProperty("税号")
    private String taxNumber;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
