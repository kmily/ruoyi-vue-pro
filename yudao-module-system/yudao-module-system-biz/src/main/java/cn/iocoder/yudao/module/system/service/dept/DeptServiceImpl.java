package cn.iocoder.yudao.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import cn.iocoder.yudao.module.system.convert.dept.DeptConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.dal.redis.RedisKeyConstants;
import cn.iocoder.yudao.module.system.enums.dept.DeptIdEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 部门 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    public Long createDept(DeptCreateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(null, reqVO.getParentId(), reqVO.getName());
        // 插入部门
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @Override
    public void updateDept(DeptUpdateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        validateForCreateOrUpdate(reqVO.getId(), reqVO.getParentId(), reqVO.getName());
        // 更新部门
        DeptDO updateObj = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.updateById(updateObj);
    }

    @Override
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw exception(DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO reqVO) {
        return deptMapper.selectList(reqVO);
    }

    @Override
    public List<DeptDO> getChildDeptList(Long id) {
        List<DeptDO> children = new LinkedList<>();
        // 遍历每一层
        Collection<Long> parentIds = Collections.singleton(id);
        for (int i = 0; i < Short.MAX_VALUE; i++) { // 使用 Short.MAX_VALUE 避免 bug 场景下，存在死循环
            // 查询当前层，所有的子部门
            List<DeptDO> depts = deptMapper.selectListByParentId(parentIds);
            // 1. 如果没有子部门，则结束遍历
            if (CollUtil.isEmpty(depts)) {
                break;
            }
            // 2. 如果有子部门，继续遍历
            children.addAll(depts);
            parentIds = convertSet(depts, DeptDO::getId);
        }
        return children;
    }

    @Override
    @DataPermission(enable = false) // 禁用数据权限，避免简历不正确的缓存
    @Cacheable(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST
            + "#" + RedisKeyConstants.DEPT_CHILDREN_ID_LIST_EXPIRE, key = "#id")
    public Set<Long> getChildDeptIdListFromCache(Long id) {
        // 补充说明：为什么该缓存会有 1 分钟的延迟？主要有两点：
        // 1. Spring Cache 无法方便的批量清理，所以使用 Redis 自动过期的方式。
        // 2. 变更父节点的时候，影响父子节点的数量很多，包括原父节点及其父节点，以及新父节点及其父节点。
        // 如果你真的对延迟比较敏感，可以考虑采用使用 allEntries = true 的方式，清理所有缓存。
        List<DeptDO> children = getChildDeptList(id);
        return convertSet(children, DeptDO::getId);
    }

    private void validateForCreateOrUpdate(Long id, Long parentId, String name) {
        // 校验自己存在
        validateDeptExists(id);
        // 校验父部门的有效性
        validateParentDeptEnable(id, parentId);
        // 校验部门名的唯一性
        validateDeptNameUnique(id, parentId, name);
    }

    private void validateParentDeptEnable(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (parentId.equals(id)) {
            throw exception(DEPT_PARENT_ERROR);
        }
        // 父岗位不存在
        DeptDO dept = deptMapper.selectById(parentId);
        if (dept == null) {
            throw exception(DEPT_PARENT_NOT_EXITS);
        }
        // 父部门被禁用
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
            throw exception(DEPT_NOT_ENABLE);
        }
        // 父部门不能是原来的子部门
        List<DeptDO> children = getChildDeptList(id);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw exception(DEPT_PARENT_IS_CHILD);
        }
    }

    private void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw exception(DEPT_NOT_FOUND);
        }
    }

    private void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO menu = deptMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw exception(DEPT_NAME_DUPLICATE);
        }
    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }

    @Override
    public void validateDeptList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得科室信息
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        // 校验
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                throw exception(DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw exception(DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

}
