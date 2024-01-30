package cn.iocoder.yudao.module.steam.dal.dataobject.binduser;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 *  steam用户绑定 DO
 *
 * @author 芋道源码
 */
@TableName("steam_bind_user")
@KeySequence("steam_bind_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BindUserDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Integer id;
    /**
     * steam名称
     */
    private String steamName;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * SteamId
     */
    private String steamId;
    /**
     * 交易链接
     */
    private String tradeUrl;
    /**
     * API KEY
     */
    private String apiKey;
    /**
     * 备注
     */
    private String remark;

}