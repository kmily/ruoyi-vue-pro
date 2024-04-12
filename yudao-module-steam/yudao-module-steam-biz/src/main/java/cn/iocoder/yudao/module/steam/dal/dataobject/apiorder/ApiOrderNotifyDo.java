package cn.iocoder.yudao.module.steam.dal.dataobject.apiorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.data.annotation.Transient;

/**
 * 订单扩展 DO
 *
 * @author 管理员
 */
@TableName("steam_api_order_notify")
@KeySequence("steam_api_order_notify_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiOrderNotifyDo extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 原始消息内容
     */
    private String msg;
    /**
     * 消息ID
     */
    private String messageNo;
    /**
     * 是否已经推送到远程
     */
    private Boolean pushRemote;
    /**
     * 远程地址
     */
    private String pushRemoteUrl;
    /**
     * 远程返回
     */
    private String pushRemoteResult;
    /**
     * 购买平台代码
     */
    private String platCode;
    /**
     * 签名
     */
    private transient String sign;

}