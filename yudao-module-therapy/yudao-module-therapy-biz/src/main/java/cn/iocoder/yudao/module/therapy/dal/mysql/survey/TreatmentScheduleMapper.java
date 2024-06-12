package cn.iocoder.yudao.module.therapy.dal.mysql.survey;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentScheduleDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 患者日程 Mapper
 *
 * @author CBI系统管理员
 */
@Mapper
public interface TreatmentScheduleMapper extends BaseMapperX<TreatmentScheduleDO> {

//    default PageResult<TreatmentScheduleDO> selectPage(TreatmentSchedulePageReqVO reqVO) {
//        return selectPage(reqVO, new LambdaQueryWrapperX<TreatmentScheduleDO>()
//                .eqIfPresent(TreatmentScheduleDO::getUserId, reqVO.getUserId())
//                .likeIfPresent(TreatmentScheduleDO::getName, reqVO.getName())
//                .eqIfPresent(TreatmentScheduleDO::getEstimateCompletedRate, reqVO.getEstimateCompletedRate())
//                .betweenIfPresent(TreatmentScheduleDO::getBeginTime, reqVO.getBeginTime())
//                .betweenIfPresent(TreatmentScheduleDO::getEndTime, reqVO.getEndTime())
//                .eqIfPresent(TreatmentScheduleDO::getStage, reqVO.getStage())
//                .eqIfPresent(TreatmentScheduleDO::getRemark, reqVO.getRemark())
//                .eqIfPresent(TreatmentScheduleDO::getUnfinishedCause, reqVO.getUnfinishedCause())
//                .eqIfPresent(TreatmentScheduleDO::getSolution, reqVO.getSolution())
//                .eqIfPresent(TreatmentScheduleDO::getBeforeScore, reqVO.getBeforeScore())
//                .eqIfPresent(TreatmentScheduleDO::getAfterScore, reqVO.getAfterScore())
//                .betweenIfPresent(TreatmentScheduleDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(TreatmentScheduleDO::getId));
//    }

    default List<TreatmentScheduleDO> getScheduleList(LocalDate day){
        return selectList(Wrappers.lambdaQuery(TreatmentScheduleDO.class)
                .eq(TreatmentScheduleDO::getUserId,getLoginUserId())
                .le(TreatmentScheduleDO::getBeginTime,day)
                .ge(TreatmentScheduleDO::getEndTime,day));
    }

}
