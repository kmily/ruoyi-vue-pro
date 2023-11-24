package cn.iocoder.yudao.module.crm.convert.business;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.crm.controller.admin.business.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.business.CrmBusinessDO;
import cn.iocoder.yudao.module.crm.dal.dataobject.business.CrmBusinessStatusDO;
import cn.iocoder.yudao.module.crm.dal.dataobject.business.CrmBusinessStatusTypeDO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 商机 Convert
 *
 * @author ljlleo
 */
@Mapper
public interface CrmBusinessConvert {

    CrmBusinessConvert INSTANCE = Mappers.getMapper(CrmBusinessConvert.class);

    CrmBusinessDO convert(CrmBusinessCreateReqVO bean);

    CrmBusinessDO convert(CrmBusinessUpdateReqVO bean);

    CrmBusinessRespVO convert(CrmBusinessDO bean);

    PageResult<CrmBusinessRespVO> convertPage(PageResult<CrmBusinessDO> page);

    List<CrmBusinessExcelVO> convertList02(List<CrmBusinessDO> list);

    @Mappings({
            @Mapping(target = "bizId", source = "reqVO.id"),
            @Mapping(target = "newOwnerUserId", source = "reqVO.id")
    })
    CrmPermissionTransferReqBO convert(CrmBusinessTransferReqVO reqVO, Long userId);

    default PageResult<CrmBusinessRespVO> convertPage(PageResult<CrmBusinessDO> page, List<CrmCustomerDO> customerList,
                                                      List<CrmBusinessStatusTypeDO> statusTypeList, List<CrmBusinessStatusDO> statusList) {
        PageResult<CrmBusinessRespVO> result = convertPage(page);
        Map<Long, String> customerMap = convertMap(customerList, CrmCustomerDO::getId, CrmCustomerDO::getName);
        Map<Long, String> statusTypeMap = convertMap(statusTypeList, CrmBusinessStatusTypeDO::getId, CrmBusinessStatusTypeDO::getName);
        Map<Long, String> statusMap = convertMap(statusList, CrmBusinessStatusDO::getId, CrmBusinessStatusDO::getName);
        result.getList().stream().forEach(t -> {
            t.setCustomerName(customerMap.get(t.getCustomerId()));
            t.setStatusTypeName(statusTypeMap.get(t.getStatusTypeId()));
            t.setStatusName(statusMap.get(t.getStatusId()));
        });
        return result;
    }

}
