package cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04;

import cn.iocoder.yudao.framework.easytrans.db.TransBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 学生课程 DO
 *
 * @author 芋道源码
 */
@TableName("infra_demo04_course")
@KeySequence("infra_demo04_course_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demo04CourseDO extends TransBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 名字
     */
    private String name;

}