package cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 上游API接口SKU要求配置 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_superior_api_sku_config")
@KeySequence("haoka_superior_api_sku_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuperiorApiSkuConfigDO extends BaseDO {

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
     * 标识
     */
    private String code;
    /**
     * 名字
     */
    private String name;
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
     * 输入类型
     *
     * 枚举 {@link TODO haoka_superior_api_input_type 对应的类}
     */
    private Integer inputType;
    /**
     * 选项(逗号,分割)
     */
    private String inputSelectValues;
    /**
     * 部门ID
     */
    private Long deptId;

}