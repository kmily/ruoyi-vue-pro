package cn.iocoder.yudao.module.im.dal.dataobject.groupmember;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 群成员 DO
 *
 * @author 芋道源码
 */
@TableName("im_group_member")
@KeySequence("im_group_member_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 群 id
     */
    private Long groupId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 组内显示名称
     */
    private String aliasName;
    /**
     * 备注
     */
    private String remark;

}