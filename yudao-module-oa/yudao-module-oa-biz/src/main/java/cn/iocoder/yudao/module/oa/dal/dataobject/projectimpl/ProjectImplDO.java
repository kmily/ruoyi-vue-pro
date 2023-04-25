package cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 工程实施列 DO
 *
 * @author 管理员
 */
@TableName("oa_project_impl")
@KeySequence("oa_project_impl_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImplDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 实施范围
     *
     * 枚举 {@link TODO oa_contract_impl_scope 对应的类}
     */
    private String implType;
    /**
     * 实施内容
     */
    private String implContent;
    /**
     * 备注
     */
    private String remark;

}
