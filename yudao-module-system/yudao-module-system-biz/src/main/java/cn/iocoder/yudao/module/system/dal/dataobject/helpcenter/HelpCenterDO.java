package cn.iocoder.yudao.module.system.dal.dataobject.helpcenter;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 常见问题 DO
 *
 * @author 芋道源码
 */
@TableName("system_help_center")
@KeySequence("system_help_center_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelpCenterDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 帮助类型
     */
    private Byte type;
    /**
     * 标题
     */
    private String title;
    /**
     * 问题描述
     */
    private String description;
    /**
     * 状态（0正常 1禁用）
     */
    private Byte status;

}
