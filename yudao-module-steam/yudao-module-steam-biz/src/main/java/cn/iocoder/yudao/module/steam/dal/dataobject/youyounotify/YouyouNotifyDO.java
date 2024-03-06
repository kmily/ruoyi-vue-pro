package cn.iocoder.yudao.module.steam.dal.dataobject.youyounotify;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

/**
 * steam订单 DO
 *
 * @author 管理员
 */
@TableName(value = "steam_youyou_notify",autoResultMap = true)
@KeySequence("steam_youyou_notify_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouyouNotifyDO extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 消息幂等编号。
     */
    private String messageNo;
    /**
     * 消息体
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private NotifyReq msg;

}