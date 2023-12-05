package cn.iocoder.yudao.module.crm.dal.mysql.permission;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.crm.dal.dataobject.permission.CrmPermissionExtendedDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * crm 数据权限扩展 mapper
 *
 * @author HUIHUI
 */
@Mapper
public interface CrmPermissionExtendedMapper extends BaseMapperX<CrmPermissionExtendedDO> {

    default CrmPermissionExtendedDO selectPermissionExtendedBySubBiz(Integer subBizType, Long subBizId) {
        return selectOne(new LambdaQueryWrapperX<CrmPermissionExtendedDO>()
                .eq(CrmPermissionExtendedDO::getSubBizType, subBizType)
                .eq(CrmPermissionExtendedDO::getSubBizId, subBizId));
    }

    default List<CrmPermissionExtendedDO> selectPermissionExtendedBySubBiz(Integer subBizType, Collection<Long> subBizIds) {
        return selectList(new LambdaQueryWrapperX<CrmPermissionExtendedDO>()
                .eq(CrmPermissionExtendedDO::getSubBizType, subBizType)
                .in(CrmPermissionExtendedDO::getSubBizId, subBizIds));
    }

}
