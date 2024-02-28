package cn.iocoder.yudao.module.steam.dal.dataobject.selling;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 在售饰品 DO
 *
 * @author 管理员
 */
@TableName("steam_selling")
@KeySequence("steam_selling_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellingDO extends BaseDO {

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
     * 资产id(饰品唯一)
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
     * 状态
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum 对应的类}
     */
    private Integer status;
    /**
     * 出售价格单价分
     */
    private Integer price;
    /**
     * 平台用户ID
     */
    private Long userId;
    /**
     * 用户类型(前后台用户)
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer userType;
    /**
     * 绑定用户ID
     */
    private Long bindUserId;
    /**
     * contextid
     */
    private String contextid;
    /**
     * inv_desc_id
     */
    private Long invDescId;
    /**
     * 发货状态(0代表未出售，1代表出售中，2代表已出售 )
     * 枚举 {@link cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum 对应的类}
     */
    private Integer transferStatus;

}