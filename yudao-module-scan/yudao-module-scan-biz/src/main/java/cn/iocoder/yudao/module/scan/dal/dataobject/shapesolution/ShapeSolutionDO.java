package cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 体型解决方案 DO
 *
 * @author lyz
 */
@TableName("scan_shape_solution")
@KeySequence("scan_shape_solution_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShapeSolutionDO extends BaseDO {

    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     *
     * 枚举 {@link TODO scan_solution_suggest_type 对应的类}
     */
    private Integer type;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 内容
     */
    private String content;
    /**
     * 体型id
     */
    private Integer shapeShapeId;
    /**
     * 排序
     */
    private Integer sort;

}
