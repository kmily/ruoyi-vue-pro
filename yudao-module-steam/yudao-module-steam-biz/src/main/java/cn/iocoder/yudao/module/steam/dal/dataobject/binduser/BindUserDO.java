package cn.iocoder.yudao.module.steam.dal.dataobject.binduser;

import cn.iocoder.yudao.framework.mybatis.core.type.EncryptTypeHandler;
import cn.iocoder.yudao.module.steam.service.steam.SteamMaFile;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 *  steam用户绑定 DO
 *
 * @author 芋道源码
 */
@TableName(value = "steam_bind_user",autoResultMap = true)
@KeySequence("steam_bind_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BindUserDO extends BaseDO {

    /**
     * steam名称
     */
    private String steamName;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * SteamId
     */
    private String steamId;
    /**
     * 交易链接
     */
    private String tradeUrl;
    /**
     * API KEY
     */
    private String apiKey;
    /**
     * 备注
     */
    private String remark;
    /**
     * steam密码
     */
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String steamPassword;
    /**
     * maFile文件
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private SteamMaFile maFile;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 登录过后的cookie
     */
    private String loginCookie;
    /**
     * 地址池id
     */
    private Long addressId;
    /**
     * 地址池id
     */
    private String avatarUrl;
    /**
     * 是否临时账号
     */
    private Boolean isTempAccount;

}