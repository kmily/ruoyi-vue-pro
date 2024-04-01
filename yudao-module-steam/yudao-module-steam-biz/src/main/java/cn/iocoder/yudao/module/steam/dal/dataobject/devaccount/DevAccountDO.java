package cn.iocoder.yudao.module.steam.dal.dataobject.devaccount;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import javax.validation.constraints.NotEmpty;

/**
 * 开放平台用户 DO
 *
 * @author 芋道源码
 */
@TableName("steam_dev_account")
@KeySequence("steam_dev_account_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevAccountDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * api用户名
     */
    private String userName;
    /**
     * 公匙
     */
    private String apiPublicKey;
    /**
     * 回调地址
     */
    private String callbackUrl;
    /**
     * 状态
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum 对应的类}
     */
    private Integer status;
    /**
     * 用户类型
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.UserTypeEnum 对应的类}
     */
    private Integer userType;
    /**
     * 真实姓名
     */
    private String trueName;
    /**
     * 身份证号
     */
    private String idNum;
    /**
     * 申请理由
     */
    private String applyReason;
    /**
     * 回调公匙
     */
    private String callbackPublicKey;
    /**
     * 回调地址
     */
    private String callbackPrivateKey;

}