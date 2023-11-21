package cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage;

import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 机构套餐 DO
 *
 * @author 芋道源码
 */
@TableName(value = "system_organization_package", autoResultMap = true)
@KeySequence("system_organization_package_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationPackageDO extends BaseDO {

    /**
     * 套餐编号
     */
    @TableId
    private Long id;
    /**
     * 套餐名
     */
    private String name;
    /**
     * 租户状态（0正常 1停用）
     */
    private Byte status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 关联的菜单编号
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> menuIds;
    /**
     * 是否为默认
     */
    private Boolean isDefault;

}
