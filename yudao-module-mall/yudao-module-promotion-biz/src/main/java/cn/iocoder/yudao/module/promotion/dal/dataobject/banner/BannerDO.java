package cn.iocoder.yudao.module.promotion.dal.dataobject.banner;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * Banner DO
 *
 * @author 芋道源码
 */
@TableName("promotion_banner")
@KeySequence("promotion_banner_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDO extends BaseDO {

    /**
     * Banner 编号
     */
    @TableId
    private Long id;
    /**
     * Banner 标题
     */
    private String title;
    /**
     * 图片 URL
     */
    private String picUrl;
    /**
     * 跳转地址
     */
    private String url;
    /**
     * 活动状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Byte status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 位置
     *
     * 枚举 {@link TODO promotion_banner_position 对应的类}
     */
    private Byte position;
    /**
     * 描述
     */
    private String memo;
    /**
     * Banner 点击次数
     */
    private Integer browseCount;

}
