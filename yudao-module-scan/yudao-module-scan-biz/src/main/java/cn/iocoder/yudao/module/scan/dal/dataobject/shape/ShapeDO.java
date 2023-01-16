package cn.iocoder.yudao.module.scan.dal.dataobject.shape;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 体型 DO
 *
 * @author lyz
 */
@TableName("scan_shape")
@KeySequence("scan_shape_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShapeDO extends BaseDO {

    /**
     * 体型名称
     */
    private String shapeName;
    /**
     * 体型值
     */
    private String shapeValue;
    /**
     * id
     */
    @TableId
    private Long id;

}
