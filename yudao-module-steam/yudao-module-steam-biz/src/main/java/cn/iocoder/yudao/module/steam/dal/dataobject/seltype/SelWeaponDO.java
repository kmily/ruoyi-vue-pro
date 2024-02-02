package cn.iocoder.yudao.module.steam.dal.dataobject.seltype;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 武器选择 DO
 *
 * @author 芋道源码
 */
@TableName("steam_sel_weapon")
@KeySequence("steam_sel_weapon_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelWeaponDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 类型选择编号
     */
    private Long typeId;
    /**
     * 英文名字
     */
    private String internalName;
    /**
     * 中文名称
     */
    private String localizedTagName;

}