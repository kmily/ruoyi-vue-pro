package cn.iocoder.yudao.module.steam.dal.mysql.seltype;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelTypeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.*;

/**
 * 类型选择 Mapper
 *
 * @author glzaboy
 */
@Mapper
public interface SelTypeMapper extends BaseMapperX<SelTypeDO> {

    default PageResult<SelTypeDO> selectPage(SelTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SelTypeDO>()
                .likeIfPresent(SelTypeDO::getLocalizedTagName, reqVO.getLocalizedTagName())
                .eqIfPresent(SelTypeDO::getColor, reqVO.getColor())
                .orderByDesc(SelTypeDO::getId));
    }

}