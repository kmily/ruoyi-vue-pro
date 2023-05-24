package cn.iocoder.yudao.module.oa.convert.customer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerExcelVO;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerRespVO;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerUpdateReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.customer.CustomerDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
