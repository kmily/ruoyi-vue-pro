package cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品对接上游配置 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_superior_product_config")
@KeySequence("haoka_superior_product_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuperiorProductConfigDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * ID
     */
    private Long haokaSuperiorApiId;
    /**
     * ID
     */
    private Long haokaProductId;
    /**
     * 是否已配置
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean isConfined;
    /**
     * 值
     */
    private String config;
    /**
     * 是否必填
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean required;
    /**
     * 说明
     */
    private String remarks;
    /**
     * 部门ID
     */
    private Long deptId;

}