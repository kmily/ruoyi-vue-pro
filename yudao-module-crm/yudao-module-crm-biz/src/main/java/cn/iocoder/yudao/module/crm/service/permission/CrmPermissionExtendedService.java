package cn.iocoder.yudao.module.crm.service.permission;

import cn.iocoder.yudao.module.crm.dal.dataobject.permission.CrmPermissionExtendedDO;
import cn.iocoder.yudao.module.crm.service.permission.bo.extended.CrmPermissionExtendedCreateReqBO;

import java.util.Collection;
import java.util.List;

/**
 * crm 数据权限扩展 Service 接口
 *
 * @author HUIHUI
 */
public interface CrmPermissionExtendedService {

    /**
     * 创建数据权限扩展
     *
     * @param createReqBO 请求
     * @return 编号
     */
    Long createPermissionExtended(CrmPermissionExtendedCreateReqBO createReqBO);

    /**
     * 批量删除数据权限扩展
     * tip: 删除上级数据不需要时不需要删除数据扩展原则上上级也不需要知道有没有扩展
     *
     * @param subBizType 下级数据类型
     * @param subBizIds  下级数据编号
     */
    void deletePermissionExtendedBySubBizBatch(Integer subBizType, Collection<Long> subBizIds);

    /**
     * 获取数据权限扩展
     *
     * @param subBizType 下级数据类型
     * @param subBizId   下级数据对应的 ID
     * @return 数据权限扩展
     */
    List<CrmPermissionExtendedDO> getPermissionExtendedBySubBiz(Integer subBizType, Long subBizId);

}
