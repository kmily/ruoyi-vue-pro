package cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 线下人工充值 DO
 *
 * @author 管理员
 */
@TableName("steam_offline_rechange")
@KeySequence("steam_offline_rechange_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfflineRechangeDO extends BaseDO {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 充值金额
     */
    private Integer amount;
    /**
     * 备注
     */
    private String comment;
    /**
     * Primary Key
     */
    @TableId
    private Long id;
    /**
     * 备注
     */
    private String mobile;
}