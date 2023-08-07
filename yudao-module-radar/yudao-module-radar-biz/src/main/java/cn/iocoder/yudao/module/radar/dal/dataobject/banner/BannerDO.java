package cn.iocoder.yudao.module.radar.dal.dataobject.banner;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 雷达模块banner图 DO
 *
 * @author 芋道源码
 */
@TableName("radar_banner")
@KeySequence("radar_banner_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 跳转连接
     */
    private String url;
    /**
     * 图片连接
     */
    private String picUrl;
    /**
     * 排序
     */
    private Byte sort;
    /**
     * 部门状态（0正常 1停用）
     */
    private Byte status;
    /**
     * 备注
     */
    private String memo;

}
