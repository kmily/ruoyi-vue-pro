package cn.iocoder.yudao.module.system.dal.dataobject.product;

import cn.iocoder.yudao.module.system.enums.product.ProductTypeEnum;
import com.sun.xml.bind.v2.TODO;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品 DO
 *
 * @author 管理员
 */
@TableName("oa_product")
@KeySequence("oa_product_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO extends BaseDO {

    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品型号
     */
    private String productModel;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 底价
     */
    private BigDecimal reservePrice;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 产品类型
     *
     * 枚举 {@link ProductTypeEnum 对应的类}
     */
    private String productType;
    /**
     * 单位
     *
     * 枚举 {@link TODO oa_product_unit 对应的类}
     */
    private String productUnit;

}
