package cn.iocoder.yudao.module.steam.dal.mysql.invpreview;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.*;

/**
 * 饰品在售预览 Mapper
 *
 * @author LeeAm
 */
@Mapper
public interface InvPreviewMapper extends BaseMapperX<InvPreviewDO> {

    default PageResult<InvPreviewDO> selectPage(InvPreviewPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getPrice, reqVO.getPrice())
                .eqIfPresent(InvPreviewDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(InvPreviewDO::getDeals, reqVO.getDeals())
                .eqIfPresent(InvPreviewDO::getItemId, reqVO.getItemId())
                .eqIfPresent(InvPreviewDO::getAppId, reqVO.getAppId())
                .likeIfPresent(InvPreviewDO::getItemName, reqVO.getItemName())
                .likeIfPresent(InvPreviewDO::getShortName, reqVO.getShortName())
                .likeIfPresent(InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())
                .eqIfPresent(InvPreviewDO::getImageUrl, reqVO.getImageUrl())
                .eqIfPresent(InvPreviewDO::getItemInfo, reqVO.getItemInfo())
                .eqIfPresent(InvPreviewDO::getSellType, reqVO.getSellType())
                .eqIfPresent(InvPreviewDO::getCurrencyId, reqVO.getCurrencyId())
                .eqIfPresent(InvPreviewDO::getCnyPrice, reqVO.getCnyPrice())
                .eqIfPresent(InvPreviewDO::getSalePrice, reqVO.getSalePrice())
                .eqIfPresent(InvPreviewDO::getSubsidyPrice, reqVO.getSubsidyPrice())
                .eqIfPresent(InvPreviewDO::getActivityTag, reqVO.getActivityTag())
                .eqIfPresent(InvPreviewDO::getTagList, reqVO.getTagList())
                .eqIfPresent(InvPreviewDO::getSubsidyTag, reqVO.getSubsidyTag())
                .eqIfPresent(InvPreviewDO::getAutoPrice, reqVO.getAutoPrice())
                .eqIfPresent(InvPreviewDO::getAutoQuantity, reqVO.getAutoQuantity())
                .eqIfPresent(InvPreviewDO::getReferencePrice, reqVO.getReferencePrice())
                .eqIfPresent(InvPreviewDO::getSelQuality, reqVO.getSelQuality())
                .eqIfPresent(InvPreviewDO::getSelItemset, reqVO.getSelItemset())
                .eqIfPresent(InvPreviewDO::getSelWeapon, reqVO.getSelWeapon())
                .eqIfPresent(InvPreviewDO::getSelExterior, reqVO.getSelExterior())
                .eqIfPresent(InvPreviewDO::getSelRarity, reqVO.getSelRarity())
                .eqIfPresent(InvPreviewDO::getSelType, reqVO.getSelType())
                .eqIfPresent(InvPreviewDO::getExistInv, reqVO.getExistInv())
                .orderByDesc(InvPreviewDO::getId));
    }

}