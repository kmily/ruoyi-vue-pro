package cn.iocoder.yudao.module.steam.dal.dataobject.selitemset;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 收藏品选择 DO
 *
 * @author glzaboy
 */
@TableName("steam_sel_itemset")
@KeySequence("steam_sel_itemset_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelItemsetDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 父级编号
     */
    private Long parentId;
    /**
     * 英文名称
     */
    private String internalName;
    /**
     * 中文名称
     */
    private String localizedTagName;

}