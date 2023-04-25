package cn.iocoder.yudao.module.system.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 客户管理 Excel VO
 *
 * @author 管理员
 */
@Data
public class CustomerExcelVO {

    @ExcelProperty("联系人")
    private String contactName;

    @ExcelProperty("联系电话")
    private String contactPhone;

    @ExcelProperty("详细地址")
    private String address;

    @ExcelProperty("开户行")
    private String bankName;

    @ExcelProperty("账户")
    private String bankAccount;

    @ExcelProperty("税号")
    private String taxNumber;

    @ExcelProperty("创建者")
    private String createBy;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updateBy;

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("名称")
    private String customerName;

    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("oa_customer_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String customerType;

    @ExcelProperty("省")
    private String province;

    @ExcelProperty("市")
    private String city;

    @ExcelProperty("区/县")
    private String district;

    @ExcelProperty("备注")
    private String remark;

}
