package cn.iocoder.yudao.module.db.dal.dataobject.material;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料 DO
 *
 * @author Arlen
 */
@TableName("db_material")
@KeySequence("db_material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDO extends BaseDO {

    /**
     * 组织ID
     */
    private String orgId;
    /**
     * ERP物料编码
     */
    private String erpMtrlId;
    /**
     * 规格
     */
    private String model;
    /**
     * 仓库ID
     */
    private String storeId;
    /**
     * 储位ID
     */
    private String storelocId;
    /**
     * 最低安全库存
     */
    private BigDecimal qtySkMin;
    /**
     * 最高安全库存
     */
    private BigDecimal qtySkMax;
    /**
     * 预警库存
     */
    private BigDecimal qtySkWarn;
    /**
     * 备注
     */
    private String rem;
    /**
     * 状态
     */
    private String status;
    /**
     * 审核时间
     */
    private LocalDateTime checkTime;
    /**
     * 单位内码
     */
    private String unitId;
    /**
     * 审核人
     */
    private String checker;
    /**
     * 唯一ID
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 物料类型
     *
     * 枚举 {@link TODO db_mtrl_type 对应的类}
     */
    private String type;
    /**
     * ABC分类
     *
     * 枚举 {@link TODO db_mtrl_abc 对应的类}
     */
    private String abc;

}