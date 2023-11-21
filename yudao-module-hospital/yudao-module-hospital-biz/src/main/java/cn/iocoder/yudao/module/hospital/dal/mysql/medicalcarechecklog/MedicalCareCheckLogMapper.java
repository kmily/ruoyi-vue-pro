package cn.iocoder.yudao.module.hospital.dal.mysql.medicalcarechecklog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcarechecklog.MedicalCareCheckLogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo.*;

/**
 * 医护审核记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MedicalCareCheckLogMapper extends BaseMapperX<MedicalCareCheckLogDO>,BaseMapper<MedicalCareCheckLogDO> {

    default PageResult<MedicalCareCheckLogDO> selectPage(MedicalCareCheckLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MedicalCareCheckLogDO>()
                .eqIfPresent(MedicalCareCheckLogDO::getCareId, reqVO.getCareId())
                .likeIfPresent(MedicalCareCheckLogDO::getCheckName, reqVO.getCheckName())
                .betweenIfPresent(MedicalCareCheckLogDO::getCheckTime, reqVO.getCheckTime())
                .eqIfPresent(MedicalCareCheckLogDO::getOpinion, reqVO.getOpinion())
                .eqIfPresent(MedicalCareCheckLogDO::getCheckStatus, reqVO.getCheckStatus())
                .betweenIfPresent(MedicalCareCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MedicalCareCheckLogDO::getId));
    }

    default List<MedicalCareCheckLogDO> selectList(MedicalCareCheckLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MedicalCareCheckLogDO>()
                .eqIfPresent(MedicalCareCheckLogDO::getCareId, reqVO.getCareId())
                .likeIfPresent(MedicalCareCheckLogDO::getCheckName, reqVO.getCheckName())
                .betweenIfPresent(MedicalCareCheckLogDO::getCheckTime, reqVO.getCheckTime())
                .eqIfPresent(MedicalCareCheckLogDO::getOpinion, reqVO.getOpinion())
                .eqIfPresent(MedicalCareCheckLogDO::getCheckStatus, reqVO.getCheckStatus())
                .betweenIfPresent(MedicalCareCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MedicalCareCheckLogDO::getId));
    }

}
