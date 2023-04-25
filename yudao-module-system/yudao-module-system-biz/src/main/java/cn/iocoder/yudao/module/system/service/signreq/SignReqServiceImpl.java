package cn.iocoder.yudao.module.system.service.signreq;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.signreq.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.signreq.SignReqDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.system.convert.signreq.SignReqConvert;
import cn.iocoder.yudao.module.system.dal.mysql.signreq.SignReqMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 注册申请 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated

public class SignReqServiceImpl implements SignReqService {

    @Resource
    private SignReqMapper signReqMapper;

    @Override
    public Long createSignReq(SignReqCreateReqVO createReqVO) {
        // 插入
        SignReqDO signReq = SignReqConvert.INSTANCE.convert(createReqVO);
        signReqMapper.insert(signReq);
        // 返回
        return signReq.getId();
    }

    @Override
    public void updateSignReq(SignReqUpdateReqVO updateReqVO) {
        // 校验存在
        validateSignReqExists(updateReqVO.getId());
        // 更新
        SignReqDO updateObj = SignReqConvert.INSTANCE.convert(updateReqVO);
        signReqMapper.updateById(updateObj);
    }

    @Override
    public void deleteSignReq(Long id) {
        // 校验存在
        validateSignReqExists(id);
        // 删除
        signReqMapper.deleteById(id);
    }

    private void validateSignReqExists(Long id) {
        if (signReqMapper.selectById(id) == null) {
            throw exception(SIGN_REQ_NOT_EXISTS);
        }
    }

    @Override
    public SignReqDO getSignReq(Long id) {
        return signReqMapper.selectById(id);
    }

    @Override
    public List<SignReqDO> getSignReqList(Collection<Long> ids) {
        return signReqMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<SignReqDO> getSignReqPage(SignReqPageReqVO pageReqVO) {
        return signReqMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SignReqDO> getSignReqList(SignReqExportReqVO exportReqVO) {
        return signReqMapper.selectList(exportReqVO);
    }

}
