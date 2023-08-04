package cn.iocoder.yudao.module.member.service.family;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.family.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.family.FamilyConvert;
import cn.iocoder.yudao.module.member.dal.mysql.family.FamilyMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.FAMILY_NOT_EXISTS;

/**
 * 用户家庭 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FamilyServiceImpl implements FamilyService {

    @Resource
    private FamilyMapper familyMapper;

    @Override
    public Long createFamily(FamilyCreateReqVO createReqVO) {
        // 插入
        FamilyDO family = FamilyConvert.INSTANCE.convert(createReqVO);
        familyMapper.insert(family);
        // 返回
        return family.getId();
    }

    @Override
    public void updateFamily(FamilyUpdateReqVO updateReqVO) {
        // 校验存在
        validateFamilyExists(updateReqVO.getId());
        // 更新
        FamilyDO updateObj = FamilyConvert.INSTANCE.convert(updateReqVO);
        familyMapper.updateById(updateObj);
    }

    @Override
    public void deleteFamily(Long id) {
        // 校验存在
        validateFamilyExists(id);
        // 删除
        familyMapper.deleteById(id);
    }

    private void validateFamilyExists(Long id) {
        if (familyMapper.selectById(id) == null) {
            throw exception(FAMILY_NOT_EXISTS);
        }
    }

    @Override
    public FamilyDO getFamily(Long id) {
        return familyMapper.selectById(id);
    }

    @Override
    public List<FamilyDO> getFamilyList(Collection<Long> ids) {
        return familyMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FamilyDO> getFamilyPage(FamilyPageReqVO pageReqVO) {
        return familyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FamilyDO> getFamilyList(FamilyExportReqVO exportReqVO) {
        return familyMapper.selectList(exportReqVO);
    }

}
