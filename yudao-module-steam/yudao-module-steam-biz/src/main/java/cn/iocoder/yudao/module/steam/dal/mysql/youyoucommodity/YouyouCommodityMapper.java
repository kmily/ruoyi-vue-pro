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
                .betweenIfPresent(YouyouCommodityDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(YouyouCommodityDO::getCommodityStickers, reqVO.getCommodityStickers())
                .eqIfPresent(YouyouCommodityDO::getCommodityDoppler, reqVO.getCommodityDoppler())
                .eqIfPresent(YouyouCommodityDO::getCommodityFade, reqVO.getCommodityFade())
                .eqIfPresent(YouyouCommodityDO::getCommodityHardened, reqVO.getCommodityHardened())
                .eqIfPresent(YouyouCommodityDO::getTransferStatus, reqVO.getTransferStatus())
                .eqIfPresent(YouyouCommodityDO::getStatus, reqVO.getStatus())
                .orderByDesc(YouyouCommodityDO::getId));
    }

}