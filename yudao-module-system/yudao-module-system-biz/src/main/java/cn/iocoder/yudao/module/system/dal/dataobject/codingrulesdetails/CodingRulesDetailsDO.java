package cn.iocoder.yudao.module.system.dal.dataobject.codingrulesdetails;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码规则明细 DO
 *
 * @author panjiabao
 */
@TableName("system_coding_rules_details")
@KeySequence("system_coding_rules_details_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingRulesDetailsDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编码规则头id
     */
    private String ruleId;
    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 类型 1-常量 2-日期 3-日流水号 4-月流水号 5-年流水号
     */
    private String type;
    /**
     * 设置值
     */
    private String value;
    /**
     * 长度
     */
    private Integer len;
    /**
     * 起始值
     */
    private Integer initial;
    /**
     * 步长
     */
    private Integer stepSize;
    /**
     * 补位符
     */
    private String fillKey;
    /**
     * 备注
     */
    private String remark;
}
