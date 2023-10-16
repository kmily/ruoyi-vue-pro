package cn.iocoder.yudao.module.member.dal.dataobject.test;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 会员标签 DO
 *
 * @author 芋道源码
 */
@TableName("member_test")
@KeySequence("member_test_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 标签名称
     */
    private String name;

}
