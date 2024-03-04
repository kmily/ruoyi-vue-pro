package cn.iocoder.yudao.module.steam.dal.mysql.youyougoodslist;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyougoodslist.YouyouGoodslistDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist.vo.*;

/**
 * 查询商品列 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface YouyouGoodslistMapper extends BaseMapperX<YouyouGoodslistDO> {

    default PageResult<YouyouGoodslistDO> selectPage(YouyouGoodslistPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<YouyouGoodslistDO>()
                .eqIfPresent(YouyouGoodslistDO::getTemplateId, reqVO.getTemplateId())
                .likeIfPresent(YouyouGoodslistDO::getCommodityName, reqVO.getCommodityName())
                .eqIfPresent(YouyouGoodslistDO::getCommodityPrice, reqVO.getCommodityPrice())
                .eqIfPresent(YouyouGoodslistDO::getCommodityAbrade, reqVO.getCommodityAbrade())
                .eqIfPresent(YouyouGoodslistDO::getCommodityPaintSeed, reqVO.getCommodityPaintSeed())
                .eqIfPresent(YouyouGoodslistDO::getCommodityPaintIndex, reqVO.getCommodityPaintIndex())
                .eqIfPresent(YouyouGoodslistDO::getCommodityHaveNameTag, reqVO.getCommodityHaveNameTag())
                .eqIfPresent(YouyouGoodslistDO::getCommodityHaveBuzhang, reqVO.getCommodityHaveBuzhang())
                .eqIfPresent(YouyouGoodslistDO::getCommodityHaveSticker, reqVO.getCommodityHaveSticker())
                .eqIfPresent(YouyouGoodslistDO::getShippingMode, reqVO.getShippingMode())
                .eqIfPresent(YouyouGoodslistDO::getTemplateisFade, reqVO.getTemplateisFade())
                .eqIfPresent(YouyouGoodslistDO::getTemplateisHardened, reqVO.getTemplateisHardened())
                .eqIfPresent(YouyouGoodslistDO::getTemplateisDoppler, reqVO.getTemplateisDoppler())
                .eqIfPresent(YouyouGoodslistDO::getCommodityStickersStickerId, reqVO.getCommodityStickersStickerId())
                .eqIfPresent(YouyouGoodslistDO::getCommodityStickersRawIndex, reqVO.getCommodityStickersRawIndex())
                .likeIfPresent(YouyouGoodslistDO::getCommodityStickersName, reqVO.getCommodityStickersName())
                .likeIfPresent(YouyouGoodslistDO::getCommodityStickersHashName, reqVO.getCommodityStickersHashName())
                .eqIfPresent(YouyouGoodslistDO::getCommodityStickersMaterial, reqVO.getCommodityStickersMaterial())
                .eqIfPresent(YouyouGoodslistDO::getCommodityStickersImgUrl, reqVO.getCommodityStickersImgUrl())
                .eqIfPresent(YouyouGoodslistDO::getCommodityStickersPrice, reqVO.getCommodityStickersPrice())
                .eqIfPresent(YouyouGoodslistDO::getCommodityStickersAbrade, reqVO.getCommodityStickersAbrade())
                .eqIfPresent(YouyouGoodslistDO::getCommodityDopplerTitle, reqVO.getCommodityDopplerTitle())
                .eqIfPresent(YouyouGoodslistDO::getCommodityDopplerAbbrTitle, reqVO.getCommodityDopplerAbbrTitle())
                .eqIfPresent(YouyouGoodslistDO::getCommodityDopplerColor, reqVO.getCommodityDopplerColor())
                .eqIfPresent(YouyouGoodslistDO::getCommodityFadeTitle, reqVO.getCommodityFadeTitle())
                .eqIfPresent(YouyouGoodslistDO::getCommodityFadeNumerialValue, reqVO.getCommodityFadeNumerialValue())
                .eqIfPresent(YouyouGoodslistDO::getCommodityFadeColor, reqVO.getCommodityFadeColor())
                .eqIfPresent(YouyouGoodslistDO::getCommodityHardenedTitle, reqVO.getCommodityHardenedTitle())
                .eqIfPresent(YouyouGoodslistDO::getCommodityHardenedAbbrTitle, reqVO.getCommodityHardenedAbbrTitle())
                .eqIfPresent(YouyouGoodslistDO::getCommodityHardenedColor, reqVO.getCommodityHardenedColor())
                .betweenIfPresent(YouyouGoodslistDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(YouyouGoodslistDO::getId));
    }

}