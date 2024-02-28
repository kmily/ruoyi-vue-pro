package cn.iocoder.yudao.module.steam.dal.dataobject.adblock;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 广告位 DO
 *
 * @author 管理员
 */
@TableName("steam_ad_block")
@KeySequence("steam_ad_block_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdBlockDO extends BaseDO {

    /**
     * Primary Key
     */
    @TableId
    private Long id;
    /**
     * 广告名称
     */
    private String adName;

}