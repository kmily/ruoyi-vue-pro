package cn.iocoder.yudao.module.oa.service.contract;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.contract.ContractDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 合同 Service 接口
 *
 * @author 管理员
 */
public interface ContractService {

    /**
     * 创建合同
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createContract(@Valid ContractCreateReqVO createReqVO);

    /**
     * 更新合同
     *
     * @param updateReqVO 更新信息
     */
    void updateContract(@Valid ContractUpdateReqVO updateReqVO);

    /**
     * 删除合同
     *
     * @param id 编号
     */
    void deleteContract(Long id);

    /**
     * 获得合同
     *
     * @param id 编号
     * @return 合同
     */
    ContractDO getContract(Long id);

    /**
     * 获得合同列表
     *
     * @param ids 编号
     * @return 合同列表
     */
    List<ContractDO> getContractList(Collection<Long> ids);

    /**
     * 获得合同分页
     *
     * @param pageReqVO 分页查询
     * @return 合同分页
     */
    PageResult<ContractDO> getContractPage(ContractPageReqVO pageReqVO);

    /**
     * 获得合同列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 合同列表
     */
    List<ContractDO> getContractList(ContractExportReqVO exportReqVO);

}
