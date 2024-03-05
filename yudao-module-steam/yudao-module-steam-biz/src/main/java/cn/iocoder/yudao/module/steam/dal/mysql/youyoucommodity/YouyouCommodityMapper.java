package cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.*;

/**
 * 悠悠商品列表 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface YouyouCommodityMapper extends BaseMapperX<YouyouCommodityDO> {

    default PageResult<YouyouCommodityDO> selectPage(YouyouCommodityPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<YouyouCommodityDO>()
                .eqIfPresent(YouyouCommodityDO::getId, reqVO.getId())
                .eqIfPresent(YouyouCommodityDO::getTemplateId, reqVO.getTemplateId())
                .likeIfPresent(YouyouCommodityDO::getCommodityName, reqVO.getCommodityName())
                .eqIfPresent(YouyouCommodityDO::getCommodityPrice, reqVO.getCommodityPrice())
                .eqIfPresent(YouyouCommodityDO::getCommodityAbrade, reqVO.getCommodityAbrade())
                .eqIfPresent(YouyouCommodityDO::getCommodityPaintSeed, reqVO.getCommodityPaintSeed())
                .eqIfPresent(YouyouCommodityDO::getCommodityPaintIndex, reqVO.getCommodityPaintIndex())
                .eqIfPresent(YouyouCommodityDO::getCommodityHaveNameTag, reqVO.getCommodityHaveNameTag())
                .eqIfPresent(YouyouCommodityDO::getCommodityHaveBuzhang, reqVO.getCommodityHaveBuzhang())
                .eqIfPresent(YouyouCommodityDO::getCommodityHaveSticker, reqVO.getCommodityHaveSticker())
                .eqIfPresent(YouyouCommodityDO::getShippingMode, reqVO.getShippingMode())
                .eqIfPresent(YouyouCommodityDO::getTemplateisFade, reqVO.getTemplateisFade())
                .eqIfPresent(YouyouCommodityDO::getTemplateisHardened, reqVO.getTemplateisHardened())
                .eqIfPresent(YouyouCommodityDO::getTemplateisDoppler, reqVO.getTemplateisDoppler())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickersStickerId, reqVO.getCommodityStickersStickerId())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickersRawIndex, reqVO.getCommodityStickersRawIndex())
                .likeIfPresent(YouyouCommodityDO::getCommodityStickersName, reqVO.getCommodityStickersName())
                .likeIfPresent(YouyouCommodityDO::getCommodityStickersHashName, reqVO.getCommodityStickersHashName())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickersMaterial, reqVO.getCommodityStickersMaterial())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickersImgUrl, reqVO.getCommodityStickersImgUrl())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickersPrice, reqVO.getCommodityStickersPrice())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickersAbrade, reqVO.getCommodityStickersAbrade())
                .eqIfPresent(YouyouCommodityDO::getCommodityDopplerTitle, reqVO.getCommodityDopplerTitle())
                .eqIfPresent(YouyouCommodityDO::getCommodityDopplerAbbrTitle, reqVO.getCommodityDopplerAbbrTitle())
                .eqIfPresent(YouyouCommodityDO::getCommodityDopplerColor, reqVO.getCommodityDopplerColor())
                .eqIfPresent(YouyouCommodityDO::getCommodityFadeTitle, reqVO.getCommodityFadeTitle())
                .eqIfPresent(YouyouCommodityDO::getCommodityFadeNumerialValue, reqVO.getCommodityFadeNumerialValue())
                .eqIfPresent(YouyouCommodityDO::getCommodityFadeColor, reqVO.getCommodityFadeColor())
                .eqIfPresent(YouyouCommodityDO::getCommodityHardenedTitle, reqVO.getCommodityHardenedTitle())
                .eqIfPresent(YouyouCommodityDO::getCommodityHardenedAbbrTitle, reqVO.getCommodityHardenedAbbrTitle())
                .eqIfPresent(YouyouCommodityDO::getCommodityHardenedColor, reqVO.getCommodityHardenedColor())
                .eqIfPresent(YouyouCommodityDO::getTransferStatus, reqVO.getTransferStatus())
                .betweenIfPresent(YouyouCommodityDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(YouyouCommodityDO::getId));
    }

}