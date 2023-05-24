package cn.iocoder.yudao.module.oa.dal.dataobject.quarterlyassessment;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 季度考核 DO
 *
 * @author 管理员
 */
@TableName("oa_quarterly_assessment")
@KeySequence("oa_quarterly_assessment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuarterlyAssessmentDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 季度
     */
    private String quarter;
    /**
     * 年份
     */
    private Integer quarterYear;
    /**
     * 本季度工作内容
     */
    private String workContent;
    /**
     * 关键工作
     */
    private String criticalWork;
    /**
     * 出差记录
     */
    private String businessTrip;
    /**
     * 下季度工作安排
     */
    private String nextQuarterWork;
    /**
     * 上传附件
     */
    private String appendFiles;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

}
