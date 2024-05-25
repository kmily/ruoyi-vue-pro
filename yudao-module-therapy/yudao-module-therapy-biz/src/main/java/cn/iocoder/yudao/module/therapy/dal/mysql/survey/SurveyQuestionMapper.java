package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyQuestionMapper extends BaseMapperX<QuestionDO> {
    /**
     * 通过问卷id删除问卷题目
     *
     * @param surveyId
     */
    default void deleteBySurveyId(Long surveyId) {
        LambdaQueryWrapper<QuestionDO> lambdaUpdateWrapper = new LambdaQueryWrapper<QuestionDO>()
                .eq(QuestionDO::getBelongSurveyId, surveyId);
        delete(lambdaUpdateWrapper);
    }

    default List<QuestionDO> selectBySurveyId(Long surveyId){
        LambdaQueryWrapper<QuestionDO> lambdaUpdateWrapper = new LambdaQueryWrapper<QuestionDO>()
                .eq(QuestionDO::getBelongSurveyId, surveyId);

        return selectList(lambdaUpdateWrapper);
    }
}
