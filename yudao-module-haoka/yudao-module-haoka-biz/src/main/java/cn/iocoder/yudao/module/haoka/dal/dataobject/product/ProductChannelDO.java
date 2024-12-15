package cn.iocoder.yudao.module.haoka.dal.dataobject.product;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品的渠道 DO
 *
 * @author 芋道源码
 */
@TableName("haoka_product_channel")
@KeySequence("haoka_product_channel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductChannelDO extends BaseDO {

    /**
     * 产品类型ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 产品类型名称
     */
    private String name;
    /**
     * 部门ID
     */
    private Long deptId;

}
