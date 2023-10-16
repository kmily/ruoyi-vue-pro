package cn.iocoder.yudao.module.product.dal.mysql.unit;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.product.dal.dataobject.unit.UnitDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.product.controller.admin.unit.vo.*;

/**
 * 计量单位 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UnitMapper extends BaseMapperX<UnitDO> {

    default PageResult<UnitDO> selectPage(UnitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UnitDO>()
                .likeIfPresent(UnitDO::getName, reqVO.getName())
                .betweenIfPresent(UnitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UnitDO::getId));
    }

    default List<UnitDO> selectList(UnitExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<UnitDO>()
                .likeIfPresent(UnitDO::getName, reqVO.getName())
                .betweenIfPresent(UnitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UnitDO::getId));
    }

}
