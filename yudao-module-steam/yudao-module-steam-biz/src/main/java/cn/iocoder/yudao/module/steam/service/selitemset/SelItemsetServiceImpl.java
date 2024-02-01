package cn.iocoder.yudao.module.steam.service.selitemset;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selitemset.SelItemsetDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.selitemset.SelItemsetMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 收藏品选择 Service 实现类
 *
 * @author glzaboy
 */
@Service
@Validated
public class SelItemsetServiceImpl implements SelItemsetService {

    @Resource
    private SelItemsetMapper selItemsetMapper;

    @Override
    public Long createSelItemset(SelItemsetSaveReqVO createReqVO) {
        // 校验父级编号的有效性
        validateParentSelItemset(null, createReqVO.getParentId());
        // 校验英文名称的唯一性
        validateSelItemsetInternalNameUnique(null, createReqVO.getParentId(), createReqVO.getInternalName());

        // 插入
        SelItemsetDO selItemset = BeanUtils.toBean(createReqVO, SelItemsetDO.class);
        selItemsetMapper.insert(selItemset);
        // 返回
        return selItemset.getId();
    }

    @Override
    public void updateSelItemset(SelItemsetSaveReqVO updateReqVO) {
        // 校验存在
        validateSelItemsetExists(updateReqVO.getId());
        // 校验父级编号的有效性
        validateParentSelItemset(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验英文名称的唯一性
        validateSelItemsetInternalNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getInternalName());

        // 更新
        SelItemsetDO updateObj = BeanUtils.toBean(updateReqVO, SelItemsetDO.class);
        selItemsetMapper.updateById(updateObj);
    }

    @Override
    public void deleteSelItemset(Long id) {
        // 校验存在
        validateSelItemsetExists(id);
        // 校验是否有子收藏品选择
        if (selItemsetMapper.selectCountByParentId(id) > 0) {
            throw exception(SEL_ITEMSET_EXITS_CHILDREN);
        }
        // 删除
        selItemsetMapper.deleteById(id);
    }

    private void validateSelItemsetExists(Long id) {
        if (selItemsetMapper.selectById(id) == null) {
            throw exception(SEL_ITEMSET_NOT_EXISTS);
        }
    }

    private void validateParentSelItemset(Long id, Long parentId) {
        if (parentId == null || SelItemsetDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父收藏品选择
        if (Objects.equals(id, parentId)) {
            throw exception(SEL_ITEMSET_PARENT_ERROR);
        }
        // 2. 父收藏品选择不存在
        SelItemsetDO parentSelItemset = selItemsetMapper.selectById(parentId);
        if (parentSelItemset == null) {
            throw exception(SEL_ITEMSET_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父收藏品选择，如果父收藏品选择是自己的子收藏品选择，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentSelItemset.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(SEL_ITEMSET_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父收藏品选择
            if (parentId == null || SelItemsetDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentSelItemset = selItemsetMapper.selectById(parentId);
            if (parentSelItemset == null) {
                break;
            }
        }
    }

    private void validateSelItemsetInternalNameUnique(Long id, Long parentId, String internalName) {
        SelItemsetDO selItemset = selItemsetMapper.selectByParentIdAndInternalName(parentId, internalName);
        if (selItemset == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的收藏品选择
        if (id == null) {
            throw exception(SEL_ITEMSET_INTERNAL_NAME_DUPLICATE);
        }
        if (!Objects.equals(selItemset.getId(), id)) {
            throw exception(SEL_ITEMSET_INTERNAL_NAME_DUPLICATE);
        }
    }

    @Override
    public SelItemsetDO getSelItemset(Long id) {
        return selItemsetMapper.selectById(id);
    }

    @Override
    public List<SelItemsetDO> getSelItemsetList(SelItemsetListReqVO listReqVO) {
        return selItemsetMapper.selectList(listReqVO);
    }

}