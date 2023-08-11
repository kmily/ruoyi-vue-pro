package cn.iocoder.yudao.module.member.dal.dataobject.devicenotice;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备通知 DO
 *
 * @author 芋道源码
 */
@TableName("member_device_notice")
@KeySequence("member_device_notice_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceNoticeDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 家庭ID
     */
    private Long familyId;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 卡片名称
     */
    private String content;
    /**
     * 设备类型
     */
    private Byte type;
    /**
     * 状态（0未读 1已读）
     */
    private Byte status;

    /**
     * 发生时间
     */
    private LocalDateTime happenTime;

}
