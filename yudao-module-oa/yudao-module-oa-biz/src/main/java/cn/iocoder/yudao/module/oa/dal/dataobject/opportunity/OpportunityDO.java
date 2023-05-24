package cn.iocoder.yudao.module.oa.dal.dataobject.opportunity;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 商机 DO
 *
 * @author 管理员
 */
@TableName("oa_opportunity")
@KeySequence("oa_opportunity_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 商机标题
     */
    private String businessTitle;
    /**
     * 商机详情
     */
    private String detail;
    /**
     * 上报时间
     */
    private LocalDateTime reportTime;
    /**
     * 跟进用户id
     */
    private Long followUserId;
    /**
     * 商机状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

}
