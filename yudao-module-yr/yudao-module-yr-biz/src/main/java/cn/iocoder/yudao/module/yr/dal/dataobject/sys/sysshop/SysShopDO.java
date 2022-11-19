package cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 店面 DO
 *
 * @author 芋道源码
 */
@TableName("qg_sys_shop")
@KeySequence("qg_sys_shop_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysShopDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 店面名称
     */
    private String shopName;
    /**
     * 店面城市
     */
    private String shopCity;
    /**
     * 店面地址
     */
    private String shopAddress;
    /**
     * 门牌号
     */
    private String shopAddressNum;
    /**
     * 店面分组
     */
    private String shopGroup;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

}
