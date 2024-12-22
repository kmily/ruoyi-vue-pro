package cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单来源-直播间配置 DO
 *
 * @author xiongxiong
 */
@TableName("haoka_order_source_live")
@KeySequence("haoka_order_source_live_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSourceLiveDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 直播间ID
     */
    private Long authorId;
    /**
     * 来源ID
     */
    private Long sourceId;
    /**
     * 来源，可选
     */
    private String source;
    /**
     * 所属店铺ID
     */
    private Long shopId;
    /**
     * 用户ID，可选
     */
    private Long userId;
    /**
     * 团队ID，可选
     */
    private Long deptId;
    /**
     * 团队名称，可选
     */
    private String teamName;
    /**
     * 重命名后的名称，可选，用于搜索
     */
    private String nickName;

}