package cn.iocoder.yudao.module.steam.dal.dataobject.inv;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户库存储 DO
 *
 * @author 芋道源码
 */
@TableName("steam_inv")
@KeySequence("steam_inv_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvDO extends BaseDO {

    /**
     * assetid
     */
    private String assetid;
    /**
     * classid
     */
    private String classid;
    /**
     * instanceid
     */
    private String instanceid;
    /**
     * amount
     */
    private String amount;
    /**
     * steamId
     */
    private String steamId;
    /**
     * 启用
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean status;
    /**
     * 出售价格单价分
     */
    private Integer price;
    /**
     * 发货状态
     */
    private Integer transferStatus;
    /**
     * Primary Key
     */
    @TableId
    private Long id;
    /**
     * csgoid
     */
    private Integer appid;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 绑定用户ID
     */
    private Long bindUserId;

}