package cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户查询明细 DO
 *
 * @author 管理员
 */
@TableName("steam_youyou_details")
@KeySequence("steam_youyou_details_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouyouDetailsDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * 通过申请获取的AppKey
     */
    private String appkey;
    /**
     * 时间戳
     */
    private LocalDateTime timestamp;
    /**
     * API输入参数签名结果
     */
    private String sign;
    /**
     * 明细类型，1=订单明细，2=资金流水
     *
     * 枚举 {@link //TODO infra_config_type 对应的类}
     */
    private Integer dataType;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 申请标识
     */
    private String applyCode;
    /**
     * 查询明细结果返回的url
     */
    private String data;

}