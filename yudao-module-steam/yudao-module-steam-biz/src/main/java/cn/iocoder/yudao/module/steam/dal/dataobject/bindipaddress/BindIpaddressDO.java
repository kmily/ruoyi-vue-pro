package cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 绑定用户IP地址池 DO
 *
 * @author 管理员
 */
@TableName("steam_bind_ipaddress")
@KeySequence("steam_bind_ipaddress_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BindIpaddressDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 地址池id
     */
    private Long addressId;
    /**
     * ip地址
     */
    private String ipAddress;

}