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
                .eqIfPresent(YouyouCommodityDO::getTransferStatus, reqVO.getTransferStatus())
                .betweenIfPresent(YouyouCommodityDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(YouyouCommodityDO::getId));
    }

}