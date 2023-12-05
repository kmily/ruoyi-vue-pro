package cn.iocoder.yudao.module.crm.dal.dataobject.permission;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.crm.enums.common.CrmBizTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * CRM 数据权限扩展 DO
 *
 * @author HUIHUI
 */
@TableName("crm_permission_extended")
@KeySequence("crm_permission_extended_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmPermissionExtendedDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 上级数据类型，比如说客户
     *
     * 枚举 {@link CrmBizTypeEnum}
     */
    private Integer parentBizType;
    /**
     * 上级数据对应的 ID
     *
     * 关联 {@link CrmBizTypeEnum} 对应模块 DO 的 id 字段
     */
    private Long parentBizId;

    /**
     * 下级数据类型，比如联系人
     *
     * 枚举 {@link CrmBizTypeEnum}
     */
    private Integer subBizType;
    /**
     * 下级数据对应的 ID
     *
     * 关联 {@link CrmBizTypeEnum} 对应模块 DO 的 id 字段
     */
    private Long subBizId;

}
