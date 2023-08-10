package cn.iocoder.yudao.module.radar.dal.dataobject.device;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备信息 DO
 *
 * @author 芋道源码
 */
@TableName("radar_device")
@KeySequence("radar_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDO extends TenantBaseDO {

    /**
     * 设备ID
     */
    @TableId
    private Long id;
    /**
     * 设备sn编号
     */
    private String sn;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备类别
     */
    private Integer type;
    /**
     * 部门状态（0正常 1停用）
     */
    private Byte status;

    /**
     * 保活时间
     */
    private LocalDateTime keepalive;

}
