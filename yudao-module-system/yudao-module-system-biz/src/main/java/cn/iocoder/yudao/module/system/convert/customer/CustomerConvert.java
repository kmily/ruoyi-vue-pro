package cn.iocoder.yudao.module.system.convert.customer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.customer.CustomerDO;

/**
 * 客户管理 Convert
 *
 * @author 管理员
 */
@Mapper
public interface CustomerConvert {

    CustomerConvert INSTANCE = Mappers.getMapper(CustomerConvert.class);

    CustomerDO convert(CustomerCreateReqVO bean);

    CustomerDO convert(CustomerUpdateReqVO bean);

    CustomerRespVO convert(CustomerDO bean);

    List<CustomerRespVO> convertList(List<CustomerDO> list);

    PageResult<CustomerRespVO> convertPage(PageResult<CustomerDO> page);

    List<CustomerExcelVO> convertList02(List<CustomerDO> list);

}
