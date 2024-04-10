package cn.iocoder.yudao.module.steam.dal.mysql.othertemplate;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo.*;

/**
 * 其他平台模板 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface OtherTemplateMapper extends BaseMapperX<OtherTemplateDO> {

    default PageResult<OtherTemplateDO> selectPage(OtherTemplatePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OtherTemplateDO>()
                .eqIfPresent(OtherTemplateDO::getPlatformIdentity, reqVO.getPlatformIdentity())
                .eqIfPresent(OtherTemplateDO::getExterior, reqVO.getExterior())
                .likeIfPresent(OtherTemplateDO::getExteriorName, reqVO.getExteriorName())
                .eqIfPresent(OtherTemplateDO::getItemId, reqVO.getItemId())
                .likeIfPresent(OtherTemplateDO::getItemName, reqVO.getItemName())
                .likeIfPresent(OtherTemplateDO::getMarketHashName, reqVO.getMarketHashName())
                .eqIfPresent(OtherTemplateDO::getAutoDeliverPrice, reqVO.getAutoDeliverPrice())
                .eqIfPresent(OtherTemplateDO::getQuality, reqVO.getQuality())
                .eqIfPresent(OtherTemplateDO::getRarity, reqVO.getRarity())
                .eqIfPresent(OtherTemplateDO::getType, reqVO.getType())
                .eqIfPresent(OtherTemplateDO::getImageUrl, reqVO.getImageUrl())
                .eqIfPresent(OtherTemplateDO::getAutoDeliverQuantity, reqVO.getAutoDeliverQuantity())
                .eqIfPresent(OtherTemplateDO::getQualityColor, reqVO.getQualityColor())
                .likeIfPresent(OtherTemplateDO::getQualityName, reqVO.getQualityName())
                .eqIfPresent(OtherTemplateDO::getRarityColor, reqVO.getRarityColor())
                .likeIfPresent(OtherTemplateDO::getRarityName, reqVO.getRarityName())
                .likeIfPresent(OtherTemplateDO::getShortName, reqVO.getShortName())
                .likeIfPresent(OtherTemplateDO::getTypeName, reqVO.getTypeName())
                .orderByDesc(OtherTemplateDO::getId));
    }

}