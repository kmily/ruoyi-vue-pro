package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 砍价 Excel VO
 *
 * @author WangBosheng
 */
@Data
public class BargainActivityExcelVO {

    @ExcelProperty("砍价活动编号")
    private Long id;

    @ExcelProperty("关联商品ID")
    private Long spuId;

    @ExcelProperty("砍价商品表skuID")
    private Long bargainSkuId;

    @ExcelProperty("砍价活动名称")
    private String name;

    @ExcelProperty("砍价开启时间")
    private Integer startTime;

    @ExcelProperty("砍价结束时间")
    private Integer endTime;

    @ExcelProperty(value = "砍价状态 0(关闭)  1(开启)", converter = DictConvert.class)
    @DictFormat("infra_config_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Byte status;

    @ExcelProperty("用户每次砍价的最大金额")
    private Integer bargainMaxPrice;

    @ExcelProperty("用户每次砍价的最小金额")
    private Integer bargainMinPrice;

    @ExcelProperty("用户帮砍的次数")
    private Integer bargainCount;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty("砍价有效时间")
    private LocalDateTime bargainEffectiveTime;

    @ExcelProperty("砍价商品最低价")
    private BigDecimal minPrice;

    @ExcelProperty("砍价起始金额")
    private Integer bargainFirstPrice;

    @ExcelProperty("库存")
    private Integer stock;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("用户帮砍的次数")
    private Integer peopleNum;

}
