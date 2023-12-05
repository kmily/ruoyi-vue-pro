package cn.iocoder.yudao.module.crm.service.permission.bo.extended;

import cn.iocoder.yudao.module.crm.enums.common.CrmBizTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * crm 数据权限扩展 Create Req BO
 *
 * @author HUIHUI
 */
@Data
public class CrmPermissionExtendedCreateReqBO {

    /**
     * 上级数据类型，比如说客户
     *
     * 枚举 {@link CrmBizTypeEnum}
     */
    @NotNull(message = "上级数据类型不能为空")
    private Integer parentBizType;
    /**
     * 上级数据对应的 ID
     *
     * 关联 {@link CrmBizTypeEnum} 对应模块 DO 的 id 字段
     */
    @NotNull(message = "上级数据对应的的编号不能为空")
    private Long parentBizId;

    /**
     * 下级数据类型，比如联系人
     *
     * 枚举 {@link CrmBizTypeEnum}
     */
    @NotNull(message = "下级数据对应的编号不能为空")
    private Integer subBizType;
    /**
     * 下级数据对应的 ID
     *
     * 关联 {@link CrmBizTypeEnum} 对应模块 DO 的 id 字段
     */
    @NotNull(message = "下级数据对应的编号不能为空")
    private Long subBizId;

}
