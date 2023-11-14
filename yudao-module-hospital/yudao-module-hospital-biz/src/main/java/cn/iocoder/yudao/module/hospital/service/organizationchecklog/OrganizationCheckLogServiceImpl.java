package cn.iocoder.yudao.module.hospital.service.organizationchecklog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organizationchecklog.OrganizationCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.organizationchecklog.OrganizationCheckLogConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.organizationchecklog.OrganizationCheckLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 组织审核记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrganizationCheckLogServiceImpl implements OrganizationCheckLogService {

    @Resource
    private OrganizationCheckLogMapper organizationCheckLogMapper;

    @Override
    public Long createOrganizationCheckLog(OrganizationCheckLogCreateReqVO createReqVO) {
        // 插入
        OrganizationCheckLogDO organizationCheckLog = OrganizationCheckLogConvert.INSTANCE.convert(createReqVO);
        organizationCheckLogMapper.insert(organizationCheckLog);
        // 返回
        return organizationCheckLog.getId();
    }

    @Override
    public List<OrganizationCheckLogDO> getOrganizationCheckLogList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return organizationCheckLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OrganizationCheckLogDO> getOrganizationCheckLogPage(OrganizationCheckLogPageReqVO pageReqVO) {
        return organizationCheckLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrganizationCheckLogDO> getOrganizationCheckLogList(OrganizationCheckLogExportReqVO exportReqVO) {
        return organizationCheckLogMapper.selectList(exportReqVO);
    }

}
