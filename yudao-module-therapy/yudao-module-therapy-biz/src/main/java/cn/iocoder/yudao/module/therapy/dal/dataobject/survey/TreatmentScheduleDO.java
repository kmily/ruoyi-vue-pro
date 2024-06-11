package cn.iocoder.yudao.module.therapy.dal.dataobject.survey;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
/**
 * 患者日程 DO
 *
 * @author CBI系统管理员
 */
@TableName("hlgyy_treatment_schedule")
@KeySequence("hlgyy_treatment_schedule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentScheduleDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 患者id
     */
    private Long userId;
    /**
     * 名称
     */
    private String name;
    /**
     * 预计能完成的概率
     */
    private Integer estimateCompletedRate;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 状态
     * @like SignState
     */
    private Integer state;
    /**
     * 备注
     */
    private String remark;
    /**
     * 未完成原因
     */
    private String unfinishedCause;
    /**
     * 克服办法
     */
    private String solution;
    /**
     * 活动前评分
     */
    private Integer beforeScore;
    /**
     * 活动后评分
     */
    private Integer afterScore;

    /**
     * 签到时间
     */
    private LocalDateTime signInTime;

}