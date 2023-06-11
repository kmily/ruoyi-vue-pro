package cn.iocoder.yudao.module.member.dal.dataobject.MemberUser;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户 DO
 *
 * @author 芋道源码
 */
@TableName("member_user")
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUserDO extends TenantBaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户真实名称
     */
    private String realName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 注册 IP
     */
    private String registerIp;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 支付密码
     */
    private String payPassword;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 用户组id
     */
    private Long userGroupId;
    /**
     * 积分
     */
    private Long point;
    /**
     * 推荐人
     */
    private Long referrer;
    /**
     * 性别
     */
    private Byte gender;
    /**
     * 标签
     */
    private Long labelId;

}
