package cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 业务字典tree DO
 *
 * @author alex
 */
@TableName("qg_sys_dict_tree")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QgDictTreeDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 显示顺序
     */
    private Integer level;
    /**
     * 类型
     */
    private String type;
    /**
     * 状态（0正常 1停用）
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * 只读（0正常 1只读）
     *
     *
     */
    private Integer isRead;

}
