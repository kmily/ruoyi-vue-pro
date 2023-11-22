package cn.iocoder.yudao.module.hospital.dal.mysql.medicalcare;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.CareFavoriteDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.*;

/**
 * 医护收藏 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CareFavoriteMapper extends BaseMapperX<CareFavoriteDO>,BaseMapper<CareFavoriteDO> {

    default PageResult<CareFavoriteDO> selectPage(AppCareFavoritePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CareFavoriteDO>()
                .eqIfPresent(CareFavoriteDO::getMemberId, reqVO.getMemeberId())
                .eqIfPresent(CareFavoriteDO::getCareId, reqVO.getCareId())
                .betweenIfPresent(CareFavoriteDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareFavoriteDO::getId));
    }

}
