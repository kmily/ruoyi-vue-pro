package cn.iocoder.yudao.module.oa.dal.dataobject.product;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.xml.bind.v2.TODO;
import lombok.*;

import java.math.BigDecimal;

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
     * id
     */
    @TableId
    private Long id;
    /**
     * 产品类型
     *
     * 枚举 {@link TODO oa_product_type 对应的类}
     */
    private String productType;
    /**
     * 单位
     *
     * 枚举 {@link TODO oa_product_unit 对应的类}
     */
    private String productUnit;

}
