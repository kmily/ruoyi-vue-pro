package cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提现银行卡 Excel VO
 *
 * @author 管理人
 */
@Data
public class CareBankCardAppExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("用户编号")
    private Long careId;

    @ExcelProperty("持卡人")
    private String name;

    @ExcelProperty("卡号")
    private String cardNo;

    @ExcelProperty("开户行")
    private String bank;

    @ExcelProperty("身份证")
    private String idCard;

    @ExcelProperty("预留手机号")
    private String mobile;

    @ExcelProperty("是否默认")
    private Boolean defaultStatus;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
