package cn.iocoder.yudao.module.oa.controller.admin.contract.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 合同 Excel VO
 *
 * @author 管理员
 */
@Data
public class ContractExcelVO {

    @ExcelProperty("合同编号")
    private String contractNo;

    @ExcelProperty("公司类型")
    private String companyType;

    @ExcelProperty("客户id")
    private Long customerId;

    @ExcelProperty("供方代表")
    private Long supplierUserId;

    @ExcelProperty("总款")
    private BigDecimal totalFee;

    @ExcelProperty("劳务费")
    private BigDecimal serviceFee;

    @ExcelProperty("佣金")
    private BigDecimal commissions;

    @ExcelProperty("零星费用")
    private BigDecimal otherFee;

    @ExcelProperty("工程实施联系人")
    private String implContactName;

    @ExcelProperty("工程实施联系电话")
    private String implContactPhone;

    @ExcelProperty("合同状态")
    private int status;

    @ExcelProperty("审批状态")
    private int approvalStatus;

    @ExcelProperty("创建者")
    private String creator;

}
