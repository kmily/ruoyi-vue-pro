package cn.iocoder.yudao.module.product.dal.dataobject.unit;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 计量单位 DO
 *
 * @author 芋道源码
 */
@TableName("product_unit")
@KeySequence("product_unit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 单位名称
     */
    private String name;

    /**
     * 开启状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
