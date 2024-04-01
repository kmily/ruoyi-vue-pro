package cn.iocoder.yudao.module.steam.dal.mysql.hotwords;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.hotwords.HotWordsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo.*;

/**
 * 热词搜索 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface HotWordsMapper extends BaseMapperX<HotWordsDO> {

    default PageResult<HotWordsDO> selectPage(HotWordsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HotWordsDO>()
                .eqIfPresent(HotWordsDO::getHotWords, reqVO.getHotWords())
                .eqIfPresent(HotWordsDO::getMarketName, reqVO.getMarketName())
                .betweenIfPresent(HotWordsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HotWordsDO::getId));
    }

}