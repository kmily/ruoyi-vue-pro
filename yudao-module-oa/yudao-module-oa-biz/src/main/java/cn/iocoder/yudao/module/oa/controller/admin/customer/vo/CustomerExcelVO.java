package cn.iocoder.yudao.module.oa.controller.admin.customer.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.oa.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;


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
    private String creator;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("更新者")
    private String updater;

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("名称")
    private String customerName;

    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.OA_CUSTOMER_TYPE)
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
