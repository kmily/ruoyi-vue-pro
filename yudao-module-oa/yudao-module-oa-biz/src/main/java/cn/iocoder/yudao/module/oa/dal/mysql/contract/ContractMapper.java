package cn.iocoder.yudao.module.oa.dal.mysql.contract;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.contract.ContractDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.*;

/**
 * 合同 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ContractMapper extends BaseMapperX<ContractDO> {

    default PageResult<ContractDO> selectPage(ContractPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getContractNo, reqVO.getContractNo())
                .eqIfPresent(ContractDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ContractDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(ContractDO::getCreateBy, reqVO.getCreateBy())
                .orderByDesc(ContractDO::getId));
    }

    default List<ContractDO> selectList(ContractExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getContractNo, reqVO.getContractNo())
                .eqIfPresent(ContractDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ContractDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(ContractDO::getCreateBy, reqVO.getCreateBy())
                .orderByDesc(ContractDO::getId));
    }

}
