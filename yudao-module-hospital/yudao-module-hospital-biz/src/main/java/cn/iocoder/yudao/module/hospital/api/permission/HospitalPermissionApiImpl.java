package cn.iocoder.yudao.module.hospital.api.permission;

import org.springframework.stereotype.Service;

/**
 * @author whycode
 * @title: HospitalPermissionApiImpl
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/179:32
 */
@Service
public class HospitalPermissionApiImpl implements HospitalPermissionApi{
    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return false;
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        return false;
    }
}
