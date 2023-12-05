package cn.iocoder.yudao.module.crm.service.customer;

import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmCustomerLimitConfigReqVO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerLimitConfigDO;
import cn.iocoder.yudao.module.crm.dal.mysql.customer.CrmCustomerLimitConfigMapper;
import cn.iocoder.yudao.module.crm.dal.mysql.customer.CrmCustomerMapper;
import cn.iocoder.yudao.module.crm.service.permission.CrmPermissionService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * 客户限制配置 Service 实现类
 *
 * @author Joey
 */
@Service
@Validated
public class CrmCustomerLimitConfigServiceImpl implements CrmCustomerLimitConfigService {

    @Resource
    private CrmCustomerLimitConfigMapper crmCustomerLimitConfigMapper;


    @Override
    public CrmCustomerLimitConfigDO selectByLimitConfig(CrmCustomerLimitConfigReqVO configReqVO) {
        return crmCustomerLimitConfigMapper.selectByLimitConfig(configReqVO);
    }
}
