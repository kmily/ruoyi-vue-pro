package cn.iocoder.yudao.module.haoka.dal.dataobject.product;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品限制条件 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_product_limit")
@KeySequence("haoka_product_limit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLimitDO extends BaseDO {

    /**
     * 产品类型ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 产品类型名称
     */
    private String name;
    /**
     * 是否使用只发货地址
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean useOnlySendArea;
    /**
     * 是否使用不发货地址
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean useNotSendArea;
    /**
     * 是否使用身份证限制
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean useCardLimit;
    /**
     * 最大年龄限制
     */
    private Integer ageMax;
    /**
     * 最小年龄限制
     */
    private Integer ageMin;
    /**
     * 单人开卡数量限制
     */
    private Integer personCardQuantityLimit;
    /**
     * 检测周期(月)
     */
    private Integer detectionCycle;
    /**
     * 部门ID
     */
    private Long deptId;

}
