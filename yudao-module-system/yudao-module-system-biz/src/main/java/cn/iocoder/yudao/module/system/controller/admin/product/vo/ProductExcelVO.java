package cn.iocoder.yudao.module.system.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


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
    @DictFormat("oa_product_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String productType;

    @ExcelProperty(value = "单位", converter = DictConvert.class)
    @DictFormat("oa_product_unit") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String productUnit;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
