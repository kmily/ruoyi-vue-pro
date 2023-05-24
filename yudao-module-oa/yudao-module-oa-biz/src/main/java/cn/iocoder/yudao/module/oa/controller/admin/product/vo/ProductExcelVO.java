package cn.iocoder.yudao.module.oa.controller.admin.product.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.oa.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 产品 Excel VO
 *
 * @author 管理员
 */
@Data
public class ProductExcelVO {

    @ExcelProperty("产品编码")
    private String productCode;

    @ExcelProperty("产品型号")
    private String productModel;

    @ExcelProperty("单价")
    private BigDecimal price;

    @ExcelProperty("底价")
    private BigDecimal reservePrice;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty(value = "产品类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.OA_PRODUCT_TYPE)
    private String productType;

    @ExcelProperty(value = "单位", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.OA_PRODUCT_UNIT)
    private String productUnit;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
