package cn.iocoder.yudao.module.steam.dal.mysql.invpreview;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.smallbun.screw.core.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * 饰品在售预览 Mapper
 *
 * @author LeeAm
 */
@Mapper
public interface InvPreviewMapperExt extends BaseMapperX<InvPreviewDO> {



    default PageResult<InvPreviewDO> selectPage(InvPreviewPageReqVO reqVO) {
        QueryWrapper<InvPreviewDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ObjectUtil.isNotNull(reqVO.getPrice()),InvPreviewDO::getPrice, reqVO.getPrice())
                .eq(ObjectUtil.isNotNull(reqVO.getQuantity()),InvPreviewDO::getQuantity, reqVO.getQuantity())
                .eq(ObjectUtil.isNotNull(reqVO.getDeals()),InvPreviewDO::getDeals, reqVO.getDeals())
                .eq(ObjectUtil.isNotNull(reqVO.getItemId()),InvPreviewDO::getItemId, reqVO.getItemId())
                .eq(ObjectUtil.isNotNull( reqVO.getAppId()),InvPreviewDO::getAppId, reqVO.getAppId())
                .like(ObjectUtil.isNotNull(reqVO.getItemName()),InvPreviewDO::getItemName, reqVO.getItemName())
                .like(ObjectUtil.isNotNull(reqVO.getShortName()),InvPreviewDO::getShortName, reqVO.getShortName())
                .like(ObjectUtil.isNotNull(reqVO.getMarketHashName()),InvPreviewDO::getMarketHashName, reqVO.getMarketHashName())
                .eq(ObjectUtil.isNotNull(reqVO.getImageUrl()),InvPreviewDO::getImageUrl, reqVO.getImageUrl())
                .eq(ObjectUtil.isNotNull(reqVO.getItemInfo()),InvPreviewDO::getItemInfo, reqVO.getItemInfo())
                .eq(ObjectUtil.isNotNull(reqVO.getSellType()),InvPreviewDO::getSellType, reqVO.getSellType())
                .eq(ObjectUtil.isNotNull(reqVO.getCurrencyId()),InvPreviewDO::getCurrencyId, reqVO.getCurrencyId())
                .eq(ObjectUtil.isNotNull(reqVO.getCnyPrice()),InvPreviewDO::getCnyPrice, reqVO.getCnyPrice())
                .eq(ObjectUtil.isNotNull(reqVO.getSalePrice()),InvPreviewDO::getSalePrice, reqVO.getSalePrice())
                .eq(ObjectUtil.isNotNull(reqVO.getSubsidyPrice()),InvPreviewDO::getSubsidyPrice, reqVO.getSubsidyPrice())
                .eq(ObjectUtil.isNotNull(reqVO.getActivityTag()),InvPreviewDO::getActivityTag, reqVO.getActivityTag())
                .eq(ObjectUtil.isNotNull(reqVO.getTagList()),InvPreviewDO::getTagList, reqVO.getTagList())
                .eq(ObjectUtil.isNotNull( reqVO.getSubsidyTag()),InvPreviewDO::getSubsidyTag, reqVO.getSubsidyTag())
                .eq(ObjectUtil.isNotNull(reqVO.getAutoPrice()),InvPreviewDO::getAutoPrice, reqVO.getAutoPrice())
                .eq(ObjectUtil.isNotNull(reqVO.getAutoQuantity()),InvPreviewDO::getAutoQuantity, reqVO.getAutoQuantity())
                .eq(ObjectUtil.isNotNull(reqVO.getReferencePrice()),InvPreviewDO::getReferencePrice, reqVO.getReferencePrice())
                .eq(ObjectUtil.isNotNull(reqVO.getSelQuality()),InvPreviewDO::getSelQuality, reqVO.getSelQuality())
                .eq(ObjectUtil.isNotNull(reqVO.getSelItemset()),InvPreviewDO::getSelItemset, reqVO.getSelItemset())
                .eq(ObjectUtil.isNotNull(reqVO.getSelExterior()),InvPreviewDO::getSelExterior, reqVO.getSelExterior())
                .eq(ObjectUtil.isNotNull( reqVO.getSelRarity()),InvPreviewDO::getSelRarity, reqVO.getSelRarity())
                .eq(ObjectUtil.isNotNull(reqVO.getExistInv()),InvPreviewDO::getExistInv, reqVO.getExistInv())
                .ge(ObjectUtil.isNotNull(reqVO.getMinPrice()),InvPreviewDO::getMinPrice,reqVO.getMinPrice())
                .le(ObjectUtil.isNotNull(reqVO.getMaxPrice()),InvPreviewDO::getMinPrice,reqVO.getMaxPrice())
                .orderByDesc(InvPreviewDO::getId);
        if (reqVO.getType() != null){
            queryWrapper.lambda().and(wrapper -> {
                if (ObjectUtil.isNotNull(reqVO.getType())) {
                    wrapper.or().eq(InvPreviewDO::getSelType, reqVO.getType());
                }
                if (ObjectUtil.isNotNull(reqVO.getType())) {
                    wrapper.or().eq(InvPreviewDO::getSelWeapon, reqVO.getType());
                }
            });
            return selectPage(reqVO,queryWrapper);
        }
        return selectPage(reqVO,queryWrapper);
    }
    default PageResult<InvPreviewDO> hotPage(InvPreviewPageReqVO reqVO) {
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
                .geIfPresent(InvPreviewDO::getMinPrice,reqVO.getMinPrice())
                .leIfPresent(InvPreviewDO::getMinPrice,reqVO.getMaxPrice())
                .orderByDesc(InvPreviewDO::getUpdateTime));
    }

}