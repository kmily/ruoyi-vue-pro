package cn.iocoder.yudao.module.steam.service.othertemplate;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.othertemplate.OtherTemplateMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 其他平台模板 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class OtherTemplateServiceImpl implements OtherTemplateService {

    @Resource
    private OtherTemplateMapper otherTemplateMapper;

    @Override
    public Integer createOtherTemplate(OtherTemplateSaveReqVO createReqVO) {
        // 插入
        OtherTemplateDO otherTemplate = BeanUtils.toBean(createReqVO, OtherTemplateDO.class);
        otherTemplateMapper.insert(otherTemplate);
        // 返回
        return otherTemplate.getId();
    }

    @Override
    public void updateOtherTemplate(OtherTemplateSaveReqVO updateReqVO) {
        // 校验存在
        validateOtherTemplateExists(updateReqVO.getId());
        // 更新
        OtherTemplateDO updateObj = BeanUtils.toBean(updateReqVO, OtherTemplateDO.class);
        otherTemplateMapper.updateById(updateObj);
    }

    @Override
    public void deleteOtherTemplate(Integer id) {
        // 校验存在
        validateOtherTemplateExists(id);
        // 删除
        otherTemplateMapper.deleteById(id);
    }

    private void validateOtherTemplateExists(Integer id) {
        if (otherTemplateMapper.selectById(id) == null) {
            throw exception(OTHER_TEMPLATE_NOT_EXISTS);
        }
    }

    @Override
    public OtherTemplateDO getOtherTemplate(Integer id) {
        return otherTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<OtherTemplateDO> getOtherTemplatePage(OtherTemplatePageReqVO pageReqVO) {
        return otherTemplateMapper.selectPage(pageReqVO);
    }

}