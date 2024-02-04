package cn.iocoder.yudao.module.steam.dal.dataobject.inv;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * steam用户库存储 DO
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
     * Primary Key
     */
    @TableId
    private Integer id;
    /**
     * appid
     */
    private Integer appid;
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
     */
    private String status;

}