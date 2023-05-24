package cn.iocoder.yudao.module.oa.dal.mysql.contract;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.ContractExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.ContractPageReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.contract.ContractDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
                .eqIfPresent(ContractDO::getCreator, reqVO.getCreator())
                .orderByDesc(ContractDO::getId));
    }

    default List<ContractDO> selectList(ContractExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getContractNo, reqVO.getContractNo())
                .eqIfPresent(ContractDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ContractDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(ContractDO::getCreator, reqVO.getCreator())
                .orderByDesc(ContractDO::getId));
    }

}
