package cn.iocoder.yudao.module.steam.dal.mysql.selquality;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.selquality.SelQualityDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.*;

/**
 * 类别选择 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SelQualityMapper extends BaseMapperX<SelQualityDO> {

    default PageResult<SelQualityDO> selectPage(SelQualityPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SelQualityDO>()
                .likeIfPresent(SelQualityDO::getInternalName, reqVO.getInternalName())
                .likeIfPresent(SelQualityDO::getLocalizedTagName, reqVO.getLocalizedTagName())
                .eqIfPresent(SelQualityDO::getColor, reqVO.getColor())
                .betweenIfPresent(SelQualityDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SelQualityDO::getId));
    }

}