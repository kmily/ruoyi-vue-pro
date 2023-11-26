package cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04;

import cn.iocoder.yudao.framework.easytrans.db.TransBaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 学生 DO
 *
 * @author 芋道源码
 */
@TableName(value = "infra_demo04_student", autoResultMap = true)
@KeySequence("infra_demo04_student_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demo04StudentDO extends TransBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 名字
     */
    private String name;
    /**
     * 性别
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Integer sex;
    /**
     * 出生日期
     */
    private LocalDateTime birthday;
    /**
     * 简介
     */
    private String description;

    /**
     * 学生班级，关联 {@link Demo04GradeDO#getId()}
     */
    private Long gradeId;
    /**
     * 学生课程, {@link Demo04CourseDO#getId()}
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> courseIds;

}