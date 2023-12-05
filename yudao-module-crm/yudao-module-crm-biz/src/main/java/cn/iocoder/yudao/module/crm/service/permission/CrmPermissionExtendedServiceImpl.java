package cn.iocoder.yudao.module.crm.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.crm.dal.dataobject.permission.CrmPermissionExtendedDO;
import cn.iocoder.yudao.module.crm.dal.mysql.permission.CrmPermissionExtendedMapper;
import cn.iocoder.yudao.module.crm.service.permission.bo.extended.CrmPermissionExtendedCreateReqBO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * CRM 数据权限扩展 Service 接口实现类
 *
 * @author HUIHUI
 */
@Service
@Validated
public class CrmPermissionExtendedServiceImpl implements CrmPermissionExtendedService {

    @Resource
    private CrmPermissionExtendedMapper permissionExtendedMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermissionExtended(CrmPermissionExtendedCreateReqBO createReqBO) {
        CrmPermissionExtendedDO permissionExtendedDO = BeanUtils.toBean(createReqBO, CrmPermissionExtendedDO.class);
        permissionExtendedMapper.insert(permissionExtendedDO);
        return permissionExtendedDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissionExtendedBySubBizBatch(Integer subBizType, Collection<Long> subBizIds) {
        List<CrmPermissionExtendedDO> permissionExtendedList =
                permissionExtendedMapper.selectPermissionExtendedBySubBiz(subBizType, subBizIds);
        if (ObjUtil.notEqual(permissionExtendedList.size(), subBizIds.size())) { // 没有直接结束不用报错
            return;
        }
        permissionExtendedMapper.deleteBatchIds(convertSet(permissionExtendedList, CrmPermissionExtendedDO::getId));
    }

    @Override
    public List<CrmPermissionExtendedDO> getPermissionExtendedBySubBiz(Integer subBizType, Long subBizId) {
        ArrayList<CrmPermissionExtendedDO> permissionExtendedList = new ArrayList<>();
        CrmPermissionExtendedDO permissionExtendedDO = permissionExtendedMapper.selectPermissionExtendedBySubBiz(subBizType, subBizId);
        permissionExtendedList.add(permissionExtendedDO);
        // 查询出所有的祖先
        List<CrmPermissionExtendedDO> allAncestors = getAllAncestors(permissionExtendedDO);
        if (CollUtil.isEmpty(allAncestors)) {
            return permissionExtendedList;
        }
        permissionExtendedList.addAll(allAncestors);
        return permissionExtendedList;
    }

    public List<CrmPermissionExtendedDO> getAllAncestors(CrmPermissionExtendedDO currentNode) {
        List<CrmPermissionExtendedDO> ancestors = new ArrayList<>();
        Long parentId = currentNode.getParentBizId();
        while (parentId != null) {
            CrmPermissionExtendedDO parentNode = permissionExtendedMapper.selectPermissionExtendedBySubBiz(
                    currentNode.getParentBizType(), currentNode.getParentBizId()); // 根据 ID 查询父节点信息
            if (parentNode != null) {
                ancestors.add(parentNode);
                parentId = parentNode.getParentBizId(); // 更新父节点 ID，继续往上查找
            } else {
                break; // 如果父节点为空，跳出循环
            }
        }
        return ancestors;
    }

}
