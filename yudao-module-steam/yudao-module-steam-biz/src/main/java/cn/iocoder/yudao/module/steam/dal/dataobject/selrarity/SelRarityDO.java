package cn.iocoder.yudao.module.steam.dal.dataobject.selrarity;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 品质选择 DO
 *
 * @author lgm
 */
@TableName("steam_sel_rarity")
@KeySequence("steam_sel_rarity_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelRarityDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 中文名称
     */
    private String localizedTagName;
    /**
     * 英文名称
     */
    private String internalName;
    /**
     * 色彩
     */
    private String color;

}