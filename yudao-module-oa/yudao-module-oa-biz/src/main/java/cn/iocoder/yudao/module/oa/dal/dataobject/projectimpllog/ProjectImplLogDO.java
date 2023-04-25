package cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog;

import com.sun.xml.bind.v2.TODO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 工程日志列表 DO
 *
 * @author 管理员
 */
@TableName("oa_project_impl_log")
@KeySequence("oa_project_impl_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImplLogDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 内容
     */
    private String logContent;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 工程进度
     *
     * 枚举 {@link TODO oa_contract_impl_state 对应的类}
     */
    private String implStatus;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;

}
