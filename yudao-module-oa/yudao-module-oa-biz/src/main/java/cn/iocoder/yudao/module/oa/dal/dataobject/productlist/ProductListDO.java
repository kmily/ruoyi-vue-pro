package cn.iocoder.yudao.module.oa.dal.dataobject.productlist;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品清单 DO
 *
 * @author 管理员
 */
@TableName("oa_product_list")
@KeySequence("oa_product_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDO extends BaseDO {

    /**
     * 清单
     */
    @TableId
    private Long id;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 合同价/单价
     */
    private BigDecimal salePrice;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 产品数量
     */
    private Integer amount;

}
