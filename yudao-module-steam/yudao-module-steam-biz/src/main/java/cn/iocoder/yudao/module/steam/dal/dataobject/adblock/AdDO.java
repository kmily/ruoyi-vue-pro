package cn.iocoder.yudao.module.steam.dal.dataobject.ad;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 广告 DO
 *
 * @author 管理员
 */
@TableName("steam_ad")
@KeySequence("steam_ad_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdDO extends BaseDO {

    /**
     * Primary Key
     */
    @TableId
    private Long id;
    /**
     * 文字描述
     */
    private String text;
    /**
     * 跳转地址
     */
    private String url;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否启用
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * adID
     */
    private Long blockId;
    /**
     * 广告名称
     */
    private String adName;

}