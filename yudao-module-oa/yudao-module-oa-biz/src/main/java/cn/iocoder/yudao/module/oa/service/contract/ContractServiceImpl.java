package cn.iocoder.yudao.module.oa.service.contract;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.contract.ContractDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.contract.ContractConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.contract.ContractMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 合同 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class ContractServiceImpl implements ContractService {

    @Resource
    private ContractMapper contractMapper;

    @Override
    public Long createContract(ContractCreateReqVO createReqVO) {
        // 插入
        ContractDO contract = ContractConvert.INSTANCE.convert(createReqVO);
        contractMapper.insert(contract);
        // 返回
        return contract.getId();
    }

    @Override
    public void updateContract(ContractUpdateReqVO updateReqVO) {
        // 校验存在
        validateContractExists(updateReqVO.getId());
        // 更新
        ContractDO updateObj = ContractConvert.INSTANCE.convert(updateReqVO);
        contractMapper.updateById(updateObj);
    }

    @Override
    public void deleteContract(Long id) {
        // 校验存在
        validateContractExists(id);
        // 删除
        contractMapper.deleteById(id);
    }

    private void validateContractExists(Long id) {
        if (contractMapper.selectById(id) == null) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
    }

    @Override
    public ContractDO getContract(Long id) {
        return contractMapper.selectById(id);
    }

    @Override
    public List<ContractDO> getContractList(Collection<Long> ids) {
        return contractMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ContractDO> getContractPage(ContractPageReqVO pageReqVO) {
        return contractMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ContractDO> getContractList(ContractExportReqVO exportReqVO) {
        return contractMapper.selectList(exportReqVO);
    }

}
