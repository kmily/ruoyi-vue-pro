package cn.iocoder.yudao.module.steam.dal.mysql.otherselling;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.*;

/**
 * 其他平台在售 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface OtherSellingMapper extends BaseMapperX<OtherSellingDO> {

    default PageResult<OtherSellingDO> selectPage(OtherSellingPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OtherSellingDO>()
                .eqIfPresent(OtherSellingDO::getAppid, reqVO.getAppid())
                .eqIfPresent(OtherSellingDO::getAssetid, reqVO.getAssetid())
                .eqIfPresent(OtherSellingDO::getClassid, reqVO.getClassid())
                .eqIfPresent(OtherSellingDO::getInstanceid, reqVO.getInstanceid())
                .eqIfPresent(OtherSellingDO::getAmount, reqVO.getAmount())
                .betweenIfPresent(OtherSellingDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(OtherSellingDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OtherSellingDO::getPrice, reqVO.getPrice())
                .eqIfPresent(OtherSellingDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(OtherSellingDO::getContextid, reqVO.getContextid())
                .eqIfPresent(OtherSellingDO::getIconUrl, reqVO.getIconUrl())
                .likeIfPresent(OtherSellingDO::getMarketName, reqVO.getMarketName())
                .eqIfPresent(OtherSellingDO::getSelQuality, reqVO.getSelQuality())
                .eqIfPresent(OtherSellingDO::getSelItemset, reqVO.getSelItemset())
                .eqIfPresent(OtherSellingDO::getSelWeapon, reqVO.getSelWeapon())
                .eqIfPresent(OtherSellingDO::getSelExterior, reqVO.getSelExterior())
                .eqIfPresent(OtherSellingDO::getSelRarity, reqVO.getSelRarity())
                .eqIfPresent(OtherSellingDO::getSelType, reqVO.getSelType())
                .likeIfPresent(OtherSellingDO::getMarketHashName, reqVO.getMarketHashName())
                .eqIfPresent(OtherSellingDO::getDisplayWeight, reqVO.getDisplayWeight())
                .eqIfPresent(OtherSellingDO::getItemInfo, reqVO.getItemInfo())
                .likeIfPresent(OtherSellingDO::getShortName, reqVO.getShortName())
                .likeIfPresent(OtherSellingDO::getSellingUserName, reqVO.getSellingUserName())
                .eqIfPresent(OtherSellingDO::getSellingAvator, reqVO.getSellingAvator())
                .eqIfPresent(OtherSellingDO::getSellingUserId, reqVO.getSellingUserId())
                .eqIfPresent(OtherSellingDO::getPlatformIdentity, reqVO.getPlatformIdentity())
                .eqIfPresent(OtherSellingDO::getSteamId, reqVO.getSteamId())
                .orderByDesc(OtherSellingDO::getId));
    }

}