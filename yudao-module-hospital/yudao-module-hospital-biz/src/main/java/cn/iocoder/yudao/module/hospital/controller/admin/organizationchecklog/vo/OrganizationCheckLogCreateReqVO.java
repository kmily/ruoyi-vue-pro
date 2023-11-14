package cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo;

import cn.iocoder.yudao.module.hospital.controller.admin.organization.vo.OrganizationAuditVO;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.module.hospital.enums.OrganizationStatusEnum.*;

@Schema(description = "管理后台 - 组织审核记录创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrganizationCheckLogCreateReqVO extends OrganizationCheckLogBaseVO {

    public OrganizationCheckLogCreateReqVO(OrganizationAuditVO auditVO, String userName) {
        this.setOrgId(auditVO.getId())
                .setCheckStatus(auditVO.getStatus() == 1? OPEN.value(): REFUSED.value())
                .setOpinion(auditVO.getOpinion())
                .setCheckName(userName)
                .setCheckTime(LocalDateTime.now());
    }
}
