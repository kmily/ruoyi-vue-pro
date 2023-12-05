package cn.iocoder.yudao.module.crm.dal.dataobject.customer;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 客户限制配置表
 * @author Joey
 */
@TableName(value = "crm_customer_limit_config", autoResultMap = true)
@KeySequence("crm_customer_limit_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCustomerLimitConfigDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     *规则类型 1: 拥有客户数限制，2:锁定客户数限制
     */
    private Integer type;

    /**
     * 规则适用人群
     */
    private String userIds;

    /**
     * 规则适用部门
     */
    private String deptIds;

    /**
     * 数量上限
     */
    private Integer maxCount;

    /**
     * 成交客户是否占有拥有客户数(当 type = 1 时)
     */
    private Long dealCountEnabled;


}
