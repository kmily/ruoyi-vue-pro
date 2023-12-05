package cn.iocoder.yudao.module.crm.service.customer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.crm.convert.customer.CrmCustomerConvert;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerLimitConfigDO;
import cn.iocoder.yudao.module.crm.dal.mysql.customer.CrmCustomerMapper;
import cn.iocoder.yudao.module.crm.framework.core.annotations.CrmPermission;
import cn.iocoder.yudao.module.crm.framework.enums.CrmBizTypeEnum;
import cn.iocoder.yudao.module.crm.framework.enums.CrmPermissionLevelEnum;
import cn.iocoder.yudao.module.crm.service.permission.CrmPermissionService;
import cn.iocoder.yudao.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.crm.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.crm.enums.customer.CrmCustomerLimitConfigEnum.CUSTOMER_SIZE_LIMITATION;
import static cn.iocoder.yudao.module.crm.enums.customer.CrmCustomerLockStatusEnum.CUSTOMER_LOCK;

/**
 * 客户 Service 实现类
 *
 * @author Wanwan
 */
@Service
@Validated
public class CrmCustomerServiceImpl implements CrmCustomerService {

    @Resource
    private CrmCustomerMapper customerMapper;
    @Resource
    private CrmPermissionService crmPermissionService;
    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CrmCustomerLimitConfigService crmCustomerLimitConfigService;

    @Override
    public Long createCustomer(CrmCustomerCreateReqVO createReqVO, Long userId) {
        // 插入
        CrmCustomerDO customer = CrmCustomerConvert.INSTANCE.convert(createReqVO);
        customerMapper.insert(customer);

        // 创建数据权限
        crmPermissionService.createPermission(new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_CUSTOMER.getType())
                .setBizId(customer.getId()).setUserId(userId).setPermissionLevel(CrmPermissionLevelEnum.OWNER.getLevel())); // 设置当前操作的人为负责人

        // 返回
        return customer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, getIdFor = CrmCustomerUpdateReqVO.class,
            permissionLevel = CrmPermissionLevelEnum.WRITE)
    public void updateCustomer(CrmCustomerUpdateReqVO updateReqVO) {
        // 校验存在
        validateCustomerExists(updateReqVO.getId());
        // TODO 芋艿：数据权限，校验是否可以操作

        // 更新
        CrmCustomerDO updateObj = CrmCustomerConvert.INSTANCE.convert(updateReqVO);
        customerMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, permissionLevel = CrmPermissionLevelEnum.WRITE)
    public void deleteCustomer(Long id) {
        // 校验存在
        validateCustomerExists(id);
        // TODO 芋艿：数据权限，校验是否可以操作

        // 删除
        customerMapper.deleteById(id);
    }

    private void validateCustomerExists(Long id) {
        if (customerMapper.selectById(id) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, permissionLevel = CrmPermissionLevelEnum.READ)
    public CrmCustomerDO getCustomer(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public PageResult<CrmCustomerDO> getCustomerPage(CrmCustomerPageReqVO pageReqVO) {
        // TODO 芋艿：数据权限，是否可以查询到；
        return customerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CrmCustomerDO> getCustomerList(CrmCustomerExportReqVO exportReqVO) {
        return customerMapper.selectList(exportReqVO);
    }

    /**
     * 校验客户是否存在
     *
     * @param customerId 客户 id
     * @return 客户
     */
    @Override
    public CrmCustomerDO validateCustomer(Long customerId) {
        CrmCustomerDO customer = getCustomer(customerId);
        if (Objects.isNull(customer)) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferCustomer(CrmCustomerTransferReqVO reqVO, Long userId) {
        // 1. 校验合同是否存在
        validateCustomer(reqVO.getId());

        // 2. 数据权限转移
        crmPermissionService.transferPermission(
                CrmCustomerConvert.INSTANCE.convert(reqVO, userId).setBizType(CrmBizTypeEnum.CRM_CUSTOMER.getType()));

        // 3. TODO 记录转移日志
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockCustomer(CrmCustomerLockReqVO lockReqVO) {
        // 校验当前客户是否存在
        validateCustomerExists(lockReqVO.getId());
        // TODO @Joey：可以校验下，如果已经对应的锁定状态，报个业务异常；原因是：后续这个业务会记录操作日志，会记录多了；
        CrmCustomerDO customerDO = customerMapper.selectById(lockReqVO.getId());
        //校验当前是否重复操作锁定/解锁状态
        if(customerDO.getLockStatus().equals(lockReqVO.getLockStatus())){
            throw exception(CUSTOMER_UNLOCK_STATUS_NO_REPETITION);
        }
        // TODO @芋艿：业务完善，增加锁定上限；
        // 获取当前登录信息,开始校验锁定上限
        AdminUserRespDTO userRespDTO = adminUserApi.getUser(getLoginUserId());

        if (userRespDTO.getDeptId() == null || userRespDTO.getId() == null) {
            //如有入参为空，提示业务异常
            throw exception(CUSTOMER_NO_DEPARTMENT_FOUND);
        }

        //开始校验规则限制
        CrmCustomerLimitConfigReqVO configReqVO = new CrmCustomerLimitConfigReqVO();
        configReqVO.setUserIds(userRespDTO.getId().toString());
        configReqVO.setDeptIds(userRespDTO.getDeptId().toString());
        //校验类型为：锁定客户数限制
        configReqVO.setType(CUSTOMER_SIZE_LIMITATION);
        CrmCustomerLimitConfigDO crmCustomerLimitConfigDO = crmCustomerLimitConfigService.selectByLimitConfig(configReqVO);

        //统计当前用户已锁定客户数量
        List<CrmCustomerDO> crmCustomerDOS =  customerMapper.selectList("updater",getLoginUserId());
        long customerLockCount = crmCustomerDOS.stream().filter(CrmCustomerDO::getLockStatus).count();
        //锁定操作的时候校验当前用户可锁定客户的上限
        if(lockReqVO.getLockStatus() && customerLockCount >= crmCustomerLimitConfigDO.getMaxCount()){
            //超出锁定数量上限，提示业务异常
            throw exception(CUSTOMER_EXCEED_LOCK_LIMIT);
        }
        // 更新
        CrmCustomerDO updateObj = CrmCustomerConvert.INSTANCE.convert(lockReqVO);
        customerMapper.updateById(updateObj);
    }

}
