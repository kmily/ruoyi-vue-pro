package cn.iocoder.yudao.module.system.api.origanization;

import cn.iocoder.yudao.module.system.api.organization.OrganizationApi;
import cn.iocoder.yudao.module.system.api.organization.dto.OrganizationDTO;
import cn.iocoder.yudao.module.system.convert.organization.OrganizationConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import cn.iocoder.yudao.module.system.service.organization.OrganizationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whycode
 * @title: OrganizationApiImple
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/1515:46
 */

@Service
public class OrganizationApiImpl implements OrganizationApi {

    @Resource
    private OrganizationService organizationService;

    @Override
    public OrganizationDTO getOrganization(Long id) {

        OrganizationDO organization = organizationService.getOrganization(id);

        return OrganizationConvert.INSTANCE.convert01(organization);
    }

    @Override
    public OrganizationDTO getSelfOrganization() {
        List<OrganizationDO> organizationDOS = organizationService.list(new LambdaQueryWrapper<OrganizationDO>().eq(OrganizationDO::getSelfOperated, true));
        return OrganizationConvert.INSTANCE.convert01(organizationDOS.get(0));
    }
}
