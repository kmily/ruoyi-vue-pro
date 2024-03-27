package cn.iocoder.yudao.module.steam.service.youyoutemplate;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.YouyouTemplateMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 悠悠商品模板 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class YouyouTemplateServiceImpl implements YouyouTemplateService {

    @Resource
    private YouyouTemplateMapper youyouTemplateMapper;

    @Override
    public Integer createYouyouTemplate(YouyouTemplateSaveReqVO createReqVO) {
        // 插入
        YouyouTemplateDO youyouTemplate = BeanUtils.toBean(createReqVO, YouyouTemplateDO.class);
        youyouTemplateMapper.insert(youyouTemplate);
        // 返回
        return youyouTemplate.getId();
    }

    @Override
    public void updateYouyouTemplate(YouyouTemplateSaveReqVO updateReqVO) {
        // 校验存在
        validateYouyouTemplateExists(updateReqVO.getId());
        // 更新
        YouyouTemplateDO updateObj = BeanUtils.toBean(updateReqVO, YouyouTemplateDO.class);
        youyouTemplateMapper.updateById(updateObj);
    }

    @Override
    public void deleteYouyouTemplate(Integer id) {
        // 校验存在
        validateYouyouTemplateExists(id);
        // 删除
        youyouTemplateMapper.deleteById(id);
    }

    private void validateYouyouTemplateExists(Integer id) {
        if (youyouTemplateMapper.selectById(id) == null) {
            throw exception(YOUYOU_TEMPLATE_NOT_EXISTS);
        }
    }

    @Override
    public YouyouTemplateDO getYouyouTemplate(Integer id) {
        return youyouTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<YouyouTemplateDO> getYouyouTemplatePage(YouyouTemplatePageReqVO pageReqVO) {
        return youyouTemplateMapper.selectPage(pageReqVO);
    }

}