package cn.iocoder.yudao.module.im.dal.dataobject.group;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * IM 群成员 DO
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
    // TODO @hao：groupId 和 userId 都写下关联字段哈
    /**
     * 群编号
     */
    private Long groupId;
    /**
     * 用户编号
     */
    private Long userId;
    // TODO @hao：nickname 和 avatar 是不是不用存储哈；
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