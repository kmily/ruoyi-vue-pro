package cn.iocoder.yudao.module.member.dal.dataobject.family;

import cn.iocoder.yudao.framework.mybatis.core.type.ListJsonTypeHandler;
import cn.iocoder.yudao.framework.mybatis.core.type.StringListTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户家庭 DO
 *
 * @author 芋道源码
 */
@TableName(value = "member_family", autoResultMap = true)
@KeySequence("member_family_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDO extends BaseDO {

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
     * 家庭名称
     */
    private String name;

    /**
     * 告警手机号
     */
    @TableField(typeHandler = ListJsonTypeHandler.class)
    private List<String> phones;

    public List<String> getPhones() {
        return phones == null? new ArrayList<>(): phones;
    }
}
