package cn.iocoder.yudao.module.member.dal.dataobject.homepage;

import cn.iocoder.yudao.framework.mybatis.core.type.ListJsonTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 首页配置 DO
 *
 * @author 芋道源码
 */
@TableName(value = "member_home_page", autoResultMap = true)
@KeySequence("member_home_page_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePageDO extends BaseDO {

    /**
     * 自增编号
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 家庭ID
     */
    private Long familyId;
    /**
     * 房间名称
     */
    private String name;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 数据类型 0-睡眠,1-如厕,2-跌倒,3-离/回家
     */
    private Byte type;
    /**
     * 状态（0正常 1停用）
     */
    private Byte status;
    /**
     * 排序
     */
    private Byte sort;
    /**
     * 模式，0-系统, 1-自定义
     */
    private Byte mold;

    /**
     * 绑定设备多个 ","分割
     */
    @TableField(typeHandler = ListJsonTypeHandler.class)
    private List<Map<String, Object>> devices;


}
