package cn.iocoder.yudao.module.crm.convert.permission;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.crm.controller.admin.permission.vo.CrmPermissionCreateReqVO;
import cn.iocoder.yudao.module.crm.controller.admin.permission.vo.CrmPermissionRespVO;
import cn.iocoder.yudao.module.crm.controller.admin.permission.vo.CrmPermissionUpdateReqVO;
import cn.iocoder.yudao.module.crm.dal.dataobject.permission.CrmPermissionDO;
import cn.iocoder.yudao.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import cn.iocoder.yudao.module.crm.service.permission.bo.CrmPermissionUpdateReqBO;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.crm.framework.enums.CrmPermissionLevelEnum.getNameByLevel;

/**
 * Crm 数据权限 Convert
 *
 * @author Wanwan
 */
@Mapper
public interface CrmPermissionConvert {

    CrmPermissionConvert INSTANCE = Mappers.getMapper(CrmPermissionConvert.class);

    CrmPermissionDO convert(CrmPermissionCreateReqBO createBO);

    CrmPermissionDO convert(CrmPermissionUpdateReqBO updateBO);

    CrmPermissionCreateReqBO convert(CrmPermissionCreateReqVO reqVO);

    CrmPermissionUpdateReqBO convert(CrmPermissionUpdateReqVO updateReqVO);

    List<CrmPermissionRespVO> convert(List<CrmPermissionDO> permission);

    default List<CrmPermissionRespVO> convert(List<CrmPermissionDO> permission, List<AdminUserRespDTO> userList) {
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        return CollectionUtils.convertList(convert(permission), item -> {
            MapUtils.findAndThen(userMap, item.getId(), user -> {
                item.setNickname(user.getNickname()).setDeptId(user.getDeptId()).setPostIds(user.getPostIds())
                        .setPermissionLevelName(getNameByLevel(item.getPermissionLevel()));
            });
            return item;
        });
    }

}
