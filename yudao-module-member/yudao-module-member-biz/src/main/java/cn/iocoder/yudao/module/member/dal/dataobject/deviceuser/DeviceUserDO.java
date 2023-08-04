package cn.iocoder.yudao.module.member.dal.dataobject.deviceuser;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备和用户绑定 DO
 *
 * @author 芋道源码
 */
@TableName("member_device_user")
@KeySequence("member_device_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUserDO extends BaseDO {

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
     * 设备ID
     */
    private Long deviceId;
    /**
     * 家庭ID
     */
    private Long familyId;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 自定义设备名称
     */
    private String customName;


}
