package cn.iocoder.yudao.module.member.dal.dataobject.visitpage;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 页面访问数据 DO
 *
 * @author 和尘同光
 */
@TableName("member_visit_page")
@KeySequence("member_visit_page_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitPageDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 页面标记
     */
    private String pageKey;

    /**
     * 访问页面
     */
    private String pageName;
    /**
     * 进入时间
     */
    private LocalDateTime startTime;
    /**
     * 离开时间
     */
    private LocalDateTime endTime;
    /**
     * 访问用时单位毫秒
     */
    private Long useTime;

}
