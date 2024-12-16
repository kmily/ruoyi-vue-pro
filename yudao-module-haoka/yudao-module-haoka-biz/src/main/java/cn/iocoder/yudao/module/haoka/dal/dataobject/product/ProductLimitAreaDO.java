package cn.iocoder.yudao.module.haoka.dal.dataobject.product;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品区域配置 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_product_limit_area")
@KeySequence("haoka_product_limit_area_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLimitAreaDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 产品限制ID
     */
    private Long haokaProductLimitId;
    /**
     * 地区
     */
    private Integer addressCode;
    /**
     * 地区
     */
    private String addressName;
    /**
     * 是否允许
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean allowed;
    /**
     * 部门ID
     */
    private Long deptId;

}
