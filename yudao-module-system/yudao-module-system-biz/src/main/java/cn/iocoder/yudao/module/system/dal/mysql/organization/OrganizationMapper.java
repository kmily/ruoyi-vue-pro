package cn.iocoder.yudao.module.system.dal.mysql.organization;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.OrganizationExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.OrganizationPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 组织机构 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationMapper extends BaseMapperX<OrganizationDO>, BaseMapper<OrganizationDO> {

    default PageResult<OrganizationDO> selectPage(OrganizationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrganizationDO>()
                .likeIfPresent(OrganizationDO::getUserName, reqVO.getUserName())
                .eqIfPresent(OrganizationDO::getDisable, reqVO.getDisable())
                .likeIfPresent(OrganizationDO::getName, reqVO.getName())
                .betweenIfPresent(OrganizationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationDO::getId));
    }

    default List<OrganizationDO> selectList(OrganizationExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OrganizationDO>()
                .eqIfPresent(OrganizationDO::getUserId, reqVO.getUserId())
                .likeIfPresent(OrganizationDO::getUserName, reqVO.getUserName())
                .eqIfPresent(OrganizationDO::getSelfOperated, reqVO.getSelfOperated())
                .eqIfPresent(OrganizationDO::getCashOut, reqVO.getCashOut())
                .eqIfPresent(OrganizationDO::getDisable, reqVO.getDisable())
                .likeIfPresent(OrganizationDO::getName, reqVO.getName())
                .betweenIfPresent(OrganizationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationDO::getId));
    }

}
