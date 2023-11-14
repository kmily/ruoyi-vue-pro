package cn.iocoder.yudao.module.hospital.dal.mysql.organization;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organization.OrganizationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.organization.vo.*;

/**
 * 组织机构 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationMapper extends BaseMapperX<OrganizationDO>, BaseMapper<OrganizationDO> {

    default PageResult<OrganizationDO> selectPage(OrganizationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrganizationDO>()
                .eqIfPresent(OrganizationDO::getUserId, reqVO.getUserId())
                .likeIfPresent(OrganizationDO::getUserName, reqVO.getUserName())
                .eqIfPresent(OrganizationDO::getSelfOperated, reqVO.getSelfOperated())
                .eqIfPresent(OrganizationDO::getCashOut, reqVO.getCashOut())
                .eqIfPresent(OrganizationDO::getDisable, reqVO.getDisable())
                .eqIfPresent(OrganizationDO::getLogo, reqVO.getLogo())
                .likeIfPresent(OrganizationDO::getName, reqVO.getName())
                .eqIfPresent(OrganizationDO::getAddressDetail, reqVO.getAddressDetail())
                .eqIfPresent(OrganizationDO::getAddressIdPath, reqVO.getAddressIdPath())
                .eqIfPresent(OrganizationDO::getAddressPath, reqVO.getAddressPath())
                .eqIfPresent(OrganizationDO::getCenter, reqVO.getCenter())
                .eqIfPresent(OrganizationDO::getIntroduction, reqVO.getIntroduction())
                .eqIfPresent(OrganizationDO::getDeliveryScore, reqVO.getDeliveryScore())
                .eqIfPresent(OrganizationDO::getDescriptionScore, reqVO.getDescriptionScore())
                .eqIfPresent(OrganizationDO::getServiceScore, reqVO.getServiceScore())
                .eqIfPresent(OrganizationDO::getStaffNum, reqVO.getStaffNum())
                .eqIfPresent(OrganizationDO::getGoodsNum, reqVO.getGoodsNum())
                .eqIfPresent(OrganizationDO::getCollectionNum, reqVO.getCollectionNum())
                .eqIfPresent(OrganizationDO::getImages, reqVO.getImages())
                .eqIfPresent(OrganizationDO::getMobile, reqVO.getMobile())
                .eqIfPresent(OrganizationDO::getEmail, reqVO.getEmail())
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
                .eqIfPresent(OrganizationDO::getLogo, reqVO.getLogo())
                .likeIfPresent(OrganizationDO::getName, reqVO.getName())
                .eqIfPresent(OrganizationDO::getAddressDetail, reqVO.getAddressDetail())
                .eqIfPresent(OrganizationDO::getAddressIdPath, reqVO.getAddressIdPath())
                .eqIfPresent(OrganizationDO::getAddressPath, reqVO.getAddressPath())
                .eqIfPresent(OrganizationDO::getCenter, reqVO.getCenter())
                .eqIfPresent(OrganizationDO::getIntroduction, reqVO.getIntroduction())
                .eqIfPresent(OrganizationDO::getDeliveryScore, reqVO.getDeliveryScore())
                .eqIfPresent(OrganizationDO::getDescriptionScore, reqVO.getDescriptionScore())
                .eqIfPresent(OrganizationDO::getServiceScore, reqVO.getServiceScore())
                .eqIfPresent(OrganizationDO::getStaffNum, reqVO.getStaffNum())
                .eqIfPresent(OrganizationDO::getGoodsNum, reqVO.getGoodsNum())
                .eqIfPresent(OrganizationDO::getCollectionNum, reqVO.getCollectionNum())
                .eqIfPresent(OrganizationDO::getImages, reqVO.getImages())
                .eqIfPresent(OrganizationDO::getMobile, reqVO.getMobile())
                .eqIfPresent(OrganizationDO::getEmail, reqVO.getEmail())
                .betweenIfPresent(OrganizationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationDO::getId));
    }

}
