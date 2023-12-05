package cn.iocoder.yudao.module.crm.service.customer;


import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmCustomerLimitConfigReqVO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerLimitConfigDO;

/**
 * 客户限制配置 Service 接口
 *
 * @author Joey
 */
public interface CrmCustomerLimitConfigService {

    /**
     * 查询当前登录人客户限制配置
     */
    CrmCustomerLimitConfigDO selectByLimitConfig(CrmCustomerLimitConfigReqVO configReqVO);

}
