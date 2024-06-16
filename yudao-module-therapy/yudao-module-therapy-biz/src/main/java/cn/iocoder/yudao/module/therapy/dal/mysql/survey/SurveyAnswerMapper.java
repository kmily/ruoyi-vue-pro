package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Mapper
public interface SurveyAnswerMapper extends BaseMapperX<SurveyAnswerDO> {
    default PageResult<SurveyAnswerDO> selectPage(SurveyAnswerPageReqVO reqVO) {
        // 分页查询
        return selectPage(reqVO, new LambdaQueryWrapperX<SurveyAnswerDO>()
                .eq(SurveyAnswerDO::getCreator, reqVO.getUserId().toString())
                .eqIfPresent(SurveyAnswerDO::getSurveyType, reqVO.getSurveyType())
                .orderByDesc(SurveyAnswerDO::getId));
    }

    default SurveyAnswerDO selectBySurveyIdAndUserId(Long surveyId) {
        return selectOne(Wrappers.lambdaQuery(SurveyAnswerDO.class)
                .eq(SurveyAnswerDO::getBelongSurveyId, surveyId)
                .eq(SurveyAnswerDO::getCreator, getLoginUserId()));
    }

    default List<SurveyAnswerDO> selectBySurveysAndDate(Long userId, LocalDate begin, LocalDate end, List<Integer> types) {
        return selectList(new LambdaQueryWrapperX<SurveyAnswerDO>()
                .eq(SurveyAnswerDO::getCreator, userId)
                .betweenIfPresent(SurveyAnswerDO::getCreateTime, begin, end)
                .in(SurveyAnswerDO::getSurveyType, types));

    }

    default List<SurveyAnswerDO> selectBySurveyTypeAndUserId(Long userId, List<Integer> surveyType) {
        return selectList(Wrappers.lambdaQuery(SurveyAnswerDO.class)
                .in(SurveyAnswerDO::getSurveyType, surveyType)
                .eq(SurveyAnswerDO::getCreator, userId));
    }
}
