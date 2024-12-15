package cn.iocoder.yudao.module.haoka.dal.dataobject.product;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品身份证限制 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_product_limit_card")
@KeySequence("haoka_product_limit_card_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLimitCardDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 产品限制ID
     */
    private Long haokaProductLimitId;
    /**
     * 身份证号前4或6位
     */
    private Integer cardNum;
    /**
     * 部门ID
     */
    private Long deptId;

}