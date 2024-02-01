package cn.iocoder.yudao.module.steam.dal.mysql.selexterior;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.selexterior.SelExteriorDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.*;

/**
 * 外观选择 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SelExteriorMapper extends BaseMapperX<SelExteriorDO> {

    default PageResult<SelExteriorDO> selectPage(SelExteriorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SelExteriorDO>()
                .likeIfPresent(SelExteriorDO::getInternalName, reqVO.getInternalName())
                .likeIfPresent(SelExteriorDO::getLocalizedTagName, reqVO.getLocalizedTagName())
                .betweenIfPresent(SelExteriorDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SelExteriorDO::getId));
    }

}