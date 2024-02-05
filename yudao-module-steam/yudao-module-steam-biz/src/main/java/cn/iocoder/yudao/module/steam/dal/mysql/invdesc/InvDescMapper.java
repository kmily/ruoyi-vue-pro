package cn.iocoder.yudao.module.steam.dal.mysql.invdesc;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.*;

/**
 * 库存信息详情 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InvDescMapper extends BaseMapperX<InvDescDO> {

    default PageResult<InvDescDO> selectPage(InvDescPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InvDescDO>()
                .eqIfPresent(InvDescDO::getAppid, reqVO.getAppid())
                .eqIfPresent(InvDescDO::getClassid, reqVO.getClassid())
                .eqIfPresent(InvDescDO::getInstanceid, reqVO.getInstanceid())
                .eqIfPresent(InvDescDO::getCurrency, reqVO.getCurrency())
                .eqIfPresent(InvDescDO::getBackgroundColor, reqVO.getBackgroundColor())
                .eqIfPresent(InvDescDO::getTradable, reqVO.getTradable())
                .eqIfPresent(InvDescDO::getActions, reqVO.getActions())
                .eqIfPresent(InvDescDO::getFraudwarnings, reqVO.getFraudwarnings())
                .likeIfPresent(InvDescDO::getName, reqVO.getName())
                .eqIfPresent(InvDescDO::getNameColor, reqVO.getNameColor())
                .eqIfPresent(InvDescDO::getType, reqVO.getType())
                .likeIfPresent(InvDescDO::getMarketName, reqVO.getMarketName())
                .likeIfPresent(InvDescDO::getMarketHashName, reqVO.getMarketHashName())
                .eqIfPresent(InvDescDO::getMarketActions, reqVO.getMarketActions())
                .eqIfPresent(InvDescDO::getCommodity, reqVO.getCommodity())
                .eqIfPresent(InvDescDO::getMarketTradableRestriction, reqVO.getMarketTradableRestriction())
                .eqIfPresent(InvDescDO::getMarketable, reqVO.getMarketable())
                .eqIfPresent(InvDescDO::getSelQuality, reqVO.getSelQuality())
                .eqIfPresent(InvDescDO::getSelItemset, reqVO.getSelItemset())
                .eqIfPresent(InvDescDO::getSelWeapon, reqVO.getSelWeapon())
                .eqIfPresent(InvDescDO::getSelExterior, reqVO.getSelExterior())
                .eqIfPresent(InvDescDO::getSelRarity, reqVO.getSelRarity())
                .eqIfPresent(InvDescDO::getSelType, reqVO.getSelType())
                .eqIfPresent(InvDescDO::getSteamId, reqVO.getSteamId())
                .orderByDesc(InvDescDO::getId));
    }

}