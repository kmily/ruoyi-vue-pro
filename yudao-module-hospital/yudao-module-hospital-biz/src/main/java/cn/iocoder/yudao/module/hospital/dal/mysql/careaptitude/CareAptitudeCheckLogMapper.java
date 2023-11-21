package cn.iocoder.yudao.module.hospital.dal.mysql.careaptitude;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeCheckLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.*;

/**
 * 医护资质审核记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CareAptitudeCheckLogMapper extends BaseMapperX<CareAptitudeCheckLogDO>,BaseMapper<CareAptitudeCheckLogDO> {

    default PageResult<CareAptitudeCheckLogDO> selectPage(CareAptitudeCheckLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CareAptitudeCheckLogDO>()
                .eqIfPresent(CareAptitudeCheckLogDO::getCareId, reqVO.getCareId())
                .eqIfPresent(CareAptitudeCheckLogDO::getAptitudeId, reqVO.getAptitudeId())
                .betweenIfPresent(CareAptitudeCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareAptitudeCheckLogDO::getId));
    }

    default List<CareAptitudeCheckLogDO> selectList(CareAptitudeCheckLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CareAptitudeCheckLogDO>()
                .eqIfPresent(CareAptitudeCheckLogDO::getCareId, reqVO.getCareId())
                .eqIfPresent(CareAptitudeCheckLogDO::getAptitudeId, reqVO.getAptitudeId())
                .betweenIfPresent(CareAptitudeCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CareAptitudeCheckLogDO::getId));
    }

}
