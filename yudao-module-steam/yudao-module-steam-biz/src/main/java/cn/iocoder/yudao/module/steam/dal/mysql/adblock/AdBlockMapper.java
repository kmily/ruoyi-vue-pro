package cn.iocoder.yudao.module.steam.dal.mysql.adblock;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.adblock.AdBlockDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.adblock.vo.*;

/**
 * 广告位 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface AdBlockMapper extends BaseMapperX<AdBlockDO> {

    default PageResult<AdBlockDO> selectPage(AdBlockPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdBlockDO>()
                .betweenIfPresent(AdBlockDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(AdBlockDO::getAdName, reqVO.getAdName())
                .orderByDesc(AdBlockDO::getId));
    }

}