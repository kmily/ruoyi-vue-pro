package cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 悠悠商品模板 DO
 *
 * @author 管理员
 */
@TableName("steam_youyou_template")
@KeySequence("steam_youyou_template_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouyouTemplateDO extends BaseDO {

    /**
     * 武器全称
     */
    private String name;
    /**
     * 武器英文全称
     */
    private String hashName;
    /**
     * 类型编号
     */
    private Integer typeId;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型英文名称
     */
    private String typeHashName;
    /**
     * 武器编号
     */
    private Integer weaponId;
    /**
     * 武器名称
     */
    private String weaponName;
    /**
     * 武器英文名称
     */
    private String weaponHashName;
    /**
     * 主键ID
     */
    @TableId
    private Integer id;
    /**
     * 模板ID
     */
    private Integer templateId;

}