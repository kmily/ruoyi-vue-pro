package cn.iocoder.yudao.module.wms.dal.dataobject.ro;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 收料单LPN明细 DO
 *
 * @author Arlen
 */
@TableName("wms_ro_lpn")
@KeySequence("wms_ro_lpn_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoLpnDO extends BaseDO {

    /**
     * 收料LPN明细ID
     */
    private String roLpnId;
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
     * LPNID
     */
    private String lpnId;
    /**
     * SN
     */
    private String sn;
    /**
     * LPN
     */
    private String lpn;
    /**
     * LPN数量
     */
    private Double lpnQty;
    /**
     * 批次
     */
    private String batchNo;
    /**
     * 收料时间
     */
    private LocalDateTime roDate;
    /**
     * 收料人
     */
    private String roBy;

}