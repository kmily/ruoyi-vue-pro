package cn.iocoder.yudao.module.therapy.dal.mysql.survey;


import cn.iocoder.boot.module.therapy.enums.SignState;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentScheduleDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 患者日程 Mapper
 *
 * @author CBI系统管理员
 */
@Mapper
public interface TreatmentScheduleMapper extends BaseMapperX<TreatmentScheduleDO> {

    default PageResult<TreatmentScheduleDO> selectPage(SurveyAnswerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TreatmentScheduleDO>()
                .eqIfPresent(TreatmentScheduleDO::getUserId, reqVO.getUserId())
                .orderByDesc(TreatmentScheduleDO::getId));
    }

    default List<TreatmentScheduleDO> getScheduleList(LocalDate day) {
        return selectList(Wrappers.lambdaQuery(TreatmentScheduleDO.class)
                .eq(TreatmentScheduleDO::getUserId, getLoginUserId())
                .between(TreatmentScheduleDO::getBeginTime, day,day.plusDays(1)));
    }

    default Map<String, Object> statSchedule(LocalDate begin, LocalDate end,Long userId) {
        MPJLambdaWrapperX<TreatmentScheduleDO> wrapperX = new MPJLambdaWrapperX();
        wrapperX.selectCount(TreatmentScheduleDO::getId, "count")
                .selectSum(TreatmentScheduleDO::getAfterScore, "score")
                .eqIfPresent(TreatmentScheduleDO::getUserId, userId)
                .betweenIfPresent(TreatmentScheduleDO::getSignInTime, begin, end)
                .ge(TreatmentScheduleDO::getState, SignState.SIGNED.getType());
        List<Map<String, Object>> res = selectMaps(wrapperX);
        return Objects.nonNull(res) && res.size() > 0 ? res.get(0) : null;
    }

    default Long countByUserId(Long userId) {
        return selectCount(Wrappers.lambdaQuery(TreatmentScheduleDO.class)
                .eq(TreatmentScheduleDO::getCreator,userId));
    }



}
