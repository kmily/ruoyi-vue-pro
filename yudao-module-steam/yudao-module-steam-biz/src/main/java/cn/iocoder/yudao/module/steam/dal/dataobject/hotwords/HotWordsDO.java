package cn.iocoder.yudao.module.steam.dal.dataobject.hotwords;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 热词搜索 DO
 *
 * @author 管理员
 */
@TableName("steam_hot_words")
@KeySequence("steam_hot_words_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotWordsDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * 热词
     */
    private String hotWords;
    /**
     * 实际昵称
     */
    private String marketName;

}