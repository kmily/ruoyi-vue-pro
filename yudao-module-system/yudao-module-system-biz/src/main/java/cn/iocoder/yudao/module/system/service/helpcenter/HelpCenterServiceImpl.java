package cn.iocoder.yudao.module.system.service.helpcenter;

import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterUpdateReqVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.system.dal.dataobject.helpcenter.HelpCenterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.system.convert.helpcenter.HelpCenterConvert;
import cn.iocoder.yudao.module.system.dal.mysql.helpcenter.HelpCenterMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.HELP_CENTER_NOT_EXISTS;

/**
 * 常见问题 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HelpCenterServiceImpl extends ServiceImpl<HelpCenterMapper, HelpCenterDO> implements HelpCenterService {

    @Resource
    private HelpCenterMapper helpCenterMapper;

    @Override
    public Long createHelpCenter(HelpCenterCreateReqVO createReqVO) {
        // 插入
        HelpCenterDO helpCenter = HelpCenterConvert.INSTANCE.convert(createReqVO);
        helpCenterMapper.insert(helpCenter);
        // 返回
        return helpCenter.getId();
    }

    @Override
    public void updateHelpCenter(HelpCenterUpdateReqVO updateReqVO) {
        // 校验存在
        validateHelpCenterExists(updateReqVO.getId());
        // 更新
        HelpCenterDO updateObj = HelpCenterConvert.INSTANCE.convert(updateReqVO);
        helpCenterMapper.updateById(updateObj);
    }

    @Override
    public void deleteHelpCenter(Long id) {
        // 校验存在
        validateHelpCenterExists(id);
        // 删除
        helpCenterMapper.deleteById(id);
    }

    private void validateHelpCenterExists(Long id) {
        if (helpCenterMapper.selectById(id) == null) {
            throw exception(HELP_CENTER_NOT_EXISTS);
        }
    }

    @Override
    public HelpCenterDO getHelpCenter(Long id) {
        return helpCenterMapper.selectById(id);
    }

    @Override
    public List<HelpCenterDO> getHelpCenterList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return helpCenterMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<HelpCenterDO> getHelpCenterPage(HelpCenterPageReqVO pageReqVO) {
        return helpCenterMapper.selectPage(pageReqVO);
    }

    @Override
    public List<HelpCenterDO> getHelpCenterList(HelpCenterExportReqVO exportReqVO) {
        return helpCenterMapper.selectList(exportReqVO);
    }

}
