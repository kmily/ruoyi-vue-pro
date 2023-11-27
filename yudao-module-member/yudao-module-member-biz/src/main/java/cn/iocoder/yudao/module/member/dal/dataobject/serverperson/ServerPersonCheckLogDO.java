package cn.iocoder.yudao.module.member.dal.dataobject.serverperson;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 被户人审核记录 DO
 *
 * @author 芋道源码
 */
@TableName("member_server_person_check_log")
@KeySequence("member_server_person_check_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerPersonCheckLogDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 被户人编号
     */
    private Long personId;
    /**
     * 审核人
     */
    private String checkName;
    /**
     * 审核时间
     */
    private LocalDateTime checkTime;
    /**
     * 审核意见
     */
    private String opinion;
    /**
     * 审核状态
     */
    private String checkStatus;

}
