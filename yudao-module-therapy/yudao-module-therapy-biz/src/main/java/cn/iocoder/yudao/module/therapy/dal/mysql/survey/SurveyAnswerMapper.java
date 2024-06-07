package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jodd.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Mapper
public interface SurveyAnswerMapper extends BaseMapperX<SurveyAnswerDO> {
    default PageResult<SurveyAnswerDO> selectPage(SurveyAnswerPageReqVO reqVO) {
        // 分页查询
        return selectPage(reqVO, new LambdaQueryWrapperX<SurveyAnswerDO>()
                .eq(SurveyAnswerDO::getCreator, reqVO.getUserId().toString())
                .eqIfPresent(SurveyAnswerDO::getSurveyType,reqVO.getSurveyType())
                .orderByDesc(SurveyAnswerDO::getId));
    }

    default SurveyAnswerDO selectBySurveyIdAndUserId(Long surveyId){
        return selectOne(Wrappers.lambdaQuery(SurveyAnswerDO.class)
                .eq(SurveyAnswerDO::getBelongSurveyId,surveyId)
                .eq(SurveyAnswerDO::getCreator,getLoginUserId()));
    }
}
