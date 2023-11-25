package cn.iocoder.yudao.module.wms.dal.dataobject.ro;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 收料单 DO
 *
 * @author Arlen
 */
@TableName("wms_ro")
@KeySequence("wms_ro_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoDO extends BaseDO {

    /**
     * 收料单ID
     */
    @TableId(type = IdType.INPUT)
    private String roId;
    /**
     * 收料单号
     */
    private String roCode;
    /**
     * 收料类型
     *
     * 枚举 {@link TODO wms_ro_type 对应的类}
     */
    private String roType;
    /**
     * 收料状态
     *
     * 枚举 {@link TODO wms_ro_status 对应的类}
     */
    private String roStatus;
    /**
     * 加急
     */
    private String isUrgent;
    /**
     * ASN
     */
    private String asn;
    /**
     * 供应商ID
     */
    private String supId;
    /**
     * 客户ID
     */
    private String custId;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 员工ID
     */
    private String empId;
    /**
     * 来源
     *
     * 枚举 {@link TODO com_data_src 对应的类}
     */
    private String rosrcType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审核时间
     */
    private LocalDateTime checkTime;
    /**
     * 关闭
     */
    private String closed;
    /**
     * 组织ID
     */
    private Long orgId;
    /**
     * 审核人ID
     */
    private String checker;
    /**
     * 关闭人
     */
    private String closer;

}