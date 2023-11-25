package cn.iocoder.yudao.module.wms.dal.dataobject.ro;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 收料单物料明细 DO
 *
 * @author Arlen
 */
@TableName("wms_ro_mtrl")
@KeySequence("wms_ro_mtrl_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoMtrlDO extends BaseDO {

    /**
     * 收料单物料明细ID
     */
    @TableId(type = IdType.INPUT)
    private String roMtrlId;
    /**
     * 收料单ID
     */
    private String roId;
    /**
     * 行号
     */
    private Integer roSeq;
    /**
     * 物料ID
     */
    private String mtrlId;
    /**
     * 仓库ID
     */
    private String storeId;
    /**
     * 基本单位
     */
    private String unitId;
    /**
     * 基本数量
     */
    private Long qtyB;
    /**
     * 单位
     */
    private String unitIdM;
    /**
     * 物料数量
     */
    private Long qtyM;
    /**
     * 赠品数量
     */
    private Long qtyG;
    /**
     * 已收数量
     */
    private Long qtyRo;
    /**
     * 收料状态
     */
    private String mtrlRoStatus;
    /**
     * 送检数量
     */
    private Long qtyQc;
    /**
     * 送检状态
     */
    private String mtrlQcStatus;
    /**
     * 加急
     */
    private String isUrgent;

}