package cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商机-跟进日志 DO
 *
 * @author 东海
 */
@TableName("oa_opportunity_follow_log")
@KeySequence("oa_opportunity_follow_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityFollowLogDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 商机id
     */
    private Long businessId;
    /**
     * 跟进日志内容
     */
    private String logContent;

}
