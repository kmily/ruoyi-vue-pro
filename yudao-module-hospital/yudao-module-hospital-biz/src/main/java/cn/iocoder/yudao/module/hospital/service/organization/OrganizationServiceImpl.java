package cn.iocoder.yudao.module.hospital.service.organization;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo.OrganizationCheckLogCreateReqVO;
import cn.iocoder.yudao.module.hospital.enums.OrganizationStatusEnum;
import cn.iocoder.yudao.module.hospital.service.organizationchecklog.OrganizationCheckLogService;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.organization.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organization.OrganizationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.organization.OrganizationConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.organization.OrganizationMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.hospital.enums.OrganizationStatusEnum.*;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.USER_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 组织机构 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationDO> implements OrganizationService {

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private OrganizationCheckLogService checkLogService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public Long createOrganization(OrganizationCreateReqVO createReqVO) {
        // 插入
        OrganizationDO organization = OrganizationConvert.INSTANCE.convert(createReqVO);
        organization.setDisable(APPLYING.value());

        OrganizationDO organizationDO = this.organizationMapper.selectOne(OrganizationDO::getName, organization.getName());
        if(organizationDO != null){
            throw exception(STORE_NAME_EXIST_ERROR);
        }

        MemberUserRespDTO user = memberUserApi.getUser(organization.getUserId());
        if(user == null){
            throw exception(USER_NOT_EXISTS);
        }
        if(Boolean.TRUE.equals(user.getHaveStore())){
            throw exception(STORE_APPLY_DOUBLE_ERROR);
        }
        organizationMapper.insert(organization);
        // 设置会员 - 店铺信息
        memberUserApi.updateUserHaveStore(user.getId(), organization.getId());
        // 返回
        return organization.getId();
    }

    @Override
    public void updateOrganization(OrganizationUpdateReqVO updateReqVO) {
        // 校验存在
        validateOrganizationExists(updateReqVO.getId());
        // 更新
        OrganizationDO updateObj = OrganizationConvert.INSTANCE.convert(updateReqVO);
        organizationMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrganization(Long id) {
        // 校验存在
        validateOrganizationExists(id);
        // 删除
        organizationMapper.deleteById(id);
    }

    private void validateOrganizationExists(Long id) {
        if (organizationMapper.selectById(id) == null) {
            throw exception(ORGANIZATION_NOT_EXISTS);
        }
    }

    @Override
    public OrganizationDO getOrganization(Long id) {
        return organizationMapper.selectById(id);
    }

    @Override
    public List<OrganizationDO> getOrganizationList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return organizationMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OrganizationDO> getOrganizationPage(OrganizationPageReqVO pageReqVO) {
        return organizationMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrganizationDO> getOrganizationList(OrganizationExportReqVO exportReqVO) {
        return organizationMapper.selectList(exportReqVO);
    }

    @Override
    public void closeOrganization(Long id) {
        // 校验存在
        validateOrganizationExists(id);
        organizationMapper.updateById(new OrganizationDO().setDisable(CLOSED.value()).setId(id));
    }

    @Override
    public void openOrganization(Long id) {
        // 校验存在
        validateOrganizationExists(id);
        organizationMapper.updateById(new OrganizationDO().setDisable(OPEN.value()).setId(id));
    }

    @Override
    public void auditOrganization(OrganizationAuditVO auditVO) {
        OrganizationDO organization = this.getOrganization(auditVO.getId());
        if(organization == null){
            throw exception(ORGANIZATION_NOT_EXISTS);
        }
        Integer status = auditVO.getStatus();
        AdminUserRespDTO user = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
        String checkStatus;
        if(status == 1){
            // 审通过
            memberUserApi.updateUserHaveStore(organization.getUserId(), organization.getId());
            checkStatus = OPEN.value();
        }else{
            checkStatus = REFUSED.value();
        }

        organizationMapper.updateById(new OrganizationDO()
                .setId(auditVO.getId())
                .setCheckName(user.getNickname())
                .setCheckStatus(checkStatus)
                .setDisable(checkStatus)
                .setCheckTime(LocalDateTime.now()));
        checkLogService.createOrganizationCheckLog(new OrganizationCheckLogCreateReqVO(auditVO, user.getNickname()));

    }

}
