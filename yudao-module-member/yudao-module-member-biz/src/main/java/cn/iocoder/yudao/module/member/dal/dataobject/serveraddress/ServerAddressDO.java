package cn.iocoder.yudao.module.member.dal.dataobject.serveraddress;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 服务地址 DO
 *
 * @author 芋道源码
 */
@TableName("member_server_address")
@KeySequence("member_server_address_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerAddressDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 地区编码
     */
    private Long areaId;
    /**
     * 省市县/区
     */
    private String address;
    /**
     * 收件详细地址
     */
    private String detailAddress;
    /**
     * 是否默认
     */
    private Boolean defaultStatus;
    /**
     * 经纬度
     */
    private String coordinate;

    /**
     * 租户租户编号
     */
    private Long  tenantId;
}
