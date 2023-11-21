package cn.iocoder.yudao.module.hospital.dal.mysql.careaptitude;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.*;

/**
 * 医护资质 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CareAptitudeMapper extends BaseMapperX<CareAptitudeDO>,BaseMapper<CareAptitudeDO> {

    default PageResult<CareAptitudeDO> selectPage(AppCareAptitudePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CareAptitudeDO>()
                .eqIfPresent(CareAptitudeDO::getCareId, reqVO.getCareId())
                .betweenIfPresent(CareAptitudeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareAptitudeDO::getId));
    }

    default List<CareAptitudeDO> selectList(AppCareAptitudeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CareAptitudeDO>()
                .eqIfPresent(CareAptitudeDO::getCareId, reqVO.getCareId())
                .betweenIfPresent(CareAptitudeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareAptitudeDO::getId));
    }

}
