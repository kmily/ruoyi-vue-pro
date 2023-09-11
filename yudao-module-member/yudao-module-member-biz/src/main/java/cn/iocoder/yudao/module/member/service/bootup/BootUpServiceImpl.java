package cn.iocoder.yudao.module.member.service.bootup;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.bootup.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.bootup.BootUpDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.bootup.BootUpConvert;
import cn.iocoder.yudao.module.member.dal.mysql.bootup.BootUpMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 用户启动数据 Service 实现类
 *
 * @author 和尘同光
 */
@Service
@Validated
public class BootUpServiceImpl implements BootUpService {

    @Resource
    private BootUpMapper bootUpMapper;

    @Override
    public Long createBootUp(BootUpCreateReqVO createReqVO) {
        // 插入
        BootUpDO bootUp = BootUpConvert.INSTANCE.convert(createReqVO);
        bootUpMapper.insert(bootUp);
        // 返回
        return bootUp.getId();
    }

    @Override
    public void updateBootUp(BootUpUpdateReqVO updateReqVO) {
        // 校验存在
        BootUpDO updateObj = BootUpConvert.INSTANCE.convert(updateReqVO);
        bootUpMapper.updateById(updateObj);
    }

    @Override
    public void deleteBootUp(Long id) {

    }


    @Override
    public BootUpDO getBootUp(Long id) {
        return bootUpMapper.selectById(id);
    }

    @Override
    public List<BootUpDO> getBootUpList(Collection<Long> ids) {
        return bootUpMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BootUpDO> getBootUpPage(BootUpPageReqVO pageReqVO) {
        return bootUpMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BootUpDO> getBootUpList(BootUpExportReqVO exportReqVO) {
        return bootUpMapper.selectList(exportReqVO);
    }

    @Override
    public List<BootUpDO> getBootUpList(BootUpReqVO bootUpReqVO) {
        return bootUpMapper.selectList(new LambdaQueryWrapperX<BootUpDO>()
                .betweenIfPresent(BaseDO::getCreateTime, bootUpReqVO.getCreateTime()));
    }

}
