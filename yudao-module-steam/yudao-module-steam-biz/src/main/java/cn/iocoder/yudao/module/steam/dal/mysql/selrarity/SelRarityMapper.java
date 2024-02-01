package cn.iocoder.yudao.module.steam.dal.mysql.selrarity;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.selrarity.SelRarityDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.*;

/**
 * 品质选择 Mapper
 *
 * @author lgm
 */
@Mapper
public interface SelRarityMapper extends BaseMapperX<SelRarityDO> {

    default PageResult<SelRarityDO> selectPage(SelRarityPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SelRarityDO>()
                .likeIfPresent(SelRarityDO::getLocalizedTagName, reqVO.getLocalizedTagName())
                .likeIfPresent(SelRarityDO::getInternalName, reqVO.getInternalName())
                .eqIfPresent(SelRarityDO::getColor, reqVO.getColor())
                .betweenIfPresent(SelRarityDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SelRarityDO::getId));
    }

}