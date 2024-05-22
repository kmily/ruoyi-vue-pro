package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SurveyAnAnswerMapper extends BaseMapperX<QuestionDO> {

}
